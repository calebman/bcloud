package com.bcloud.server.common.security.token;

import javax.servlet.http.HttpServletRequest;

/**
 * @author JianhuiChen
 * @description 访问令牌解析器
 * @date 2019-08-12
 */
public interface ITokenExtractor {

    /**
     * 解析访问令牌信息
     *
     * @param request 请求对象
     * @return 访问令牌
     */
    String readToken(HttpServletRequest request);
}
