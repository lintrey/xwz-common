package com.wanzi.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @program: common
 * @description: ${description}
 * @author: zhangchuntao
 * @create: 2018-08-01
 **/
@SpringBootApplication
@EnableAsync
public class CommonApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommonApplication.class,args);
    }
}
