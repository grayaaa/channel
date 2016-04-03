package com.netease.channel.exception;

import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class DefaultExceptionHandler {
    /**
     * 没有权限 异常
     * <p/>
     * 后续根据不同的需求定制即可
     */
    @ExceptionHandler({UnauthorizedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ModelAndView processUnauthenticatedException(NativeWebRequest request, UnauthorizedException e) {
        StringBuilder message = new StringBuilder();
        message.append("用户 ")
                .append(" 没有访问权限，").append("如需获得本系统的访问权限，请联系孟彦").append(" <a href=\"/\">回到首页</a> ");

        ModelAndView mv = new ModelAndView();
        mv.addObject("errorMsg", message);
        mv.addObject("exception", e);
        mv.setViewName("error");
        return mv;
    }
}
