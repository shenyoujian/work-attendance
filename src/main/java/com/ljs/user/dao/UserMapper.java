package com.ljs.user.dao;

import com.ljs.user.entity.User;

public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * Author ljs
     * Description 根据用户名查找用户
     * Date 2018/8/17 21:54
     **/
    User selectByName(String username);
}