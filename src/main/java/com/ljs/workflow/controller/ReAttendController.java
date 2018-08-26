package com.ljs.workflow.controller;

import com.ljs.user.entity.User;
import com.ljs.workflow.entity.ReAttend;
import com.ljs.workflow.service.ReAttendService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Author ljs
 * @Description 补签工作流控制器
 * @Date 2018/8/20 8:29
 **/
@Controller
@RequestMapping("reAttend")
public class ReAttendController {

    @Autowired
    private ReAttendService reAttendService;

    /**
     * Author ljs
     * Description 补签数据页面
     * Date 2018/8/22 19:19
     **/
    @RequestMapping
    public String toReAttend(Model model, HttpSession session) {
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("userinfo");
//        User user = (User) session.getAttribute("userinfo");
        List<ReAttend> reAttendList = reAttendService.listReAttend(user.getUsername());
        model.addAttribute("reAttendList",reAttendList);
        return "reAttend";
    }

    /**
     * Author ljs
     * Description 用户发起工作流补签任务
     * Date 2018/8/20 8:47
     **/
    @RequestMapping("/start")
    public void startReAttendFlow(@RequestBody ReAttend reAttend, HttpSession session) {
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("userinfo");
        reAttend.setReAttendStarter(user.getUsername());                 //补签发起人
        reAttendService.startReAttendFlow(reAttend);
    }


    /**
     * Author ljs
     * Description 查看需要处理的任务列表
     * Date 2018/8/20 8:49
     **/
    @RequiresPermissions("reAttend:list")
    @RequestMapping("/list")
    public String listReAttendFlow(Model model, HttpSession session) {
        // User user = session.getAttribute("userinfo");
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("userinfo");
        String userName = user.getUsername();
        List<ReAttend> tasks = reAttendService.listTasks(userName);
        model.addAttribute("tasks",tasks);
        return  "reAttendApprove";
    }

    /**
     * Author ljs
     * Description 选择某个任务进行审批
     * Date 2018/8/20 8:52
     **/
    @RequestMapping("/approve")
    public void approveReAttendFlow(@RequestBody ReAttend reAttend, HttpServletResponse response) {
        reAttendService.approve(reAttend);
    }
}
