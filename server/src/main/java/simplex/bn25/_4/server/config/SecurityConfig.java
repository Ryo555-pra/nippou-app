package simplex.bn25._4.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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
                .formLogin(Customizer.withDefaults())  // デフォルトのログイン画面を使用
                .logout(Customizer.withDefaults());

        return http.build();
    }

    // CORS 許可（オプション）
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:5173")
                        .allowedMethods("*")
                        .allowCredentials(true); // cookie を含める場合は必須
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
