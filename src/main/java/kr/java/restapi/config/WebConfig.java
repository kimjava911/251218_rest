package kr.java.restapi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// #(6)-2
/**
 * 전역 CORS 설정
 * - @Value로 쉼표 구분 문자열을 배열로 변환
 */
@Configuration
@RequiredArgsConstructor // #(6)-5-1
public class WebConfig implements WebMvcConfigurer {

    // import org.springframework.beans.factory.annotation.Value;
//    @Value("${cors.allowed-origins}")
//    private String[] allowedOrigins;  // 쉼표로 구분된 문자열 → 배열 자동 변환

    // #(6)-5-2
    private final CorsProperties corsProperties;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // #(6)-5-3
        registry.addMapping("/api/**")
//                .allowedOrigins(allowedOrigins)
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedOrigins(corsProperties.getAllowedOrigins().toArray(new String[0]))
                .allowedMethods(corsProperties.getAllowedMethods().toArray(new String[0]))                .allowedHeaders("*")
                .allowCredentials(true)
//                .maxAge(3600);
                .maxAge(corsProperties.getMaxAge());
    }
}
