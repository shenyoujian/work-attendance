package com.ljs.attend.controller;

import com.ljs.attend.entity.Attend;
import com.ljs.attend.service.AttendService;
import com.ljs.attend.vo.QueryCondition;
import com.ljs.common.page.PageQueryBean;
import com.ljs.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @Author ljs
 * @Description TODO
 * @Date 2018/8/18 16:02
 **/
@Controller
@RequestMapping("attend")
public class AttendController {

    @Autowired
    private AttendService attendService;

    @RequestMapping
    public String toAttend() {
        return "attend";
    }

    /**
     * Author ljs
     * Description 模拟签到(使用postman提交)
     * Date 2018/8/18 19:36
     **/
    @RequestMapping("/sign")
    @ResponseBody
    public String signAttend(@RequestBody Attend attend) throws Exception {
        attendService.signAttend(attend);
        return "succ";
    }

    @RequestMapping("/attendList")
    @ResponseBody
    public PageQueryBean listAttend(QueryCondition condition, HttpSession session) {
        User user = (User) session.getAttribute("userinfo");
        //获取前端操作条件
        String [] rangeDate = condition.getRangeDate().split("/");
        condition.setStartDate(rangeDate[0]);
        condition.setEndDate(rangeDate[1]);
        condition.setUserId(user.getId());
        PageQueryBean result = attendService.listAttend(condition);
        return result;

    }

}
