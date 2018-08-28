package com.wanzi.common.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: common
 * @description: 公共返回类
 * @author: zhangchuntao
 * @create: 2018-08-02
 **/
@Data
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 7297199589511275135L;

    private Integer code;

    private String msg;

    private T data;
}
