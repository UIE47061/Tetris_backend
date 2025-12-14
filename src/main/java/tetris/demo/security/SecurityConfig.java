package tetris.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // 關閉 CSRF 防護
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // 開啟 CORS
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // 暫時允許所有請求，方便測試
            )
            .headers(headers -> headers.frameOptions().disable()); // 允許 H2 Console 顯示

        return http.build();
    }

    // 設定 CORS (讓 Vue 前端可以連過來)
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:5173"); // 這是 Vue 的預設 Port
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}