package com.ljs.attend.service;

import com.ljs.attend.dao.AttendMapper;
import com.ljs.attend.entity.Attend;
import com.ljs.attend.vo.QueryCondition;
import com.ljs.common.page.PageQueryBean;
import com.ljs.common.utils.DateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author ljs
 * @Description TODO
 * @Date 2018/8/18 19:23
 **/
@Service("attendServiceImpl")
public class AttendServiceImpl implements AttendService {

    /**
     * 中午十二点 判定上下午
     **/
    private static final int NOON_HOUR = 12;
    private static final int NOON_MINUTE = 00;
    private Log log = LogFactory.getLog(AttendServiceImpl.class);

    @Autowired
    private AttendMapper attendMapper;

    /**
     * Author ljs
     * Description 模拟签到
     * Date 2018/8/18 19:36
     **/
    @Override
    public void signAttend(Attend attend) throws Exception {
        try {
            Date today = new Date();
            attend.setAttendDate(today);
            attend.setAttendWeek((byte) DateUtil.getTodayWeek());
            Date noon = DateUtil.getDate(NOON_HOUR, NOON_MINUTE);

            // 查询当天是否有打卡记录
            Attend todayRecord = attendMapper.selectTodaySignRecord(attend.getUserId());

            if (todayRecord != null) {
                if (today.compareTo(noon) <= 0) {
                    //有记录并且now打卡时间早于12点,不用再存进去
                    return;
                } else {
                    //有记录并且now打卡时间晚于12点，需要更新
                    //注意是更新todayRecord,不是today
                    todayRecord.setAttendEvening(today);
                    attendMapper.insertSelective(todayRecord);
                }
            } else {
                if (today.compareTo(noon) <= 0) {
                    //没有记录并且now打卡时间早于12点,需要存进去
                    attend.setAttendMorning(today);
                } else {
                    //没有记录并且now打卡时间晚于12点，需要存进去
                    attend.setAttendEvening(today);
                }
                attendMapper.insertSelective(attend);
            }
            //1、中午十二点以前打卡都算上午打卡，过9:30以后打卡，直接异常算迟到
            //2、十二点以后都算下午打卡，超过6:00以后打卡直接异常，算早退
            //3、选择最后一个下午的打卡时间与上午打卡进行比较获取时间差
            //4、时间差不足8小时算异常，并且缺席多长时间要存进去
        } catch (Exception e) {
            log.error("用户签到异常", e);
            throw e;
        }

    }

    /**
     * Author ljs
     * Description 根据查询条件返回分页数据
     * Date 2018/8/19 5:15
     **/
    @Override
    public PageQueryBean listAttend(QueryCondition condition) {
        // 根据条件查询 count记录数目
        int count = attendMapper.countByCondition(condition);
        PageQueryBean pageResult = new PageQueryBean();
        if (count > 0) {
            pageResult.setTotalRows(count);
            pageResult.setCurrentPage(condition.getCurrentPage());
            pageResult.setPageSize(condition.getPageSize());
            List<Attend> attendList = attendMapper.selectAttendPage(condition);
            pageResult.setItems(attendList);
        }
        //如果有记录 才去查询分页数据 没有相关记录数目 没必要去查分页数据
        return pageResult;
    }
}
