package com.ljs.user.service;

import com.ljs.common.utils.MD5Utils;
import com.ljs.user.dao.UserMapper;
import com.ljs.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * @Author ljs
 * @Description TODO
 * @Date 2018/8/16 23:17
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * Author ljs
     * Description 根据用户名查询用户
     * Date 2018/8/17 21:51
     **/
    @Override
    public User findUserByName(String username) {
        User user = userMapper.selectByName(username);
        return user;
    }

    /**
     * Author ljs
     * Description 准备用户以供测试登录
     * Date 2018/8/17 22:06
     **/
    @Override
    public void createUser(User user) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        user.setPassword(MD5Utils.encryptPassword(user.getPassword()));
        userMapper.insertSelective(user);
    }
}
