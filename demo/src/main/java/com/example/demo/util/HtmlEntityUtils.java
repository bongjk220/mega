package com.example.demo.util;

import org.springframework.stereotype.Component;

@Component
public class HtmlEntityUtils {
    
    /**
     * HTML 엔티티를 실제 문자로 변환
     * @param text HTML 엔티티가 포함된 텍스트
     * @return 변환된 텍스트
     */
    public static String decodeHtmlEntities(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        
        return text
                .replace("&#40;", "(")
                .replace("&#41;", ")")
                .replace("&#39;", "'")
                .replace("&#34;", "\"")
                .replace("&#38;", "&")
                .replace("&#60;", "<")
                .replace("&#62;", ">")
                .replace("&#91;", "[")
                .replace("&#93;", "]");
    }
    
    /**
     * 모든 HTML 엔티티를 정제
     * @param text 정제할 텍스트
     * @return 정제된 텍스트
     */
    public static String cleanText(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        
        // HTML 엔티티 디코딩
        String decoded = decodeHtmlEntities(text);
        
        // 추가적인 HTML 태그 제거 (필요시)
        decoded = decoded.replaceAll("<[^>]*>", "");
        
        // 여러 공백을 단일 공백으로
        decoded = decoded.replaceAll("\\s+", " ").trim();
        
        return decoded;
    }
}
