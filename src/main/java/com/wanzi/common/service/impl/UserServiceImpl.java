package com.wanzi.common.service.impl;

import com.wanzi.common.entity.Result;
import com.wanzi.common.entity.ResultUtil;
import com.wanzi.common.service.IAsyncService;
import com.wanzi.common.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @program: common
 * @description: ${description}
 * @author: zhangchuntao
 * @create: 2018-08-22
 **/
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IAsyncService asyncService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> getName() {
        log.info("【getName】【入参】");

        Future<String> future = asyncService.getFirstNameAsync();
        while (true){
            if (future.isDone()){
                try {
                    String firstName = future.get();
                    log.info("【异步getName】【返回】firstName:{}",firstName);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                break;
            }

        }
//        log.info("【getName】【结束】");
       return ResultUtil.success("不可能回滚");
    }

    @Async
    public void deal(){

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /*log.error("【异步处理异常】");
        throw new XwzServiceException(1,"异步处理异常");*/
    }

    public static void main(String[] args) {
        /*UserServiceImpl userService = new UserServiceImpl();
        userService.deal();*/

        System.out.println();
    }
}
