package com.wanzi.common.exception;

import lombok.Data;

/**
 * @program: common
 * @description:
 * 创建自定义异常类原因：java自己的异常类只有message参数，
 * 这里我们还需要一个code参数（用来记录请求状态）。
 * 并且创建自定义异常还能和系统异常区分开来。
 * @author: zhangchuntao
 * @create: 2018-08-02
 **/
@Data
public class XwzServiceException extends RuntimeException {

    private int code;

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public XwzServiceException(int code, String message) {
        super(message);
        this.code = code;
    }

}
