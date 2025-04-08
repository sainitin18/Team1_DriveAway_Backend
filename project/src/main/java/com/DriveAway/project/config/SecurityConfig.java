package com.DriveAway.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests((requests) -> requests
                .anyRequest().permitAll()
            )
            .formLogin().disable()
            .httpBasic().disable();
        return http.build();
//    	http
//        .csrf().disable()
//        .authorizeHttpRequests((requests) -> requests
//            .requestMatchers(
//                "/swagger-ui/**",
//                "/swagger-ui.html",
//                "/v3/api-docs/**",
//                "/auth/**",
//                "/public/**"
//            ).permitAll()
//            .requestMatchers(HttpMethod.GET, "/users").hasAuthority("ADMIN")
//            .anyRequest().authenticated()
//        )
//        .formLogin().disable()
//        .httpBasic().disable()
//        .sessionManagement(session -> session
//            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)  // ✅ allow session usage
//        );
//    return http.build();
    }
}
