package com.bcloud.server.common.security;

import com.bcloud.server.common.security.cache.IUserCacheStore;
import com.bcloud.server.common.security.token.ITokenExtractor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * @author JianhuiChen
 * @description 控制层的用户参数注入器
 * @date 2019-08-12
 */

@Slf4j
@RequiredArgsConstructor
public class SessionUserArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * 实体工厂
     */
    private final IUserCacheStore userCacheStore;

    /**
     * 令牌解析器
     */
    private final ITokenExtractor tokenExtractor;


    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().isAssignableFrom(SessionUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) {
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        String accessToken = tokenExtractor.readToken(request);
        log.debug("Resolve argment SecurityUser, use token {}", accessToken);
        return userCacheStore.getTokenUser(accessToken);
    }
}
