package com.ljs.workflow.service;


import com.ljs.workflow.dao.ReAttendMapper;
import com.ljs.workflow.entity.ReAttend;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;


import org.activiti.engine.task.Task;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author ljs
 * @Description 补签实现类
 * @Date 2018/8/20 8:30
 **/
@Service
public class ReAttendServiceImpl implements ReAttendService {

    private static final java.lang.String RE_ATTEND_FLOW_ID = "re_attend";

    /**
     * 补签流程状态
     * 1 进行中
     * 2 通过
     * 3 不通过
     */
    private static final Byte RE_ATTEND_STATUS_ONGOING = 1;
    private static final Byte RE_ATTEND_STATUS_PSSS = 2;
    private static final Byte RE_ATTEND_STATUS_REFUSE = 3;
    /**
     *
     */
    private static final Byte ATTEND_STATUS_NORMAL = 1;
    /**
     * 流程下一步处理人
     */
    private static final String NEXT_HANDLER = "next_handler";
    /**
     * 任务关联补签数据键
     */
    private static final String RE_ATTEND_SIGN = "re_attend";

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ReAttendMapper reAttendMapper;

    /**
     * Author ljs
     * Description 发起补签工作流任务
     * Date 2018/8/21 14:08
     **/
    @Override
    @Transactional
    public void startReAttendFlow(ReAttend reAttend) {
        //laowang是ljs的组长
        reAttend.setCurrentHandler("laowang");
        reAttend.setStatus(RE_ATTEND_STATUS_ONGOING); //处理中
        //插入数据库补签表
        reAttendMapper.insertSelective(reAttend);
        Map<String, Object> map = new HashMap();
        map.put(RE_ATTEND_SIGN, reAttend);
        map.put(NEXT_HANDLER, reAttend.getCurrentHandler());
        //启动补签流程实例
        ProcessInstance instance = runtimeService.startProcessInstanceByKey(RE_ATTEND_FLOW_ID, map);
        //提交用户补签任务
        Task task = taskService.createTaskQuery().processInstanceId(instance.getId()).singleResult();
        taskService.complete(task.getId(), map);

    }

    /**
     * Author ljs
     * Description 查询需要处理的任务列表
     * Date 2018/8/21 14:37
     **/
    @Override
    public List<ReAttend> listTasks(String username) {

        List<ReAttend> reAttendList = new ArrayList<ReAttend>();
        //根据username获取组长需要处理的工作流列表
        List<Task> taskList = taskService.createTaskQuery().processVariableValueEquals(username).list();
        //转换成页面实体，在页面显示出来
        if (CollectionUtils.isNotEmpty(taskList)) {
            for (Task task : taskList) {
                Map<String, Object> variable = taskService.getVariables(task.getId());
                ReAttend reAttend = (ReAttend) variable.get(RE_ATTEND_SIGN);
                reAttend.setTaskId(task.getId());
                reAttendList.add(reAttend);
            }
        }
        return reAttendList;
    }


    /**
     * Author ljs
     * Description 开始进行审批，审批结束修改页面状态
     * Date 2018/8/21 14:39
     **/
    @Override
    @Transactional
    public void approve(ReAttend reAttend) {
        //获取选中的工作流
        Task task = taskService.createTaskQuery().taskId(reAttend.getTaskId()).singleResult();
        if (RE_ATTEND_STATUS_PSSS.toString().equals(reAttend.getApproveFlag())) {
            // 审批通过，修改补签数据状态
            reAttend.setStatus(RE_ATTEND_STATUS_PSSS);
            reAttendMapper.updateByPrimaryKeySelective(reAttend);
        }else if(RE_ATTEND_STATUS_REFUSE.toString().equals(reAttend.getApproveFlag())){
            // 审批不通过，修改补签数据状态
            reAttend.setStatus(RE_ATTEND_STATUS_REFUSE);
            reAttendMapper.updateByPrimaryKeySelective(reAttend);
        }
        taskService.complete(reAttend.getTaskId());
    }

    /**
     *@Author JackWang [www.coder520.com]
     *@Date 2017/7/1 20:42
     *@Description 查询补签申请状态
     * @param username
     */
    @Override
    public List<ReAttend> listReAttend(String username) {
        List<ReAttend> list =reAttendMapper.selectReAttendRecord(username);
        return list;
    }
}
