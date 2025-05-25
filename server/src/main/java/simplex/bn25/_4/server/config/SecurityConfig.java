package simplex.bn25._4.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.Customizer;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").authenticated()  // API は認証が必要
                        .anyRequest().permitAll()
                )
//                .formLogin(Customizer.withDefaults())  // デフォルトのログイン画面を使用
                .formLogin(form -> form
                        // デフォルトの /login フォームを使い、
                        // ログイン成功時は React アプリへリダイレクト
                        .successHandler(successHandler())    // カスタム成功ハンドラ
                        .permitAll()
                )
                .logout(Customizer.withDefaults());

        return http.build();
    }

    /**
     * ログイン成功時にフロントの React (Vite) サーバへリダイレクトするハンドラ。
     */
    private AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            // フロント開発サーバのルートへ
            response.sendRedirect("http://localhost:5173/");
        };
    }

//    // CORS 許可（オプション）
//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/api/**")
//                        .allowedOrigins("http://localhost:5173")
//                        .allowedMethods("*")
//                        .allowCredentials(true); // cookie を含める場合は必須
//            }
//        };
//    }

    // CORS 設定: React 開発サーバ (5173) からの /api/** 呼び出しを許可
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:5173")
                        .allowedMethods("*")
                        .allowCredentials(true);
                // またログインフォームへのアクセスが CORS ブロックされないように
                registry.addMapping("/login").allowedOrigins("http://localhost:5173").allowCredentials(true);
            }
        };
    }

    // テスト用のインメモリユーザー
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("trainee01")
                .password("{noop}password") // パスワードエンコーディングなし（簡易）
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}
