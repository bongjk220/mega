package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Lombok?? getter/setter, builder ??? ??
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    private int boxoRank;
    private String movieNm;
    private String rfilmDe;
    private String movieStatNm;
    private String admisClassNm;
    private String imgPathNm;
    private String movieSynopCn;
    private String playTime;
}