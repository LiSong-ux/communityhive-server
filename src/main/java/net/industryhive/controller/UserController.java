package net.industryhive.controller;

import net.industryhive.bean.User;
import net.industryhive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 未央
 * @create 2019-10-30 9:51
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value="/register", method= RequestMethod.POST)
    @ResponseBody
    public String register(User newUser) {
        //String result = userService.addUser(newUser);
        System.out.println(newUser.getUsername());
        return "success_api";
    }

}
