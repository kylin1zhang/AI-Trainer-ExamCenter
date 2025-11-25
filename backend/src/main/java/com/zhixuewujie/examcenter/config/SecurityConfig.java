package com.zhixuewujie.examcenter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 配置
 * 
 * 注意：当前简化配置，允许所有请求通过
 * JWT 验证在 Controller 层通过拦截器或手动验证实现
 *
 * @author 智学无界
 * @since 2025-11-24
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/doc.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().permitAll() // 临时允许所有请求，实际应该需要认证
            );
        
        return http.build();
    }

}

