package kr.java.restapi.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

// #(6)-4
/**
 * CORS 설정 프로퍼티
 * - @Configuration을 붙이면 자동 스캔되어 별도 활성화 불필요
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "cors")
public class CorsProperties {

    private List<String> allowedOrigins = new ArrayList<>();
    private List<String> allowedMethods = new ArrayList<>();
    private Long maxAge = 3600L;
}
