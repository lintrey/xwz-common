package com.wanzi.common.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanzi.common.aop.CrossDomainInterceptor;
import com.wanzi.common.aop.TimeCostInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: common
 * @description: 拦截器、静态资源、页面跳转 配置中心
 * @author: zhangchuntao
 * @create: 2018-08-02
 **/
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {


    /**
     *  拦截器addInterceptors
     *  实现拦截器功能需要完成以下2个步骤：
     * 创建我们自己的拦截器类并实现 HandlerInterceptor 接口
     * 其实重写WebMvcConfigurerAdapter中的addInterceptors方法把自定义的拦截器类添加进来即可
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //addPathPatterns("/**")对所有请求都拦截
        //excludePathPatterns 排除/error、/login请求
        registry.addInterceptor(new TimeCostInterceptor()).addPathPatterns("/**").excludePathPatterns("/error","/login");
        registry.addInterceptor(new CrossDomainInterceptor()).addPathPatterns("/**");

        //拦截器链，按add顺序拦截
        super.addInterceptors(registry);
    }

    /**
     * springboot 静态资源自动配置：
     * classpath:/META-INF/resources
     * classpath:/resources
     * classpath:/static
     * classpath:/public
     *
     * 此方法为静态资源用户配置：
     *  通过addResourceHandler添加映射路径，然后通过addResourceLocations来指定路径
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //指定外部路径
        registry.addResourceHandler("/**").addResourceLocations("file:F:/demo/");
        //指定工程路径
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        //某些资源默认在此路径，so  加上这个路径
        registry.addResourceHandler("/**").addResourceLocations("classpath:/META-INF/resources/").setCachePeriod(0);
        super.addResourceHandlers(registry);
    }

    /**
     * 页面跳转
     *
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        registry.addViewController("/toLogin").setViewName("login");
        super.addViewControllers(registry);
    }


    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        ByteArrayHttpMessageConverter byteArrayHttpMessageConverter = new ByteArrayHttpMessageConverter();
        converters.add(byteArrayHttpMessageConverter);
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        converters.add(stringHttpMessageConverter);

        List<MediaType> supportedMediaTypes = new ArrayList<MediaType>(3);
        supportedMediaTypes.add(new MediaType("application", "json", Charset.forName("UTF-8")));
        supportedMediaTypes.add(new MediaType("text", "html", Charset.forName("UTF-8")));

        // 排除空值
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().serializationInclusion(JsonInclude.Include.NON_NULL).build();
        MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter = new MappingJackson2HttpMessageConverter(objectMapper);

        mappingJacksonHttpMessageConverter.setSupportedMediaTypes(supportedMediaTypes);
        converters.add(mappingJacksonHttpMessageConverter);
    }
}
