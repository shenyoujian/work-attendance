package com.ljs.user.service;

import com.ljs.user.dao.UserMapper;
import com.ljs.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author ljs
 * @Description TODO
 * @Date 2018/8/16 23:17
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Transactional
    @Override
    public int createUser(User user, User user1) {
        userMapper.insertSelective(user);
        userMapper.insertSelective(user1);
        return 0;

    }
}
