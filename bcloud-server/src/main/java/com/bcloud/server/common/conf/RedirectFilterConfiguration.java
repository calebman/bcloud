package com.bcloud.server.common.conf;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.function.Function;

/**
 * @author JianhuiChen
 * @description 用于实现前后端分离的集成部署
 * @date 2019-08-22
 */
@Configuration
public class RedirectFilterConfiguration {

    /**
     * 匹配前端路由
     * 所有 /front/* 请求映射至 index.html
     * exp /front/user/manager -> /index.html
     */
    @Bean
    public FilterRegistrationBean frontFilterRegisteration() {
        FilterRegistrationBean<RedirectFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RedirectFilter("/front/**", http -> "/front/index.html"));
        registration.addUrlPatterns("/front/*");
        registration.setName("frontFilter");
        return registration;
    }

    /**
     * 匹配前端请求前缀
     * 所有 /api/* 请求映射至 /*
     * exp /api/app/users/me -> /app/users/me
     */
    @Bean
    public FilterRegistrationBean apiFilterRegisteration() {
        FilterRegistrationBean<RedirectFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RedirectFilter("/api/**",
                http -> http.getServletPath().replaceFirst("/api", "")));
        registration.addUrlPatterns("/api/*");
        registration.setName("apiFilter");
        return registration;
    }


    /**
     * 用于转发逻辑的过滤器 完成前后端分离部署所需中间件nginx的功能
     */
    @RequiredArgsConstructor
    @Slf4j
    public static class RedirectFilter implements Filter {

        /**
         * 配置url通配符
         */
        private final String urlPattern;

        /**
         * 转发逻辑
         */
        private final Function<HttpServletRequest, String> rewriteHandler;

        /**
         * 路径匹配器
         */
        private AntPathMatcher pathMatcher = new AntPathMatcher();

        @Override
        public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
            HttpServletRequest request = (HttpServletRequest) req;
            String servletPath = request.getServletPath();
            // 匹配的路径重写
            if (pathMatcher.match(urlPattern, servletPath)) {
                String rewritePath = rewriteHandler.apply(request);
                log.debug("Rewrite {} to {}", servletPath, rewritePath);
                req.getRequestDispatcher(rewritePath).forward(req, resp);
            } else {
                chain.doFilter(req, resp);
            }
        }
    }
}
