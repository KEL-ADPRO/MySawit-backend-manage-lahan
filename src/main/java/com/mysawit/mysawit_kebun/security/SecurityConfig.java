package com.mysawit.mysawit_kebun.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JWTFilter jwtFilter;
    private final static String ADMIN = "ADMIN";
    private final static String DEFAULTURL = "/api/kebun/**";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/error").permitAll()
                    .requestMatchers(HttpMethod.GET, DEFAULTURL).permitAll()

                    .requestMatchers(HttpMethod.PATCH, DEFAULTURL).hasAuthority(ADMIN)
                    .requestMatchers(HttpMethod.DELETE, DEFAULTURL).hasAuthority(ADMIN)
                    .requestMatchers(HttpMethod.POST, DEFAULTURL).hasAuthority(ADMIN)
                    .requestMatchers(HttpMethod.PUT, DEFAULTURL).hasAuthority(ADMIN)

                    .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
