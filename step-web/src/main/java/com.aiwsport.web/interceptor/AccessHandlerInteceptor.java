package com.aiwsport.web.interceptor;

import com.aiwsport.core.ConfigServerException;
import com.aiwsport.core.ConfigServerExceptionFactor;
import com.google.common.collect.Maps;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class AccessHandlerInteceptor implements HandlerInterceptor {
    private static Map<String, String> ips = Maps.newConcurrentMap();

    static {
        ips.put("*", "");
        ips.put("10.77.9.41", "");
        ips.put("10.77.29.179", "");
        ips.put("10.77.29.180", "");
    }

    /**
     * controller 执行之前调用
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        //返回发出请求的IP地址
        if (ips.get("*") != null) {
            return true;
        } else {
            String ip = request.getRemoteAddr();
            if (ips.get(ip) != null) {
                return true;
            } else {
                throw new ConfigServerException(ConfigServerExceptionFactor.IP_NOT_VISIT_SERVER);
            }
        }
    }

    /**
     * controller 执行之后，且页面渲染之前调用
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

    }

    /**
     * 页面渲染之后调用，一般用于资源清理操作
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {


    }

}
