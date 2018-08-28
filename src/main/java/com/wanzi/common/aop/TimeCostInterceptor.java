package com.wanzi.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: common
 * @description: spring 拦截器
 * @author: zhangchuntao
 * @create: 2018-08-02
 **/
@Slf4j
public class TimeCostInterceptor extends HandlerInterceptorAdapter {
    /**
     * NamedThreadLocal 继承 ThreadLocal 它是线程绑定的变量，提供线程局部变量
     * NamedThreadLocal：Spring提供的一个命名的ThreadLocal实现
     *
      */
    private NamedThreadLocal<Long> localStartReqTime = new NamedThreadLocal<Long>("TimeCost-startReqTime");

    /**
     *预处理回调方法，实现处理器的预处理（如登录检查），第三个参数为响应的处理器（如我们上一章的Controller实现）；
     *      返回值：true表示继续流程（如调用下一个拦截器或处理器）；
     *              false表示流程中断（如登录检查失败），不会继续调用其他的拦截器或处理器，此时我们需要通过response来产生响应；
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("【请求时间拦截器】");
        Long beginTime = System.currentTimeMillis();
        //
        localStartReqTime.set(beginTime);
        return true;
    }

    /**
     *
     * 后处理回调方法，实现处理器的后处理（但在渲染视图之前），此时我们可以通过
     * modelAndView（模型和视图对象）对模型数据进行处理或对视图进行处理，modelAndView也可能为null。
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * afterCompletion: 整个请求处理完毕回调方法，即在视图渲染完毕时回调，如性能监控中我们
     * 可以在此记录结束时间并输出消耗时间，还可以进行一些资源清理
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Long beginTime = localStartReqTime.get();

        Long endTime = System.currentTimeMillis();

        Long costTime = endTime - beginTime;
        if (costTime > 500){

            log.warn("【请求时间】{} 毫秒", costTime );
        }

    }
}
