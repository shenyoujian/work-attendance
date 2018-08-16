package com.ljs.user.controller;

import com.ljs.user.entity.User;
import com.ljs.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @RequestMapping("/index")
    public String user(){
        User user = new User();
        user.setRealName("ljs");
        user.setMobile("15622716980");
        user.setPassword("123");
        user.setUsername("ljs");
        User user1 = new User();
        user1.setId(1L);
        user1.setRealName("ljs");
        user1.setMobile("15622716980");
        user1.setPassword("123");
        user1.setUsername("ljs");
        userService.createUser(user, user1);
        return "user";
    }
}
