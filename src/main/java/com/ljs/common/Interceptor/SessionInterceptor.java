package com.ljs.common.Interceptor;

import com.ljs.user.entity.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @Author ljs
 * @Description 拦截除login页面的请求
 * @Date 2018/8/18 0:12
 **/
public class SessionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        //拦截请求到达之前
        String uri = request.getRequestURI();
        //如果url里包含了login就不拦截，包括loginxxx
        if (uri.indexOf("login") >= 0) {
           return true;
        }

        //检查session是否有userinfo
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("userinfo");
        if (user != null) {
            return true;
        }

        //转发到登录
        request.getRequestDispatcher("/login").forward(request, response);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
