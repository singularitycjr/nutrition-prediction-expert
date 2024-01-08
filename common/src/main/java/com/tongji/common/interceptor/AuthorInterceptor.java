package com.tongji.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class AuthorInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 获取拦截到的路由信息
        // 获取请求头中的参数

        String key = request.getHeader("gateway_key");
        log.info("当前拦截到的key是：{}", key);
        if (!"r8t41n2i5ifsd1g3".equals(key)) {
            throw new RuntimeException("非法请求");
        }
        return true;
    }
}
