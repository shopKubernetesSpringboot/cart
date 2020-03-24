package com.dgf.shopcart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.ReactiveMapSessionRepository;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class SessionConfig {

    @Bean
    public ReactiveMapSessionRepository reactiveSessionRepository() {
        return new ReactiveMapSessionRepository(new ConcurrentHashMap<>());
    }
}
