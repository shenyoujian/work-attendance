package com.ljs.attend.service;

import com.ljs.attend.dao.AttendMapper;
import com.ljs.attend.entity.Attend;
import com.ljs.attend.vo.QueryCondition;
import com.ljs.common.page.PageQueryBean;
import com.ljs.common.utils.DateUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author ljs
 * @Description TODO
 * @Date 2018/8/18 19:23
 **/
@Service
public class AttendServiceImpl implements AttendService {

    /**
     * 中午十二点 用于判定上下午
     * 早上9点半 用于判定是否迟早
     * 下午6点半 用于判定是否早退
     **/
    private static final int NOON_HOUR = 12;
    private static final int NOON_MINUTE = 00;
    private static final int MORNING_HOUR = 9;
    private static final int MORNING_MINUTE = 30;
    private static final int EVENING_HOUR = 18;
    private static final int EVENING_MINUTE = 30;

    /**
     * 缺勤一整天
     **/
    private static final Integer ABSENCE_DAY = 480;
    /**
     * 考勤异常状态
     **/
    private static final Byte ATTEND_STATUS_ABNORMAL = 2;
    private static final Byte ATTEND_STATUS_NORMAL = 1;


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
            Date monringAttend = DateUtil.getDate(MORNING_HOUR, MORNING_MINUTE);
            Date eveningAttend = DateUtil.getDate(EVENING_HOUR, EVENING_MINUTE);

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
                    //计算打卡时间是否迟到
                    if (today.compareTo(monringAttend) > 0) {
                        //大于9点半迟到了
                        attend.setAttendStatus(ATTEND_STATUS_ABNORMAL);
                        attend.setAbsence(DateUtil.getMinutes(monringAttend, today));
                    } else {
                        //没有迟早
                        attend.setAttendStatus(ATTEND_STATUS_NORMAL);
                        attend.setAbsence(0);
                    }
                } else {
                    //没有记录并且now打卡时间晚于12点，需要存进去
                    attend.setAttendEvening(today);
                    //计算打卡时间是否早退
                    if (today.compareTo(eveningAttend) < 0) {
                        //早于下午6点半早退了
                        attend.setAttendStatus(ATTEND_STATUS_ABNORMAL);
                        attend.setAbsence(DateUtil.getMinutes(today, eveningAttend));
                    } else {
                        //没有早退
                        attend.setAttendStatus(ATTEND_STATUS_NORMAL);
                        attend.setAbsence(0);
                    }
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

    @Override
    @Transactional
    public void checkAttend() {
        //1、查询缺勤用户ID，插入打卡记录 并且设置为异常 缺勤480分钟
        List<Long> userIdList = attendMapper.selectTodayAbsence();
        // 健壮性非空判断，使用这个apache的这个工具类可以判断非空，美观一点。。。
        if (CollectionUtils.isNotEmpty(userIdList)) {
            List<Attend> attendList = new ArrayList<Attend>();
            for (Long userId : userIdList) {
                Attend attend = new Attend();
                attend.setUserId(userId);
                attend.setAttendDate(new Date());
                attend.setAttendWeek((byte) DateUtil.getTodayWeek());
                attend.setAttendStatus(ATTEND_STATUS_ABNORMAL);
                attend.setAbsence(ABSENCE_DAY);
                attendList.add(attend);
            }
            attendMapper.batchInsert(attendList);
        }

        //2、检查晚打卡 将下班未打卡设置为异常
        List<Attend> absenceList = attendMapper.selectTodayEveningAbsence();
        if (CollectionUtils.isNotEmpty(absenceList)) {
            for (Attend attend : absenceList) {
                attend.setAbsence(ABSENCE_DAY);
                attend.setAttendStatus(ATTEND_STATUS_ABNORMAL);
                attendMapper.updateByPrimaryKeySelective(attend);
            }
        }
    }
}
