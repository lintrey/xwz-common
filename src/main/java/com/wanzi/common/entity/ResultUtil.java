package com.wanzi.common.entity;


/**
 * @program: common
 * @description: ${description}
 * @author: zhangchuntao
 * @create: 2018-08-02
 **/
public class ResultUtil {

    public static Result error(int code,String msg){
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static <T> Result<T> success(T data){
        Result result = new Result();
        result.setCode(200);
        result.setData(data);
        return result;
    }

    public static Result success(){
        Result result = new Result();
        result.setCode(200);
        return result;
    }

}
