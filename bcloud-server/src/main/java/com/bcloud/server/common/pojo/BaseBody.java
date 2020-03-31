package com.bcloud.server.common.pojo;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * @author zr
 * @description 基础返回类
 * @date 21/12/2017
 */
@Slf4j
@Getter
@Setter
public class BaseBody<T> implements Serializable {

    /**
     * 序列化
     */
    private static final long serialVersionUID = 1L;

    /**
     * 错误编码
     */
    private int code;

    /**
     * 错误信息
     */
    private String msg;

    /**
     * 数据
     */
    private T data;

    private BaseBody() {

    }

    private BaseBody(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private BaseBody(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    /**
     * 将自身当成json串写出去
     *
     * @param response 响应
     */
    public void writeAsJson(HttpServletResponse response) throws IOException {
        String jsonStr = this.toString();

        response.setHeader("Pragma", "No-app");
        response.setHeader("Cache-Control", "no-app");
        response.setHeader("Content-Type", "application/json; charset=UTF-8");
        //允许ajax跨域的参数设置
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.getWriter().write(jsonStr);
        response.flushBuffer();
    }

    /**
     * 将自身当成TextHtml写出去
     *
     * @param response 响应
     */
    public void writeAsTextHtml(HttpServletResponse response) throws IOException {
        String htmlText = this.toString();

        response.setHeader("Pragma", "No-app");
        response.setHeader("Cache-Control", "no-app");
        response.setDateHeader("Expires", 0);
        response.setHeader("Content-Type", "text/html; charset=UTF-8");
        //允许ajax跨域的参数设置
        response.getWriter().write(htmlText);
        response.flushBuffer();
    }

    /**
     * 写出成功响应
     *
     * @return 响应体
     */
    public static BaseBody buildSuccess() {
        return new BaseBody();
    }

    /**
     * 写出成功响应
     *
     * @param data 数据
     * @return 响应体
     */
    public static <T> BaseBody<T> buildSuccess(T data) {
        BaseBody<T> baseResponse = new BaseBody<>();
        baseResponse.setData(data);
        return baseResponse;
    }

    /**
     * 写出失败响应
     *
     * @param code 错误代码
     * @param msg  错误消息
     * @return 响应体
     */
    public static BaseBody buildError(int code, String msg) {
        return new BaseBody(code, msg);
    }

    /**
     * 写出失败响应
     *
     * @param code 错误代码
     * @param msg  错误消息
     * @param data 错误详细数据
     * @return 响应体
     */
    public static <T> BaseBody<T> buildError(int code, String msg, T data) {
        return new BaseBody(code, msg, data);
    }
}
