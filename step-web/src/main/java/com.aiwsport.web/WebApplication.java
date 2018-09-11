package com.aiwsport.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@ComponentScan(basePackages = {"com.aiwsport.web", "com.aiwsport.core"})
@MapperScan(basePackages = {"com.aiwsport.core.mapper"})
@EnableTransactionManagement
public class WebApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

//    @Configuration
//    static class WebMvc implements WebMvcConfigurer {
//        //增加拦截器
//        @Override
//        public void addInterceptors(InterceptorRegistry registry) {
//            registry.addInterceptor(new AccessHandlerInteceptor())    //指定拦截器类
//                    .addPathPatterns("/api/**");        //指定该类拦截的url
//        }
//    }

}