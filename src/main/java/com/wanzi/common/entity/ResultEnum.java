package com.wanzi.common.entity;


/**
 * @program: common
 * @description: ${description}
 * @author: zhangchuntao
 * @create: 2018-08-02
 **/
public enum ResultEnum {
    /**
     *
     */
    SUCCESS(200,"请求成功"),

    /**
     * 系统错误
     */
    SYSTEM_ERROR(0,"服务繁忙，请稍后重试!");

    private int code;

    private String msg;

    ResultEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
