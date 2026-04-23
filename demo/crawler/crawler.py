# 메가박스 박스오피스 TiDB Cloud 직접 저장
import requests
import mysql.connector
import os
import sys
from datetime import datetime, timedelta

sys.stdout.reconfigure(encoding="utf-8")
sys.stderr.reconfigure(encoding="utf-8")

# TiDB Cloud 연결 정보
TIDB_HOST = "gateway01.ap-northeast-1.prod.aws.tidbcloud.com"
TIDB_PORT = 4000
TIDB_USER = "PrkazshsERPiVWq.root"
TIDB_PASSWORD = "iQcYuElTAhN3wGD5"
TIDB_DATABASE = "mydb"

def connect_tidb():
    """TiDB Cloud 연결"""
    try:
        conn = mysql.connector.connect(
            host=TIDB_HOST,
            port=TIDB_PORT,
            user=TIDB_USER,
            password=TIDB_PASSWORD,
            database=TIDB_DATABASE,
            ssl_ca="",  # TiDB Cloud는 SSL 필요
            autocommit=True
        )
        print("TiDB Cloud 연결 성공")
        return conn
    except Exception as e:
        print(f"TiDB Cloud 연결 실패: {e}")
        return None

def create_table_if_not_exists(conn):
    """영화 테이블 생성"""
    try:
        cursor = conn.cursor()
        
        # 한국 시간대 설정
        cursor.execute("SET time_zone = '+09:00'")
        
        # 테이블 삭제 후 재생성 (항상 최신 데이터 유지)
        cursor.execute("DROP TABLE IF EXISTS movies")
        
        create_table_sql = """
        CREATE TABLE movies (
            id INT AUTO_INCREMENT PRIMARY KEY,
            boxoRank INT NOT NULL,
            movieNm VARCHAR(255) NOT NULL,
            rfilmDe VARCHAR(20),
            movieStatNm VARCHAR(50),
            admisClassNm VARCHAR(50),
            imgPathNm VARCHAR(500),
            movieSynopCn TEXT,
            playTime VARCHAR(20),
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
        """
        cursor.execute(create_table_sql)
        print("영화 테이블 생성 완료")
        
    except Exception as e:
        print(f"테이블 생성 실패: {e}")

def save_movies_to_tidb(conn, movies):
    """영화 데이터를 TiDB Cloud에 저장"""
    try:
        cursor = conn.cursor()
        
        # 한국 시간대 설정
        cursor.execute("SET time_zone = '+09:00'")
        
        # 데이터 정제 (HTML 엔티티 제거)
        cleaned_movies = []
        korea_time = datetime.now() + timedelta(hours=9)  # 한국 시간 (UTC+9)
        
        for movie in movies:
            cleaned_movie = {
                'boxoRank': movie.get('boxoRank', 0),
                'movieNm': clean_html_entities(movie.get('movieNm', '')),
                'rfilmDe': clean_html_entities(movie.get('rfilmDe', '')),
                'movieStatNm': clean_html_entities(movie.get('movieStatNm', '')),
                'admisClassNm': clean_html_entities(movie.get('admisClassNm', '')),
                'imgPathNm': clean_html_entities(movie.get('imgPathNm', '')),
                'movieSynopCn': clean_html_entities(movie.get('movieSynopCn', '')),
                'playTime': clean_html_entities(movie.get('playTime', '')),
                'created_at': korea_time.strftime('%Y-%m-%d %H:%M:%S')
            }
            cleaned_movies.append(cleaned_movie)
        
        # 데이터 삽입
        insert_sql = """
        INSERT INTO movies (boxoRank, movieNm, rfilmDe, movieStatNm, admisClassNm, imgPathNm, movieSynopCn, playTime, created_at)
        VALUES (%(boxoRank)s, %(movieNm)s, %(rfilmDe)s, %(movieStatNm)s, %(admisClassNm)s, %(imgPathNm)s, %(movieSynopCn)s, %(playTime)s, %(created_at)s)
        """
        
        cursor.executemany(insert_sql, cleaned_movies)
        print(f"{len(cleaned_movies)}개 영화 데이터 저장 완료")
        
    except Exception as e:
        print(f"데이터 저장 실패: {e}")

def clean_html_entities(text):
    """HTML 엔티티 정제"""
    if not text:
        return ""
    
    return (text.replace("&#40;", "(")
                  .replace("&#41;", ")")
                  .replace("&#39;", "'")
                  .replace("&#34;", "\"")
                  .replace("&#38;", "&")
                  .replace("&#60;", "<")
                  .replace("&#62;", ">")
                  .replace("&#91;", "[")
                  .replace("&#93;", "]"))

def crawl_and_save():
    """메가박스 데이터 크롤링 및 TiDB Cloud 저장"""
    print("메가박스 크롤링 시작...")
    
    # 1. 데이터 크롤링
    url = "https://www.megabox.co.kr/on/oh/oha/Movie/selectMovieList.do"
    
    headers = {
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
        "Referer": "https://www.megabox.co.kr/movie"
    }
    
    try:
        res = requests.post(url, json={"orderType": "boxoffice", "pageNo": 1}, headers=headers)
        data = res.json()
        
        movies = []
        for m in data.get("movieList", []):
            movies.append({
                "boxoRank": int(m.get("boxoRank", 0)),
                "movieNm": m.get("movieNm", "제목 없음"),
                "rfilmDe": m.get("rfilmDe", ""),
                "movieStatNm": m.get("movieStatNm", ""),
                "admisClassNm": m.get("admisClassNm", ""),
                "imgPathNm": m.get("imgPathNm", ""),
                "movieSynopCn": m.get("movieSynopCn", ""),
                "playTime": m.get("playTime", "")
            })
        
        print(f"크롤링한 영화 수: {len(movies)}")
        
        # 2. TiDB Cloud에 저장
        conn = connect_tidb()
        if conn:
            try:
                create_table_if_not_exists(conn)
                save_movies_to_tidb(conn, movies)
                print("모든 작업 완료!")
            finally:
                conn.close()
        
    except Exception as e:
        print(f"크롤링 실패: {e}")

if __name__ == "__main__":
    crawl_and_save()
