package com.tongji.service.config;

import com.tongji.service.interceptor.TokenInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;


@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private TokenInterceptor tokenInterceptor;


    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
        registry.addInterceptor(this.tokenInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login");
    }


    ///**
    // * 日期格式标准化
    // * @param converters 转换器们
    // */
    //protected void extendMessageConverters(List<HttpMessageConverter<?>> converters){
    //    // 创建一个消息转换器对象
    //    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    //
    //    // 设置对象转换器，可以将Java对象转为json字符串
    //    converter.setObjectMapper(new JacksonObjectMapper());
    //
    //    // 将我们自己的转换器放入到 spring MVC 框架的容器中
    //    converters.add(0, converter);
    //}
}
