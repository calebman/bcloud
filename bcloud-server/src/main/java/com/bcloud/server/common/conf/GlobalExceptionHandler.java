package com.bcloud.server.common.conf;

import com.bcloud.server.common.pojo.BaseBody;
import com.bcloud.server.common.security.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zr
 * @description 全局异常响应
 * @date 25/12/2017
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    /**
     * 日志
     */
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 拦截权限不足的异常
     *
     * @param request 请求
     * @param e       异常
     * @return 响应
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AccessDeniedException.class)
    public BaseBody handleRuntimeException(HttpServletRequest request, AccessDeniedException e) {
        e.printStackTrace();
        logger.error("请求：{}, params:{}, 无权访问：{}", request.getRequestURI(), request.getParameterMap(), e);
        return BaseBody.buildError(401, e.getMessage());
    }

    /**
     * 拦截绑定异常
     *
     * @param request 请求
     * @param e       异常
     * @return 响应
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public BaseBody handleRequestBindException(HttpServletRequest request, Exception e) {
        Map<String, String> errorMap = new HashMap<>();
        if (e instanceof BindException || e instanceof MethodArgumentNotValidException) {
            BindingResult bindingResult;
            if (e instanceof BindException) {
                bindingResult = ((BindException) e).getBindingResult();
            } else {
                bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
            }
            errorMap = bindingResult.getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (v1, v2) -> v1));
        } else if (e instanceof ConstraintViolationException) {
            Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) e).getConstraintViolations();
            errorMap = constraintViolations.stream()
                    .collect(Collectors.toMap(o -> o.getPropertyPath().toString(), ConstraintViolation::getMessage, (v1, v2) -> v1));
        }
        logger.error("请求：{}, params:{}, 参数校验错误：{}", request.getRequestURI(), request.getParameterMap(), errorMap);
        return BaseBody.buildError(400, "参数校验错误", errorMap);
    }

    /**
     * 拦截无效请求异常
     *
     * @param request 请求
     * @param e       异常
     * @return 响应
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public BaseBody handleNotFoundException(HttpServletRequest request, NoHandlerFoundException e) {
        logger.error("请求：{}, params:{}, 无效请求错误：{}", request.getRequestURI(), request.getParameterMap(), e);
        return BaseBody.buildError(404, "该地址是一个无效请求");
    }

    /**
     * 拦截无效请求异常
     *
     * @param request 请求
     * @param e       异常
     * @return 响应
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public BaseBody handleUnsupportedMethodException(HttpServletRequest request, HttpRequestMethodNotSupportedException e) {
        logger.error("请求：{}, params:{}, 无效请求错误：{}", request.getRequestURI(), request.getParameterMap(), e);
        return BaseBody.buildError(405, "请求该地址的方法不对");
    }

    /**
     * 拦截服务器异常
     *
     * @param request 请求
     * @param e       异常
     * @return 响应
     * @throws RuntimeException 抛出异常用于事务回滚
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public BaseBody handleRuntimeException(HttpServletRequest request, RuntimeException e) throws RuntimeException {
        e.printStackTrace();
        logger.error("请求：{}, params:{}, 业务响应错误：{}", request.getRequestURI(), request.getParameterMap(), e);
        return BaseBody.buildError(500, e.getMessage());
    }

    /**
     * 拦截服务器异常
     *
     * @param request 请求
     * @param e       异常
     * @return 响应
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public BaseBody handleException(HttpServletRequest request, Exception e) {
        logger.error("请求：{}, params:{}, 服务器响应错误：{}", request.getRequestURI(), request.getParameterMap(), e);
        return BaseBody.buildError(500, "系统正在维护中，请稍后访问");
    }

}
