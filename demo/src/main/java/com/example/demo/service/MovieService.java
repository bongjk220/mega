package com.example.demo.service;

import com.example.demo.dto.Movie;
import com.example.demo.repository.MovieRepository;
import com.example.demo.util.HtmlEntityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository repo;

    // JSON → DB
    public void loadJson() throws Exception {
        System.out.println("=== MovieService.loadJson() 시작 ===");
        
        repo.createTable(); // 테이블 생성

        ObjectMapper mapper = new ObjectMapper();

        try {
            // ClassPath 리소스 사용 (Render.com 호환)
            Resource resource = new ClassPathResource("movies.json");
            System.out.println("ClassPath 리소스: " + resource.getFilename());
            
            if (!resource.exists()) {
                System.out.println("movies.json 리소스 없음 - 샘플 데이터 생성");
                createSampleData();
                return;
            }
            
            System.out.println("JSON 파일 읽기 시도...");
            InputStream inputStream = resource.getInputStream();
            List<Movie> list = Arrays.asList(
                    mapper.readValue(inputStream, Movie[].class)
            );
            inputStream.close();
            System.out.println("읽은 영화 수: " + list.size());
            
            // HTML 엔티티 정제
            List<Movie> cleanedList = list.stream()
                    .map(this::cleanMovieData)
                    .toList();
            
            repo.deleteAll();
            repo.saveAll(cleanedList);
            System.out.println("데이터베이스 저장 완료: " + cleanedList.size() + "개");
        } catch (Exception e) {
            System.err.println("JSON 처리 실패: " + e.getMessage());
            e.printStackTrace();
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
                .playTime("120")
                .build(),
            Movie.builder()
                .boxoRank(2)
                .movieNm("짱구")
                .rfilmDe("2024.04.20")
                .movieStatNm("상영중")
                .admisClassNm("15세이상관람가")
                .imgPathNm("/SharedImg/2026/04/09/EQubwslxEDUXzW48Les6RlRvflw8eJc0.jpg")
                .movieSynopCn("")
                .playTime("135")
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