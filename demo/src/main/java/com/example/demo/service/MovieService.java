package com.example.demo.service;

import com.example.demo.dto.Movie;
import com.example.demo.repository.MovieRepository;
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
            repo.deleteAll();
            repo.saveAll(list);
        } catch (Exception e) {
            // If JSON parsing fails, create sample data
            createSampleData();
        }
    }
    
    private void createSampleData() {
        List<Movie> sampleMovies = Arrays.asList(
            Movie.builder()
                .boxoRank(1)
                .movieNm("?? ??? ???")
                .rfilmDe("2024.04.23")
                .movieStatNm("??")
                .admisClassNm("12?")
                .imgPathNm("")
                .movieSynopCn("?? ???")
                .playTime("120?")
                .build(),
            Movie.builder()
                .boxoRank(2)
                .movieNm("?????")
                .rfilmDe("2024.04.20")
                .movieStatNm("??")
                .admisClassNm("15?")
                .imgPathNm("")
                .movieSynopCn("??? ????")
                .playTime("135?")
                .build()
        );
        
        repo.deleteAll();
        repo.saveAll(sampleMovies);
    }

    public List<Movie> getAll() {
        return repo.findAll();
    }
}