package com.dgf.shopCart;

import com.dgf.shopCart.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@Slf4j
@EnableWebFlux
public class App /*implements ApplicationContextAware*/ {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

//    @Override
//    public void setApplicationContext(ApplicationContext ctx) {
//        ctx.getBean(CartService.class).initData();
//    }

}
