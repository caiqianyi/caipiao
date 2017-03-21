package com.ct.soa.quartz.manager.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchProperties.Job;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.ct.soa.quartz.core.AbstractQuartzJob;
import com.ct.soa.quartz.core.JobGroup;
import com.ct.soa.quartz.core.entity.TaskInfo;
import com.ct.soa.quartz.core.service.impl.TaskServiceImpl;
import com.ct.soa.web.framework.model.SuccessModel;

/**
 * �������
 */
@Controller
public class TaskManageController {
	@Autowired
	private TaskServiceImpl taskServiceImpl;

	@Resource
	private Map<String,AbstractQuartzJob> jobs;

	/**
	 * Index.jsp
	 */
	@RequestMapping(value={"", "/", "index"})
	public String info(String jobGroup,Map<String, Object> model){
		if(StringUtils.isNotBlank(jobGroup))
			model.put("jobGroup", JobGroup.valueOf(jobGroup));
		List<String> jobClassNames = new ArrayList<String>();
		for(AbstractQuartzJob job : jobs.values()){
			jobClassNames.add(job.getClass().getName());
		}
		model.put("jobGroups", JobGroup.values());
		model.put("jobClassNames",jobClassNames);
		return "index.jsp";
	}
	
	/**
	 * �����б�
	 * @return
	 * @throws SchedulerException 
	 */
	@ResponseBody
	@RequestMapping(value="list", method=RequestMethod.POST)
	public String list(String jobGroup) throws SchedulerException{
		Map<String, Object> map = new HashMap<String, Object>();
		List<TaskInfo> infos = taskServiceImpl.list(jobGroup);
		map.put("rows", infos);
		map.put("total", infos.size());
		return JSON.toJSONString(map);
	}
	 
	/**
	 * ���涨ʱ����
	 * @param info
	 */
	@ResponseBody
	@RequestMapping(value="save", method=RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public SuccessModel save(TaskInfo info){
		if(info.getId() == 0)
			taskServiceImpl.addJob(info);
		else
			taskServiceImpl.edit(info);
		return new SuccessModel("ok");
	}
	
	/**
	 * ɾ��ʱ����
	 * @param jobName
	 * @param jobGroup
	 */
	@ResponseBody
	@RequestMapping(value="delete/{jobName}/{jobGroup}", produces = "application/json; charset=UTF-8")
	public SuccessModel delete(@PathVariable String jobName, @PathVariable String jobGroup){
		taskServiceImpl.delete(jobName, "undefined".equals(jobGroup) ? "" : jobGroup);
		return new SuccessModel("ok");
	}
	
	/**
	 * ��ͣ��ʱ����
	 * @param jobName
	 * @param jobGroup
	 */
	@ResponseBody
	@RequestMapping(value="pause/{jobName}/{jobGroup}", produces = "application/json; charset=UTF-8")
	public SuccessModel pause(@PathVariable String jobName, @PathVariable String jobGroup){
		taskServiceImpl.pause(jobName, jobGroup);
		return new SuccessModel("ok");
	}
	
	/**
	 * ���¿�ʼ��ʱ����
	 * @param jobName
	 * @param jobGroup
	 */
	@ResponseBody
	@RequestMapping(value="resume/{jobName}/{jobGroup}", produces = "application/json; charset=UTF-8")
	public SuccessModel resume(@PathVariable String jobName, @PathVariable String jobGroup){
		taskServiceImpl.resume(jobName, jobGroup);
		return new SuccessModel("ok");
	}
}
