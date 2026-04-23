package com.example.demo.repository;

import com.example.demo.dto.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MovieRepository {

    private static final String TABLE_NAME = "movies";

    private final JdbcTemplate jdbc;

    public List<Movie> findAll() {
        return jdbc.query("SELECT * FROM " + TABLE_NAME + " ORDER BY `boxoRank` ASC",
                (rs, i) -> {
                    Movie movie = new Movie();
                    movie.setBoxoRank(rs.getInt("boxoRank"));
                    movie.setMovieNm(rs.getString("movieNm"));
                    movie.setRfilmDe(rs.getString("rfilmDe"));
                    movie.setMovieStatNm(rs.getString("movieStatNm"));
                    movie.setAdmisClassNm(rs.getString("admisClassNm"));
                    movie.setImgPathNm(rs.getString("imgPathNm"));
                    movie.setMovieSynopCn(rs.getString("movieSynopCn"));
                    movie.setPlayTime(rs.getString("playTime"));
                    return movie;
                });
    }
}
