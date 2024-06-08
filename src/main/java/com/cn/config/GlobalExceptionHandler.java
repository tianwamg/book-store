package com.cn.config;

import com.cn.response.ResultResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LoginException.class)
    public ResultResponse<String> loginException(Exception e){
        return ResultResponse.error("403",e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResultResponse<String> handleException(Exception e){
        e.printStackTrace();
        return ResultResponse.error("500",e.getMessage());
    }
}
