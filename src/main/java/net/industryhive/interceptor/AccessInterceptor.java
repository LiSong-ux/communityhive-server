package net.industryhive.interceptor;

import net.industryhive.bean.Access;
import net.industryhive.service.AccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 访问统计拦截器
 * @author 未央
 * @create 2019-11-04 22:41
 */
public class AccessInterceptor implements HandlerInterceptor {

    @Autowired
    private AccessService accessService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String clientIP = request.getRemoteAddr();

        Access newAccess = new Access();
        newAccess.setIp(clientIP);
        newAccess.setAccesstime(new Date());


        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
