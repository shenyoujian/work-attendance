package com.ljs.user.service;

import com.ljs.user.entity.User;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * @Author ljs
 * @Description TODO
 * @Date 2018/8/16 23:16
 **/
public interface UserService {

    User findUserByName(String username);

    void createUser(User user) throws UnsupportedEncodingException, NoSuchAlgorithmException;
}
