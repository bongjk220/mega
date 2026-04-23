package com.example.demo.service;

import com.example.demo.dto.Movie;
import com.example.demo.repository.MovieRepository;
import com.example.demo.util.HtmlEntityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository repo;

    // JSON → DB
    public void loadJson() throws Exception {

        repo.createTable(); // 테이블 생성

        ObjectMapper mapper = new ObjectMapper();

        // 프로젝트 루트 기준
        File file = new File("data/movies.json");
        
        if (!file.exists()) {
            // Create sample data if JSON file doesn't exist
            createSampleData();
            return;
        }

        try {
            List<Movie> list = Arrays.asList(
                    mapper.readValue(file, Movie[].class)
            );
            
            // HTML 엔티티 정제
            List<Movie> cleanedList = list.stream()
                    .map(this::cleanMovieData)
                    .toList();
            
            repo.deleteAll();
            repo.saveAll(cleanedList);
        } catch (Exception e) {
            // If JSON parsing fails, create sample data
            createSampleData();
        }
    }
    
    private void createSampleData() {
        List<Movie> sampleMovies = Arrays.asList(
            Movie.builder()
                .boxoRank(1)
                .movieNm("악마는 프라다를 입는다 2")
                .rfilmDe("2024.04.23")
                .movieStatNm("상영중")
                .admisClassNm("12세이상관람가")
                .imgPathNm("/SharedImg/2026/04/22/Z7tourFOyU0a3xThKHzRJ3gYi24XHaGu.jpg")
                .movieSynopCn("")
                .playTime("120분")
                .build(),
            Movie.builder()
                .boxoRank(2)
                .movieNm("짱구")
                .rfilmDe("2024.04.20")
                .movieStatNm("상영중")
                .admisClassNm("15세이상관람가")
                .imgPathNm("/SharedImg/2026/04/09/EQubwslxEDUXzW48Les6RlRvflw8eJc0.jpg")
                .movieSynopCn("")
                .playTime("135분")
                .build()
        );
        
        repo.deleteAll();
        repo.saveAll(sampleMovies);
    }

    /**
     * 영화 데이터의 HTML 엔티티 정제
     * @param movie 정제할 영화 데이터
     * @return 정제된 영화 데이터
     */
    private Movie cleanMovieData(Movie movie) {
        return Movie.builder()
                .boxoRank(movie.getBoxoRank())
                .movieNm(HtmlEntityUtils.cleanText(movie.getMovieNm()))
                .rfilmDe(HtmlEntityUtils.cleanText(movie.getRfilmDe()))
                .movieStatNm(HtmlEntityUtils.cleanText(movie.getMovieStatNm()))
                .admisClassNm(HtmlEntityUtils.cleanText(movie.getAdmisClassNm()))
                .imgPathNm(HtmlEntityUtils.cleanText(movie.getImgPathNm()))
                .movieSynopCn(HtmlEntityUtils.cleanText(movie.getMovieSynopCn()))
                .playTime(HtmlEntityUtils.cleanText(movie.getPlayTime()))
                .build();
    }

    public List<Movie> getAll() {
        return repo.findAll();
    }
}