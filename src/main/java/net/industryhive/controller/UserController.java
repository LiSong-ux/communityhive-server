package net.industryhive.controller;

import net.industryhive.bean.User;
import net.industryhive.service.UserService;
import net.industryhive.utils.UnifiedResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author 未央
 * @create 2019-10-30 9:51
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/register")
    @ResponseBody
    public UnifiedResult register(User newUser) {
        newUser.setRegistertime(new Date());
        userService.addUser(newUser);
        return UnifiedResult.ok();
    }

    @RequestMapping("/login")
    @ResponseBody
    public UnifiedResult login(HttpServletRequest request){
        String account = request.getParameter("account");
        String password = request.getParameter("password");

        User user = userService.getUser(account);
        if (user==null||!user.getPassword().equals(password)){
            return UnifiedResult.build(400, "账号或密码错误", null);
        }
        request.getSession().setAttribute("user", user);

        return UnifiedResult.ok();
    }

}
