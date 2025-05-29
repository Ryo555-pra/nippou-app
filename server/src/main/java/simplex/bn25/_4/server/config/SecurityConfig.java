package simplex.bn25._4.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 追加：Spring Security 側で使う CORS 設定源を定義
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(List.of("http://localhost:5173"));
        cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        cfg.setAllowedHeaders(List.of("*"));
        cfg.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 全パスに対してこの設定を適用
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

    /**
     * メインのセキュリティ設定。
     * ここで cors(Customizer.withDefaults()) を呼ぶと
     * 上記の CorsConfigurationSource が使われます。
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())    // ← これを使うなら CorsConfigurationSource が必須
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                // API は認証失敗で 401
                .exceptionHandling(ex -> ex
                        .defaultAuthenticationEntryPointFor(
                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                                new AntPathRequestMatcher("/api/**")
                        )
                )
                // フォーム・ログインを /api/auth/login で受ける
                .formLogin(form -> form
                        .loginProcessingUrl("/api/auth/login")
                        .successHandler((req,res,auth) -> res.setStatus(HttpStatus.OK.value()))
                        .failureHandler((req,res,ex) -> res.sendError(HttpStatus.UNAUTHORIZED.value()))
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler((req,res,auth) -> res.setStatus(HttpStatus.OK.value()))
                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    /**
     * MVC 側でも念のため CORS 許可（Spring Security とは別レイヤー）
     */
    @Bean
    public WebMvcConfigurer mvcCorsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:5173")
                        .allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
                        .allowCredentials(true);

                // 必要であればログイン用エンドポイントも許可
                registry.addMapping("/api/auth/**")
                        .allowedOrigins("http://localhost:5173")
                        .allowCredentials(true);
            }
        };
    }

    /**
     * DB(usersテーブル)からユーザー認証
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            // account列がログインID
            var user = jdbcTemplate.queryForObject(
                "SELECT account, password, role FROM users WHERE account = ?",
                (rs, rowNum) -> User.builder()
                    .username(rs.getString("account"))
                    .password(rs.getString("password"))
                    .roles(rs.getString("role"))
                    .build(),
                username
            );
            if (user == null) throw new UsernameNotFoundException("User not found");
            return user;
        };
    }

    /**
     * パスワードエンコーダー（平文の場合はNoOp、bcryptならBCryptPasswordEncoder）
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance(); // パスワードが平文の場合。bcryptならBCryptPasswordEncoderを使う
    }
}
