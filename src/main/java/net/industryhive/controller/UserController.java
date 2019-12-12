package net.industryhive.controller;

import cn.hutool.core.date.DateUtil;
import net.industryhive.bean.Login;
import net.industryhive.bean.User;
import net.industryhive.been.wrap.WrapUser;
import net.industryhive.service.LoginService;
import net.industryhive.service.UserService;
import net.industryhive.utils.EmailUtil;
import net.industryhive.entity.UnifiedResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
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

    /**
     * 用来存储发送过验证码的邮箱账号和发送时间
     * 如果验证码发送时间在两分钟之前，可以再次发送验证码
     * 否则不允许发送验证码
     */
    private Map<String, Date> emailMap = new HashMap<>();

    /**
     * 用户注册
     *
     * @param request
     * @param newUser
     * @return
     */
    @RequestMapping("/register")
    @ResponseBody
    public UnifiedResult register(HttpServletRequest request, WrapUser newUser) {
        String regAccount = "^[a-zA-Z]([-_a-zA-Z0-9]{8,19})$";
        String regUsername = "^[a-zA-Z0-9_\\u4e00-\\u9fa5]{2,12}$";
        String regEmail = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
//        String regMobile = "^1[34578]\\d{9}$";

        Pattern patternAccount = Pattern.compile(regAccount);
        Matcher matcherAccount = patternAccount.matcher(newUser.getAccount());
        if (!matcherAccount.matches()) {
            return UnifiedResult.build(400, "账号必须以英文字母开头，为字母、数字、下划线和中划线的组合，长度不得低于9位不得超过20位", null);
        }

        if (newUser.getPassword().length() < 9 || newUser.getPassword().length() > 32) {
            return UnifiedResult.build(400, "密码长度不得低于9位且不得超过32位", null);
        }

        Pattern patternUsername = Pattern.compile(regUsername);
        Matcher matcherUsername = patternUsername.matcher(newUser.getUsername());
        if (!matcherUsername.matches()) {
            return UnifiedResult.build(400, "用户名为汉字、字母、数字的组合且长度为2至12位", null);
        }

//        Pattern patternMobile = Pattern.compile(regMobile);
//        Matcher matcherMobile = patternMobile.matcher(newUser.getMobile());
//        if (!matcherMobile.matches()) {
//            return UnifiedResult.build(400, "手机号格式错误", null);
//        }

        Pattern patternEmail = Pattern.compile(regEmail);
        Matcher matcherEmail = patternEmail.matcher(newUser.getEmail());
        if (!matcherEmail.matches()) {
            return UnifiedResult.build(400, "邮箱格式错误", null);
        }

        if (newUser.getGender() != 0 && newUser.getGender() != 1 && newUser.getGender() != 2) {
            return UnifiedResult.build(400, "未知选项", null);
        }
        //验证邮箱验证码
        if (!newUser.getEmailCode().equals(request.getSession().getAttribute("emailCode"))) {
            return UnifiedResult.build(400, "验证码错误", null);
        }
        request.getSession().setAttribute("emailCode", null);

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

    /**
     * 发送邮箱验证码
     *
     * @param session
     * @param email
     * @return
     */
    @RequestMapping("/getEmailCode")
    @ResponseBody
    public UnifiedResult getEmailCode(HttpSession session, String email) {
//        String emailCode = (String) session.getAttribute("emailCode");
//        if (emailCode != null) {
//            return UnifiedResult.build(400, "请不要频繁获取验证码", null);
//        }
        Date date = emailMap.get(email);
        if (date != null && DateUtil.betweenMs(date, new Date()) < 120000) {
            return UnifiedResult.build(400, "请不要频繁获取验证码", null);
        }

        String regEmail = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        Pattern patternEmail = Pattern.compile(regEmail);
        Matcher matcherEmail = patternEmail.matcher(email);
        if (!matcherEmail.matches()) {
            return UnifiedResult.build(400, "邮箱格式错误", null);
        }

        User emailVali = userService.getUserByEmail(email);
        if (emailVali != null) {
            return UnifiedResult.build(400, "该邮箱已被绑定，无法注册", null);
        }

        // 生成6位随机验证码
        String code = "";
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            int r = random.nextInt(10);
            code = code + r;
        }
        // 发送邮箱验证码
        String content = "您的邮箱验证码为：" + code + "，此验证码十分钟内有效。";
        try {
            EmailUtil.sendMail(email, content);
        } catch (Exception e) {
            e.printStackTrace();
            return UnifiedResult.build(500, "发送验证码失败，请再次尝试...", null);
        }

        //将邮箱账号和发送时间保存到Map中
        emailMap.put(email, new Date());

        //将邮箱验证码保存到session中
        session.setAttribute("emailCode", code);
        //设置验证码的有效期为10分钟
        session.setMaxInactiveInterval(600);
        return UnifiedResult.ok();
    }

    /**
     * 用户登录
     *
     * @param request
     * @return
     */
    @RequestMapping("/login")
    @ResponseBody
    public UnifiedResult login(HttpServletRequest request) {
        HttpSession session = request.getSession();

        //先判断用户是否已经登录
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
        //记录用户登录终端
        String terminal = request.getParameter("terminal");
        newLogin.setTerminal(terminal);

        loginService.addLogin(newLogin);

        return UnifiedResult.ok(user);
    }

}
