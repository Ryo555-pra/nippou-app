package simplex.bn25._4.server.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.security.web.authentication.HttpStatusEntryPoint;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.Customizer;
//
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
////    @Bean
////    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
////        http
////                .cors(Customizer.withDefaults())
////                .csrf(csrf -> csrf.disable())
////                .authorizeHttpRequests(auth -> auth
////                        .requestMatchers("/api/**").authenticated()  // API は認証が必要
////                        .anyRequest().permitAll()
////                )
//////                .formLogin(Customizer.withDefaults())  // デフォルトのログイン画面を使用
////                .formLogin(form -> form
////                        // デフォルトの /login フォームを使い、
////                        // ログイン成功時は React アプリへリダイレクト
////                        .successHandler(successHandler())    // カスタム成功ハンドラ
////                        .permitAll()
////                )
////                .logout(Customizer.withDefaults());
////
////        return http.build();
////    }
//@Bean
//public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//    http
//            .csrf(csrf -> csrf.disable())
//            .authorizeHttpRequests(auth -> auth
//                    .requestMatchers("/api/**").authenticated()
//                    .anyRequest().permitAll()
//            )
//            // 未認証の /api/** には 401 を返す
//            .exceptionHandling(ex -> ex
//                    .defaultAuthenticationEntryPointFor(
//                            new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
//                            new AntPathRequestMatcher("/api/**")
//                    )
//            )
//            .formLogin(form -> form
//                    .loginPage("/login")
//                    .defaultSuccessUrl("http://localhost:5173/calendar")
//                    .permitAll()
//            )
//            .logout(Customizer.withDefaults());
//    return http.build();
//}
//
//    /**
//     * ログイン成功時にフロントの React (Vite) サーバへリダイレクトするハンドラ。
//     */
//    private AuthenticationSuccessHandler successHandler() {
//        return (request, response, authentication) -> {
//            // フロント開発サーバのルートへ
//            response.sendRedirect("http://localhost:5173/");
//        };
//    }
//
////    // CORS 許可（オプション）
////    @Bean
////    public WebMvcConfigurer corsConfigurer() {
////        return new WebMvcConfigurer() {
////            @Override
////            public void addCorsMappings(CorsRegistry registry) {
////                registry.addMapping("/api/**")
////                        .allowedOrigins("http://localhost:5173")
////                        .allowedMethods("*")
////                        .allowCredentials(true); // cookie を含める場合は必須
////            }
////        };
////    }
//
//    // CORS 設定: React 開発サーバ (5173) からの /api/** 呼び出しを許可
//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/api/**")
//                        .allowedOrigins("http://localhost:5173")
//                        .allowedMethods("*")
//                        .allowCredentials(true);
//                // またログインフォームへのアクセスが CORS ブロックされないように
//                registry.addMapping("/login").allowedOrigins("http://localhost:5173").allowCredentials(true);
//            }
//        };
//    }
//
//    // テスト用のインメモリユーザー
//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails user = User.builder()
//                .username("trainee01")
//                .password("{noop}password") // パスワードエンコーディングなし（簡易）
//                .roles("USER")
//                .build();
//        return new InMemoryUserDetailsManager(user);
//    }
//}



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * メインのセキュリティ設定
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                // 認証失敗は 401 に
                .exceptionHandling(ex -> ex
                        .defaultAuthenticationEntryPointFor(
                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                                new AntPathRequestMatcher("/api/**")
                        )
                )
                // フォームログインの代わりに、POST /api/auth/login を認証処理に使う
                .formLogin(form -> form
                        .loginProcessingUrl("/api/auth/login")
                        .successHandler((req, res, auth) -> res.setStatus(HttpStatus.OK.value()))
                        .failureHandler((req, res, ex) -> res.sendError(HttpStatus.UNAUTHORIZED.value()))
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler((req, res, auth) -> res.setStatus(HttpStatus.OK.value()))
                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    /**
     * CORS 許可設定
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // React 開発サーバーからの API 呼び出しを許可
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:5173")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowCredentials(true);
                // フォームログインページも許可
                registry.addMapping("/login").allowedOrigins("http://localhost:5173").allowCredentials(true);
            }
        };
    }

    /**
     * テスト用 In-Memory ユーザーの定義
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("trainee01")
                .password("{noop}password")  // {noop} = プレーンテキスト
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}

