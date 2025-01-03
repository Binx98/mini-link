package com.minilink.interceptor;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.minilink.annotation.NoLogin;
import com.minilink.constant.CommonConstant;
import com.minilink.enums.BusinessCodeEnum;
import com.minilink.exception.BusinessException;
import com.minilink.pojo.po.LinkUser;
import com.minilink.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Author 徐志斌
 * @Date: 2024/12/8 17:05
 * @Version 1.0
 * @Description: 登录Token-拦截器
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
    public static ThreadLocal<LinkUser> threadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (HttpMethod.OPTIONS.toString().equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpStatus.NO_CONTENT.value());
            return true;
        }
        HandlerMethod method = (HandlerMethod) handler;
        NoLogin noLogin = method.getMethod().getAnnotation(NoLogin.class);
        if (ObjectUtils.isNotEmpty(noLogin)) {
            return true;
        }
        String token = request.getHeader(CommonConstant.HEADER_TOKEN_KEY);
        if (ObjectUtils.isEmpty(token)) {
            throw new BusinessException(BusinessCodeEnum.ACCOUNT_NO_LOGIN);
        }
        LinkUser user = JwtUtil.resolve(token);
        if (ObjectUtils.isEmpty(user)) {
            throw new BusinessException(BusinessCodeEnum.ACCOUNT_NO_LOGIN);
        }
        threadLocal.set(user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        threadLocal.remove();
    }
}
