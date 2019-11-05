package net.industryhive.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 访问统计拦截器
 *
 * @author 未央
 * @create 2019-11-04 22:41
 */
public class AccessInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        ServletContext context = request.getServletContext();
        Integer totalAccess = (Integer) context.getAttribute("totalAccess");
        Integer pcAccess = (Integer) context.getAttribute("pcAccess");
        Integer androidAccess = (Integer) context.getAttribute("androidAccess");
        Integer iosAccess = (Integer) context.getAttribute("iosAccess");
        Integer othersAccess = (Integer) context.getAttribute("othersAccess");

        if (totalAccess == null) {
            context.setAttribute("totalAccess", 1);
        } else {
            context.setAttribute("totalAccess", totalAccess + 1);
        }

        String terminal = request.getParameter("terminal");
        if (terminal == null) {
            terminal = "";
        }

        if (terminal.indexOf("Android") > -1 || terminal.indexOf("Adr") > -1 || terminal.indexOf("android") > -1) {
            if (androidAccess == null) {
                context.setAttribute("androidAccess", 1);
            } else {
                context.setAttribute("androidAccess", androidAccess + 1);
            }
        } else if (terminal.indexOf("ios") > -1 || terminal.indexOf("iPhone") > -1 || terminal.indexOf("iPad") > -1 || terminal.indexOf("iPod") > -1) {
            if (iosAccess == null) {
                context.setAttribute("iosAccess", 1);
            } else {
                context.setAttribute("iosAccess", iosAccess + 1);
            }
        } else {
            String[] others = {"phone", "Mobile", "BlackBerry", "IEMobile", "MQQBrowser", "JUC", "Fennec", "wOSBrowser",
                    "BrowserNG", "WebOS", "Symbian", "Windows Phone", "pad", "pod"};
            boolean flag = true;
            for (int i = 0; i < others.length; i++) {
                if (terminal.indexOf(others[i]) > -1) {
                    if (othersAccess == null) {
                        context.setAttribute("othersAccess", 1);
                        flag = false;
                    } else {
                        context.setAttribute("othersAccess", othersAccess + 1);
                    }
                    break;
                }
            }
            if (flag) {
                if (pcAccess == null) {
                    context.setAttribute("pcAccess", 1);
                } else {
                    context.setAttribute("pcAccess", pcAccess + 1);
                }
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
