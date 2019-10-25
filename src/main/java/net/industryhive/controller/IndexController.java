package net.industryhive.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 未央
 * @create 2019-10-25 21:28
 */
@Controller
public class IndexController {

    @RequestMapping("/")
    public String hello(){
        return "index";
    }

    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    @RequestMapping("/page")
    public String page(){
        return "index";
    }

}
