package com.bcloud.server.common.conf;

import com.bcloud.server.common.security.SecurityFilter;
import com.bcloud.server.common.security.SessionUserArgumentResolver;
import com.bcloud.server.common.security.cache.IUserCacheStore;
import com.bcloud.server.common.security.cache.MemmoryUserCacheStore;
import com.bcloud.server.common.security.encoder.BCryptPasswordEncoder;
import com.bcloud.server.common.security.encoder.PasswordEncoder;
import com.bcloud.server.common.security.token.HeaderAccessTokenExtractor;
import com.bcloud.server.common.security.token.ITokenExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author JianhuiChen
 * @description web 安全配置
 * @date 2019/5/24
 */
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfiguration implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new SessionUserArgumentResolver(userCacheStore(), tokenExtractor()));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/public/**").addResourceLocations("classpath:/front/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }

    /**
     * 白名单页面
     */
    private static final String[] AUTH_WHITELIST = {
            // -- system
            "/",
            "/ws/**",
            "/api/service/file/download/**",
            "/service/file/download/**",
            "/api/system/**",
            "/system/**",
            "/public/**",
            "/front/**"
    };

    @Bean
    public FilterRegistrationBean apiFilterRegisteration() {
        SecurityFilter securityFilter = new SecurityFilter(userCacheStore(), tokenExtractor());
        securityFilter.setWhiteList(AUTH_WHITELIST);
        FilterRegistrationBean<SecurityFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(securityFilter);
        registration.addUrlPatterns("/*");
        registration.setName("securityFilter");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }

    /**
     * 定义基于内存的用户缓存策略
     *
     * @return 用户缓存策略
     */
    @Bean
    public IUserCacheStore userCacheStore() {
        return new MemmoryUserCacheStore();
    }

    /**
     * 定义基于header部分的令牌解析器
     *
     * @return 令牌解析器
     */
    @Bean
    public ITokenExtractor tokenExtractor() {
        return new HeaderAccessTokenExtractor();
    }

    /**
     * 定义基于bcrypt算法的密码加密策略
     *
     * @return 密码加密策略
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
