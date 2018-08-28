package com.wanzi.common.service.impl;

import com.wanzi.common.exception.XwzServiceException;
import com.wanzi.common.service.IAsyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

/**
 * @program: common
 * @description: ${description}
 * @author: zhangchuntao
 * @create: 2018-08-23
 **/
@Service
@Slf4j
public class AsyncServiceImpl implements IAsyncService {
    /**
     * 异步处理1
     */
    @Override
    @Async
    public void dealAsync() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.error("【异步处理异常1】");
        throw new XwzServiceException(1,"异步处理异常1");
    }

    /**
     * 带返回值异步处理
     *
     * @return
     */
    @Override
    @Async
    public Future<String> getFirstNameAsync() {

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new AsyncResult<String>("张") ;
    }
}
