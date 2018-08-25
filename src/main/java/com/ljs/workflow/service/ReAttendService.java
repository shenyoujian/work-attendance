package com.ljs.workflow.service;



import com.ljs.workflow.entity.ReAttend;

import java.util.List;

/**
 * @Author ljs
 * @Description TODO
 * @Date 2018/8/20 8:30
 **/
public interface ReAttendService {


    void startReAttendFlow(ReAttend reAttend);

    void approve(ReAttend reAttend);

    List<ReAttend> listTasks(String username);

    List<ReAttend> listReAttend(String username);
}
