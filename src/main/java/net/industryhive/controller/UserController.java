package net.industryhive.controller;

//import net.industryhive.bean.User;
import net.industryhive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @author 未央
 * @create 2019-10-30 9:51
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

//    @RequestMapping("/register")
//    @ResponseBody
//    public UnifiedResult register(User newUser) {
//        newUser.setRegistertime(new Date());
//        userService.addUser(newUser);
//        return UnifiedResult.ok();
//    }

}
