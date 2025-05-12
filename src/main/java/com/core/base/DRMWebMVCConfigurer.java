package com.core.base;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class DRMWebMVCConfigurer implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new DRMServerInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/api/**", "/js/**", "/css/**");
    }
}
