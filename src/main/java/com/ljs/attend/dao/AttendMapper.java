package com.ljs.attend.dao;

import com.ljs.attend.entity.Attend;
import com.ljs.attend.vo.QueryCondition;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

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

    /**
     * Author ljs
     * Description 查询今天没打卡的人的集合
     * Date 2018/8/20 4:12
     **/
    List<Long> selectTodayAbsence();

    /**
     * Author ljs
     * Description 批量插入打卡记录
     * Date 2018/8/20 4:12
     **/
    void batchInsert(List<Attend> attendList);

    /**
     * Author ljs
     * Description 查询晚上没打卡的人的集合
     * Date 2018/8/20 4:13
     **/
    List<Attend> selectTodayEveningAbsence();
}