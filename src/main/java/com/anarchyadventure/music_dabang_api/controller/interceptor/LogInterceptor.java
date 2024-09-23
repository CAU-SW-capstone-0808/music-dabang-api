package com.anarchyadventure.music_dabang_api.controller.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("[REQ][{}] {}?{} - from {}", request.getMethod(), request.getRequestURI(), request.getQueryString(), request.getRemoteAddr());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("[RES][{}][{}] {} - from {} / response type: {}", request.getMethod(), response.getStatus(),
                request.getRequestURI(), request.getRemoteAddr(), response.getContentType());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (ex != null) {
            log.error("[ERR][{}][{}] {} -> {}", request.getMethod(), response.getStatus(), request.getRequestURI(), ex.getMessage());
        }
    }
}
