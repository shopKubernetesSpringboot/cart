package com.dgf.shopcart;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.session.ReactiveMapSessionRepository;
import org.springframework.session.config.annotation.web.server.EnableSpringWebSession;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.config.EnableWebFlux;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
@SpringBootApplication
@EnableSpringWebSession
@EnableWebFlux
@EnableWebFluxSecurity
@Slf4j
public class App {

    @Value("${com.dgf.shopCart.cors.origins}")
    private String corsOrigins;

    public static void main(String[] args) {
        SpringApplication.run(App.class);
    }

    @Bean
    public ReactiveMapSessionRepository reactiveSessionRepository() {
        return new ReactiveMapSessionRepository(new ConcurrentHashMap<>());
    }

    @Bean
    @Primary
    public Validator springValidator() {
        return new LocalValidatorFactoryBean();
    }


    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf().disable()
                //https://docs.spring.io/spring-security/site/docs/current/reference/html5/#webflux-csrf-configure
//                .csrf(csrf -> csrf.csrfTokenRepository(CookieServerCsrfTokenRepository.withHttpOnlyFalse()))
//                .csrf(csrf -> csrf.csrfTokenRepository(new CookieServerCsrfTokenRepository()))
                .cors(c -> c.configurationSource(cors()))
                .authorizeExchange()

//                .pathMatchers(HttpMethod.GET).permitAll()
//                .pathMatchers(HttpMethod.POST).hasRole("ADMIN")
//                .pathMatchers("/posts/**").authenticated()
                .anyExchange().authenticated()
//                .anyExchange().permitAll()
                .and()
                .httpBasic()
                .and()
                .build();
    }

//    private static final String ALLOWED_HEADERS = "x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN, mode";
//    private static final String ALLOWED_METHODS = "GET, PUT, POST, DELETE, OPTIONS";
//    private static final String ALLOWED_ORIGIN = "*";
//    private static final String MAX_AGE = "3600";
//
//    @Bean
//    public WebFilter corsFilter() {
//        log.warn("from CorsConfiguration!!!");
//        return (ServerWebExchange ctx, WebFilterChain chain) -> {
//            ServerHttpRequest request = ctx.getRequest();
//            log.warn("after ServerHttpRequest");
//            if (CorsUtils.isCorsRequest(request)) {
//                log.warn("inside isCorsRequest");
//                ServerHttpResponse response = ctx.getResponse();
//                HttpHeaders headers = response.getHeaders();
//                headers.add("Access-Control-Allow-Origin", ALLOWED_ORIGIN);
//                headers.add("Access-Control-Allow-Methods", ALLOWED_METHODS);
//                headers.add("Access-Control-Max-Age", MAX_AGE);
//                headers.add("Access-Control-Allow-Headers", ALLOWED_HEADERS);
//                if (request.getMethod() == HttpMethod.OPTIONS) {
//                    response.setStatusCode(HttpStatus.OK);
//                    return Mono.empty();
//                }
//            }
//            return chain.filter(ctx);
//        };
//    }


    private UrlBasedCorsConfigurationSource cors() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        log.info("CORS allowed origins: {}", corsOrigins);
        config.addAllowedOrigin(corsOrigins);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

//    @Bean
//    CorsWebFilter corsFilter() {
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
//        config.addAllowedOrigin("http://localhost:3000");
//        config.addAllowedHeader("*");
//        config.addAllowedMethod("*");
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//
//        return new CorsWebFilter(source);
//    }

//    @Bean
//    CorsWebFilter corsWebFilter() {
//        log.warn("corsWebFilter!!");
//        CorsConfiguration corsConfig = new CorsConfiguration();
//        corsConfig.applyPermitDefaultValues();
////        corsConfig.setAllowedOrigins(Collections.singletonList("*"));
////        corsConfig.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
////        corsConfig.setAllowCredentials(true);
////        corsConfig.setMaxAge(8000L);
////        corsConfig.addAllowedMethod(POST);
////        corsConfig.addAllowedMethod(GET);
////        corsConfig.addAllowedMethod(OPTIONS);
////        corsConfig.addAllowedHeader("React-front-end");
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", corsConfig);
//
//        return new CorsWebFilter(source);
//    }

//    @Bean
//    CorsWebFilter corsFilter() {
//        return new CorsWebFilter(exchange -> {
//            CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
//            corsConfiguration.addAllowedOrigin("http://localhost:3000");
//            return corsConfiguration;
//        });
//    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String encPass=encoder.encode("user");
        log.info("password="+encPass);
        UserDetails user = User.withUsername("user")
                .password(encPass)
                .roles("USER")
                .build();
        return new MapReactiveUserDetailsService(user);
    }
}
