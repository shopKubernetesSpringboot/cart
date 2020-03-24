package com.dgf.shopcart.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthConfig {

    @Value("${com.dgf.shopcart.users.user.name}")
    private String userName;
    @Value("${com.dgf.shopcart.users.user.pass}")
    private String userPass;
    @Value("${com.dgf.shopcart.users.user.role}")
    private String userRole;

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        UserDetails user = User.withUsername(userName)
                .password(encoder.encode(userPass))
                .roles(userRole)
                .build();
        return new MapReactiveUserDetailsService(user);
    }

}
