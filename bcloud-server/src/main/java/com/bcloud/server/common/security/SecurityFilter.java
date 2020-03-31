package com.bcloud.server.common.security;

import com.bcloud.server.common.pojo.BaseBody;
import com.bcloud.server.common.security.cache.IUserCacheStore;
import com.bcloud.server.common.security.token.ITokenExtractor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Stream;

/**
 * @author JianhuiChen
 * @description 安全认证拦截器
 * @date 2020-03-28
 */
@Slf4j
@Getter
@Setter
@RequiredArgsConstructor
public class SecurityFilter implements Filter {

    /**
     * 用户缓存仓库
     */
    private final IUserCacheStore userCacheStore;

    /**
     * 令牌解析器
     */
    private final ITokenExtractor tokenExtractor;

    /**
     * 路径匹配器
     */
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * 白名单
     */
    private String[] whiteList = new String[]{};

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String servletPath = request.getServletPath();
        if (Stream.of(whiteList).anyMatch(pattern -> pathMatcher.match(pattern, servletPath))) {
            chain.doFilter(req, res);
            return;
        }
        String token = tokenExtractor.readToken(request);
        // 如果 params 里有 token 时，则使用 token 查询用户数据进行登陆验证
        if (token != null) {
            // 1. 尝试进行身份认证
            // 2. 如果用户无效，则返回 401
            // 3. 如果用户有效，则保存到 SecurityContext 中，供本次方式后续使用
            SessionUser sessionUser = userCacheStore.getTokenUser(token);
            if (sessionUser == null) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                BaseBody.buildError(401, "无效的令牌或者登录失效").writeAsJson(response);
                return;
            }
        }
        // 调用下一个Filter
        chain.doFilter(req, res);
    }
}
