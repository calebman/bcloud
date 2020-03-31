package com.bcloud.server.common.security.token;


import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author JianhuiChen
 * @description 访问令牌解析器
 * @date 2019-08-12
 */
public class HeaderAccessTokenExtractor implements ITokenExtractor {

    /**
     * 读取访问令牌信息
     *
     * @param request 请求对象
     * @return 访问令牌
     */
    public String readToken(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String accessToken = request.getHeader("Access-Token");
        if (!StringUtils.isEmpty(accessToken)) {
            return accessToken;
        }
        String authorization = request.getHeader("Authorization");
        authorization = authorization == null ? null : authorization.toLowerCase();
        final String BEARER_TYPE = "Bearer";
        if (authorization != null && authorization.startsWith(BEARER_TYPE.toLowerCase())) {
            String[] strs = authorization.split(BEARER_TYPE.toLowerCase());
            if (strs.length > 1) {
                return strs[1].trim();
            }
        }
        return null;
    }
}
