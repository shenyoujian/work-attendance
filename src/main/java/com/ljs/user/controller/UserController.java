package com.ljs.user.controller;

import com.ljs.user.entity.User;
import com.ljs.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @Author ljs
 * @Description TODO
 * @Date 2018/8/16 22:34
 **/
@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping("/home")
    public String home(){
        return "home";
    }

    /**
     * Author ljs
     * Description 获取用户信息
     * Date 2018/8/18 15:02
     **/
    @RequestMapping("/userinfo")
    @ResponseBody
    public User userInfo(HttpSession session){
        User user = (User) session.getAttribute("userinfo");
        return user;
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "login";
    }

}
