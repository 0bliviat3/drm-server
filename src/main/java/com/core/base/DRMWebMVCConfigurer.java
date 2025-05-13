package com.core.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class DRMWebMVCConfigurer implements WebMvcConfigurer {

    private final DRMServerInterceptor drmServerInterceptor;

    public DRMWebMVCConfigurer(DRMServerInterceptor drmServerInterceptor) {
        log.debug("load web config");
        this.drmServerInterceptor = drmServerInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(drmServerInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/**", "/js/**", "/css/**");
    }
}
