package com.example.MutsaMarket.config;

import com.example.MutsaMarket.jwt.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;

    public SecurityConfig(JwtTokenFilter jwtTokenFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authHttp -> authHttp
                                .requestMatchers("/no-auth", "/token/issue").permitAll() //누구나
                                .requestMatchers(HttpMethod.GET, "/items").permitAll()
                                .requestMatchers(HttpMethod.GET, "/items/**").permitAll()
                                .requestMatchers("/re-auth", "/users/my-profile").authenticated() //인증
                                .requestMatchers(HttpMethod.POST, "/items").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/items/**").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "items/**").authenticated()
                                .requestMatchers("/users/login", "/users/signup").anonymous() //비인증
                )
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtTokenFilter, AuthorizationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}