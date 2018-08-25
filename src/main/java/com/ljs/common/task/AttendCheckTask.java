package com.ljs.common.task;

import com.ljs.attend.service.AttendService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author ljs
 * @Description TODO
 * @Date 2018/8/20 3:15
 **/
public class AttendCheckTask {

    @Autowired
    private AttendService attendService;

    public  void checkAttend(){
        attendService.checkAttend();
    }
}
