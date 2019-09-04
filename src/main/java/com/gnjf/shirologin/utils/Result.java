package com.gnjf.shirologin.utils;

import java.io.Serializable;
/*
封装响应结果
*/
public class Result<T> implements Serializable {
    // 返回码
    private Integer code;
    // 响应信息
    private String message;
    // 响应数据
    private T data;
    // 操作是否成功，也可以通过code判断
    private boolean flag;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Result( boolean flag,Integer code, String message) {
        this.code = code;
        this.message = message;
        this.flag = flag;
    }

    public Result(boolean flag,Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", flag=" + flag +
                '}';
    }
}
