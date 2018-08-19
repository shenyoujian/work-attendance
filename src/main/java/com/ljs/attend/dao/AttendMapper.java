package com.ljs.attend.dao;

import com.ljs.attend.entity.Attend;
import com.ljs.attend.vo.QueryCondition;

import java.util.List;

public interface AttendMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Attend record);

    int insertSelective(Attend record);

    Attend selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Attend record);

    int updateByPrimaryKey(Attend record);

    /**
     * Author ljs
     * Description  查询某个用户今天的打卡记录
     * Date 2018/8/19 2:26
     **/
    Attend selectTodaySignRecord(long userId);

    /**
     * Author ljs
     * Description 根据查询条件返回总条目
     * Date 2018/8/19 5:35
     **/
    int countByCondition(QueryCondition condition);

    List<Attend> selectAttendPage(QueryCondition condition);
}