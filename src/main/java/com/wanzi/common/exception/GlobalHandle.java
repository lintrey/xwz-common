package com.wanzi.common.exception;

import com.wanzi.common.entity.Result;
import com.wanzi.common.entity.ResultEnum;
import com.wanzi.common.entity.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.ObjectError;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @program: common
 * @description: 异常处理：
 * 全局未知异常、自定义异常、参数校验异常。。。
 * 统一范围方式
 * 统一约定错误码、错误描述
 * @author: zhangchuntao
 * @create: 2018-08-02
 **/
@ControllerAdvice
@Slf4j
public class GlobalHandle {
    public static final Integer PARAM_ERROR = 303;


    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result exceptionHanle(Exception e){
        if (e instanceof XwzServiceException){
            XwzServiceException exception = (XwzServiceException) e;
            return ResultUtil.error(exception.getCode(),exception.getMessage());
        }else {
            log.error("系统异常",e);
            return ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMsg());
        }
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public Result validateHandle(MethodArgumentNotValidException e){
        log.error("【参数校验异常】");
        Result result = new Result();
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        Iterator<ObjectError> iterator = allErrors.iterator();
        //获取所有违反约束信息，并按“，”分隔
        StringBuffer msg = new StringBuffer();
        while (iterator.hasNext()) {
            ObjectError cvl = iterator.next();
            msg.append(cvl.getDefaultMessage()).append(",");
        }
        String resMsg = msg.substring(1,msg.length() - 1);

        result.setCode(PARAM_ERROR);
        result.setMsg(resMsg);
        //log.error(ExceptionUtils.getStackTrace(e));
        return result;
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    public Result  validateHandle(ConstraintViolationException e) {
        log.error("【参数校验异常】");
        Result result = new Result();

        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        Iterator<ConstraintViolation<?>> iterator = constraintViolations.iterator();
        //获取所有违反约束信息，并按“，”分隔
        StringBuffer msg = new StringBuffer();
        while (iterator.hasNext()) {
            ConstraintViolation<?> cvl = iterator.next();
            msg.append(cvl.getMessageTemplate()+",");
        }
        String resMsg = msg.substring(1,msg.length() - 1);
        int errorCode = PARAM_ERROR;
        result.setCode(errorCode);
        result.setMsg(resMsg);
        //log.error(ExceptionUtils.getStackTrace(e));
        return result;
    }

    @Bean
    public
    MethodValidationPostProcessor methodValidationPostProcessor() {
        return new  MethodValidationPostProcessor();

    }
}
