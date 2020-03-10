package com.dgf.shopCart;

import org.springframework.context.annotation.Configuration;

@Configuration
//@EnableSwagger2WebFlux
public class SwaggerConfig {

//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis(RequestHandlerSelectors.any())
//                .paths(PathSelectors.any())
//                .build();
//    }

//    @Bean
//    public IntegrationFlow cartAdd() {
//        return IntegrationFlows.from(
//                WebFlux.inboundGateway("/cart/add")
//                        .requestMapping(r -> r.methods(HttpMethod.PUT)
//                                .consumes(APPLICATION_JSON.toString())
//                                .produces(APPLICATION_JSON.toString()))
//        ).<String>handle((p, h) -> p.toUpperCase()).get();
//    }
//
//    @Bean
//    public IntegrationFlow cartList() {
//        return IntegrationFlows.from(
//                WebFlux.inboundGateway("/cart/list")
//                        .requestMapping(r -> r.methods(HttpMethod.GET)
//                                .consumes(ALL)
//                                .produces(APPLICATION_JSON.toString()))
//        ).<String>handle((p, h) -> p.toUpperCase()).get();
//    }

}