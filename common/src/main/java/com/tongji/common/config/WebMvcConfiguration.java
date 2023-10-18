package com.tongji.common.config;

import com.tongji.common.interceptor.TokenInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


//@Configuration
@Slf4j
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private TokenInterceptor tokenInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
        registry.addInterceptor(this.tokenInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/register")
                .excludePathPatterns("/v3/api-docs/**", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**");
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
