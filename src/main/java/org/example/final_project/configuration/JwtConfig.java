package org.example.final_project.configuration;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.configuration.jwt.JwtAuthenticationFilter;
import org.example.final_project.configuration.jwt.JwtProvider;
import org.example.final_project.service.ITokenBlacklistService;
import org.example.final_project.service.impl.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtConfig {
    JwtProvider jwtProvider;
    UserService userService;
    ITokenBlacklistService tokenBlacklistService;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtProvider, userService, tokenBlacklistService);
    }
}
