package com.wanzi.common.service;

import java.util.concurrent.Future;

/**
 * @program: common
 * @description: ${description}
 * @author: zhangchuntao
 * @create: 2018-08-23
 **/
public interface IAsyncService {

    /**
     * 异步处理1
     */
    void dealAsync();

    /**
     * 带返回值异步处理
     * @return
     */
    Future<String> getFirstNameAsync();
}
