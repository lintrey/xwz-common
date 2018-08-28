package com.wanzi.common.controller;

import com.wanzi.common.dto.UserResDto;
import com.wanzi.common.entity.Result;
import com.wanzi.common.entity.ResultUtil;
import com.wanzi.common.exception.XwzServiceException;
import com.wanzi.common.service.IUserService;
import com.wanzi.common.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Size;

/**
 * @program: common
 * @description: ${description}
 * @author: zhangchuntao
 * @create: 2018-08-01
 **/
@Validated
@Slf4j
@RestController
public class TestController {

    @Value("${request.xwz.myName}")
    private String myName;
    @Autowired
    private IUserService userService;

    @GetMapping(value = "/xwz/getCount")
    public Result getCount( @NotBlank(message = "用户名不能为空")
                                @Size(min = 1, max = 3)
                                @RequestParam(required = false) String name){
        log.info("【小丸子--查询数量】【入参】name:{}",name);


        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        float n = (float) 1.0;
        Float count = null;
       String coun1 = "";

        /*if (count == 23){
            throw new XwzServiceException(1,"格式错误");
        }*/
        UserResDto userResDto = new UserResDto();
        //userResDto.setUserName("kobe");

        return ResultUtil.success(name);
    }

    @PostMapping(value = "${request.xwz.getCount}")
    public Result getCount( @Validated @RequestBody UserResDto userResDto){
        log.info("【小丸子--查询数量】【入参】{}",userResDto);

        Integer count = 23;
        /*if (count == 23){
            throw new XwzServiceException(1,"格式错误");
        }*/

        return ResultUtil.success(count);
    }


    @GetMapping(value = "/xwz/getName")
    public Result getName(){
        log.info("【小丸子--查询数量】");

        return ResultUtil.success(userService.getName());
    }
}
