# 메가박스 박스오피스 JSON 생성
import requests, json, os

BASE = os.path.dirname(os.path.dirname(__file__))
PATH = os.path.join(BASE, "data", "movies.json")

url = "https://www.megabox.co.kr/on/oh/oha/Movie/selectMovieList.do"


# 1. 브라우저 정보를 담은 헤더 추가
headers = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
    "Referer": "https://www.megabox.co.kr/movie" # 어디서 왔는지 알려줌
}

# 2. post 요청 시 headers 인자 추가
res = requests.post(
    url, 
    json={"orderType": "boxoffice", "pageNo": 1}, 
    headers=headers # 이 부분이 핵심!
)

# res = requests.post(url, json={"orderType":"boxoffice","pageNo":1})
data = res.json()

print("-"*80)
print(data.get("movieList", []))
print("-"*80)

movies = []
for m in data.get("movieList", []): # movieList가 없으면 빈 리스트 반환
    movies.append({
        "boxoRank": int(m.get("boxoRank", 0)), # 랭킹
        "movieNm": m.get("movieNm", "제목 없음"), #영화명
        "rfilmDe": m.get("rfilmDe", ""), #상영일
        "movieStatNm": m.get("movieStatNm", ""), #상영상태
        "admisClassNm": m.get("admisClassNm", ""), #영화 연령분류
        "imgPathNm": m.get("imgPathNm", ""), #이미지파일명
        "movieSynopCn": m.get("movieSynopCn", ""), #영화 소개
        "playTime": m.get("playTime", "") #상영시간
    })

os.makedirs(os.path.dirname(PATH), exist_ok=True)

with open(PATH, "w", encoding="utf-8") as f:
    json.dump(movies, f, ensure_ascii=False, indent=2)

print("완료:", PATH)