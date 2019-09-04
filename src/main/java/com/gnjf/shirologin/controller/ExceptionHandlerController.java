package com.gnjf.shirologin.controller;

import com.gnjf.shirologin.utils.Result;
import com.gnjf.shirologin.utils.StatusCode;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//全局异常处理器
//token登录会异常的
@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(UnauthenticatedException.class)
    public Result handlerShior(){
        return new Result(false, StatusCode.ERROR,"权限不足请联系管理员");
    }
}
