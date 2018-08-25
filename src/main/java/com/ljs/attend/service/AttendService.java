package com.ljs.attend.service;

import com.ljs.attend.entity.Attend;
import com.ljs.attend.vo.QueryCondition;
import com.ljs.common.page.PageQueryBean;

/**
 * @Author ljs
 * @Description TODO
 * @Date 2018/8/18 19:22
 **/
public interface AttendService {

    void signAttend(Attend attend) throws Exception;

    PageQueryBean listAttend(QueryCondition condition);

    void checkAttend();
}
