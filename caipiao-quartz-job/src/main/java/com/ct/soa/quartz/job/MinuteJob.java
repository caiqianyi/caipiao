package com.ct.soa.quartz.job;

import java.util.Date;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.time.DateFormatUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cn.edu.hfut.dmic.webcollector.model.Page;

import com.ct.soa.quartz.core.AbstractQuartzJob;
import com.ct.soa.quartz.core.service.ITaskService;

/**
 * 该方法仅仅用来测试每分钟执行
 * 
 * 
序号	说明	是否必填	允许填写的值	允许的通配符
1	秒	     是	       0-59	,     - * /
2	分	     是	       0-59 ,     - * /
3	小时	     是	       0-23	,     - * /
4	日	     是	       1-31	,     - * ? / L W
5	月	     是	       1-12 or JAN-DEC, - * /
6	周	     是	       1-7 or SUN-SAT, - * ? / L #
7	年	     否	       empty 或 1970-2099	, - * /
 * @author cqy
 */
@Component
public class MinuteJob extends AbstractQuartzJob{
	Logger logger = LoggerFactory.getLogger(MinuteJob.class);
	
	@Resource
	private ITaskService taskService;

	@Override
	public boolean handle(JobExecutionContext arg0) {
		taskService.addSystemJob("com.xlhy.soa.quartz.job.MinuteJob", 60 , "MinuteJob");
		logger.info("JobName: {}", arg0.getJobDetail().getKey().getName() + " "+DateFormatUtils.format(new Date(), "HH:mm:ss"));
		return false;
	}
	
	@Override  
    public void visit(Page page) {  
        String question_regex="^http://www.zhihu.com/question/[0-9]+";           
        if(Pattern.matches(question_regex, page.getUrl())){                
            System.out.println("processing "+page.getUrl());  
  
            /*extract title of the page*/  
            String title=page.getDoc().title();  
            System.out.println("title:"+title);  
  
            /*extract the content of question*/  
            String question=page.getDoc().select("div[id=zh-question-detail]").text();  
            System.out.println("question:"+question);  
  
        }
    }
	
	
	public static void main(String[] args) throws Exception {
		/*Calendar c = Calendar.getInstance();
		c.set(Calendar.SECOND, c.get(Calendar.SECOND) + 120);
		String exp = c.get(Calendar.SECOND)+" "+c.get(Calendar.MINUTE)+" "+c.get(Calendar.HOUR_OF_DAY)+" "+c.get(Calendar.DAY_OF_MONTH) +" "+ (c.get(Calendar.MONTH)+1) +" ? "+c.get(Calendar.YEAR);
		System.out.println(exp);*/
		
		MinuteJob crawler=new MinuteJob();  
        crawler.addSeed("http://www.zhihu.com/question/21003086");  
        crawler.addRegex("http://www.zhihu.com/.*");
        crawler.start(1);    
	}

}