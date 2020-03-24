package com.dgf.shopcart.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    @Value("${com.dgf.shopCart.cors.origins}")
    private String corsOrigins;

    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf().csrfTokenRepository(CookieServerCsrfTokenRepository.withHttpOnlyFalse()).and()
                .cors(c -> c.configurationSource(cors()))
                .authorizeExchange()
//                .pathMatchers(HttpMethod.GET).permitAll()
//                .pathMatchers(HttpMethod.POST).hasRole("ADMIN")
//                .pathMatchers("/posts/**").authenticated()
                .anyExchange().authenticated()
                .and()
                .httpBasic()
                .and()
                .build();
    }

    private UrlBasedCorsConfigurationSource cors() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin(corsOrigins);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
