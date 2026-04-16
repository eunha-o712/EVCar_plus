package com.evcar.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LoginCheckInterceptor loginCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheckInterceptor)
                .addPathPatterns(
                        "/company/**",
                        "/vehicle/**",
                        "/charging/**",
                        "/faq/**",
                        "/consultation/**",
                        "/inquiry/**",
                        "/mypage/**",
                        "/wishlist/**",
                        "/admin/**"
                )
                .excludePathPatterns(
                        "/",
                        "/main",
                        "/main/**",
                        "/login",
                        "/login/**",
                        "/signup",
                        "/signup/**",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/images/upload/**",
                        "/favicon.ico",
                        "/error",
                        "/error/**"
                );
    }
}