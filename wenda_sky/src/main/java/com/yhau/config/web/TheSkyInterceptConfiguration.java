package com.yhau.config.web;

import com.yhau.core.intercept.LoginRequredIntercept;
import com.yhau.core.intercept.PassportIntercept;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;

@Component
public class TheSkyInterceptConfiguration extends WebMvcConfigurerAdapter {
    @Resource
    private PassportIntercept passportIntercept;
    @Resource
    private LoginRequredIntercept loginRequredIntercept;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportIntercept);
        registry.addInterceptor(loginRequredIntercept).addPathPatterns("/user/**");
        super.addInterceptors(registry);
    }
}
