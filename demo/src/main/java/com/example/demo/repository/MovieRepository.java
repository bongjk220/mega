package com.example.demo.repository;

import com.example.demo.dto.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MovieRepository {

    private final JdbcTemplate jdbc;

    // 테이블 생성 (rank 예약어 백틱 처리 및 TiDB 최적화)
    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS movie (" +
                     "id BIGINT PRIMARY KEY AUTO_RANDOM, " +
                     "`boxoRank` INT NOT NULL, " +
                     "`movieNm` VARCHAR(255), " +
                     "`rfilmDe` DATE, " +
                     "`movieStatNm` VARCHAR(50), " +
                     "`admisClassNm` VARCHAR(100), " +
                     "`imgPathNm` VARCHAR(100), " +
                     "`movieSynopCn` VARCHAR(1000), " +
                     "`playTime` VARCHAR(100), " +
                     "INDEX idx_rank (`boxoRank`)" +
                     ")";
        jdbc.execute(sql);
    }

    // 전체 삭제
    public void deleteAll() {
        jdbc.update("DELETE FROM movie");
    }

    // 저장 (날짜 파싱 에러 방지 로직 추가)
    public void saveAll(List<Movie> list) {
        // INSERT 문에서도 rank 컬럼에 백틱을 사용하는 것이 안전합니다.
        String sql = "INSERT INTO movie(`boxoRank`, "
                   + "`movieNm`, "
                   + "`rfilmDe`, "
                   + "`movieStatNm`, "
                   + "`admisClassNm`, "
                   + "`imgPathNm`, "
                   + "`movieSynopCn`, "
                   + "`playTime`) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";


        for (Movie m : list) {
            jdbc.update(sql,
                m.getBoxoRank(),
                m.getMovieNm(),
                m.getRfilmDe(),
                m.getMovieStatNm(),
                m.getAdmisClassNm(),
                m.getImgPathNm(),
                m.getMovieSynopCn(),
                m.getPlayTime()
            );
        }
    }

    // 조회 (ORDER BY에도 백틱 추가)
    public List<Movie> findAll() {
        return jdbc.query("SELECT * FROM movie ORDER BY `boxoRank` ASC",
            (rs, i) -> {
                Movie m = new Movie();
                m.setBoxoRank(rs.getInt("boxoRank"));
                m.setMovieNm(rs.getString("movieNm"));
                m.setRfilmDe(rs.getString("rfilmDe"));
                m.setMovieStatNm(rs.getString("movieStatNm"));
                m.setAdmisClassNm(rs.getString("admisClassNm"));
                m.setImgPathNm(rs.getString("imgPathNm"));
                m.setMovieSynopCn(rs.getString("movieSynopCn"));
                m.setPlayTime(rs.getString("playTime"));
                return m;
            });
    }
}