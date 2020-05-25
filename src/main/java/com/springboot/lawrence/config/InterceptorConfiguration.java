package com.springboot.lawrence.config;

import com.springboot.lawrence.interceptor.JwtInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Lawrence
 * @date 2020/4/22
 */
@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

    private static final Logger LOGGER = LoggerFactory.getLogger(InterceptorConfiguration.class);

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 放行登录，拦截其他需要 token 的接口
        LOGGER.info("注册拦截器");
        registry.addInterceptor(jwtInterceptor).addPathPatterns("/**").excludePathPatterns("/**/login");
    }
}
