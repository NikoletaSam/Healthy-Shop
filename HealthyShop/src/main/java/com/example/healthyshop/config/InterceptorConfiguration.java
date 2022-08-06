package com.example.healthyshop.config;

import com.example.healthyshop.interceptors.IpAddressLoggerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new IpAddressLoggerInterceptor()).addPathPatterns("/users/login");
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
