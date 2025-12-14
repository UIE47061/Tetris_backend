package tetris.demo.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    @Value("${docs.username}")
    private String docsUsername;

    @Value("${docs.password}")
    private String docsPassword;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // 關閉 CSRF 防護
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // 開啟 CORS
            .authorizeHttpRequests(auth -> auth
                // 保護 Swagger 相關路徑，需要認證
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").authenticated()
                // 其他請求允許通過
                .anyRequest().permitAll()
            )
            .httpBasic(Customizer.withDefaults()) // 啟用 HTTP Basic Auth
            .headers(headers -> headers.frameOptions().disable()); // 允許 H2 Console 顯示

        return http.build();
    }

    // 設定 CORS (讓 Vue 前端可以連過來)
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:5173"); // 本機開發用
        config.addAllowedOrigin("https://uie47061.github.io"); // GitHub Pages
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true); // 允許發送憑證（如 Cookie）
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    // 配置 Swagger 文檔的用戶認證
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails docsUser = User.builder()
            .username(docsUsername)
            .password("{noop}" + docsPassword) // {noop} 表示不加密存儲
            .roles("DOCS")
            .build();
        return new InMemoryUserDetailsManager(docsUser);
    }
}