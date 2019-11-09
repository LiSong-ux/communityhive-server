package net.industryhive.controller;

import net.industryhive.bean.Login;
import net.industryhive.bean.User;
import net.industryhive.service.LoginService;
import net.industryhive.service.UserService;
import net.industryhive.utils.UnifiedResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 未央
 * @create 2019-10-30 9:51
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private LoginService loginService;

    @RequestMapping("/register")
    @ResponseBody
    public UnifiedResult register(HttpServletRequest request, User newUser) {
        String regAccount = "^[a-zA-Z]([-_a-zA-Z0-9]{8,31})$";
        String regEmail = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        String regMobile = "^1[34578]\\d{9}$";

        Pattern pattern = Pattern.compile(regAccount);
        Matcher matcher = pattern.matcher(newUser.getAccount());
        if (!matcher.matches()) {
            return UnifiedResult.build(400, "账号必须以英文字母开头，为字母、数字、下划线和中划线的组合，长度不得低于9位不得超过32位", null);
        }

        if (newUser.getPassword().length() < 9 || newUser.getPassword().length() > 32) {
            return UnifiedResult.build(400, "密码长度不得低于9位且不得超过32位", null);
        }
        if (newUser.getUsername().length() < 2 || newUser.getUsername().length() > 24) {
            return UnifiedResult.build(400, "用户名长度不得低于2位且不得超过24位", null);
        }

        Pattern patternMobile = Pattern.compile(regMobile);
        Matcher matcherMobile = patternMobile.matcher(newUser.getMobile());
        if (!matcherMobile.matches()) {
            return UnifiedResult.build(400, "手机号格式错误", null);
        }

        Pattern patternEmail = Pattern.compile(regEmail);
        Matcher matcherEmail = patternEmail.matcher(newUser.getEmail());
        if (!matcherEmail.matches()) {
            return UnifiedResult.build(400, "邮箱格式错误", null);
        }

        if (newUser.getGender() != 0 && newUser.getGender() != 1 && newUser.getGender() != 2) {
            return UnifiedResult.build(400, "未知选项", null);
        }

        User accountVali = userService.getUserByAccount(newUser.getAccount());
        if (accountVali != null) {
            return UnifiedResult.build(400, "账号已被注册", null);
        }

        User usernameVali = userService.getUserByUsername(newUser.getUsername());
        if (usernameVali != null) {
            return UnifiedResult.build(400, "用户名已被注册", null);
        }

        newUser.setRegistertime(new Date());
        User user = userService.addUser(newUser);

        //用户登录
        request.getSession().setAttribute("user", user);

        //记录用户登录ip、时间
        Login newLogin = new Login();
        newLogin.setIp(request.getRemoteAddr());
        newLogin.setUserId(user.getId());
        newLogin.setTime(new Date());

        String terminal = request.getParameter("terminal");
        newLogin.setTerminal(terminal);

        loginService.addLogin(newLogin);

        return UnifiedResult.ok(user);
    }

    @RequestMapping("/login")
    @ResponseBody
    public UnifiedResult login(HttpServletRequest request) {
        HttpSession session = request.getSession();

        // 先判断用户是否已经登录
        if (session.getAttribute("user") != null) {
            return UnifiedResult.build(400, "用户已登录", null);
        }

        String account = request.getParameter("account");
        String password = request.getParameter("password");

        User user = userService.getUserByAccount(account);
        if (user == null || !user.getPassword().equals(password)) {
            return UnifiedResult.build(400, "账号或密码错误", null);
        }

        session.setAttribute("user", user);

        // 用户登录次数+1
        user.setLogincount(user.getLogincount() + 1);
        userService.updateUser(user);

        //记录用户登录ip、时间
        Login newLogin = new Login();
        newLogin.setIp(request.getRemoteAddr());
        newLogin.setUserId(user.getId());
        newLogin.setTime(new Date());

        String terminal = request.getParameter("terminal");
        newLogin.setTerminal(terminal);

        loginService.addLogin(newLogin);

        return UnifiedResult.ok(user);
    }

}
