package com.example.demo.service;

import com.example.demo.dto.Movie;
import com.example.demo.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository repo;

    public List<Movie> getAll() {
        return repo.findAll();
    }
}