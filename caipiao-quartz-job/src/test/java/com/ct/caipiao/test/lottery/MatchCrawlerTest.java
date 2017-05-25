package com.ct.caipiao.test.lottery;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ct.App;
import com.ct.caipiao.lottery.job.JclqMatchCrawler;
import com.ct.caipiao.lottery.job.JczqMatchCrawler;
import com.ct.caipiao.lottery.job.JczqOddsCrawler;

@RunWith(SpringJUnit4ClassRunner.class)  
@SpringBootTest(classes = App.class)
public class MatchCrawlerTest {
	
	private Logger logger = LoggerFactory.getLogger(MatchCrawlerTest.class);
	@Resource
	private JclqMatchCrawler jclqMatchCrawler;
	
	@Resource
	private JczqMatchCrawler jczqMatchCrawler;
	
	@Resource
	private JczqOddsCrawler jczqOddsCrawler;
	
	@Resource
	private MongoTemplate mongoTemplate;
	
	@Test
	public void testJclqMatchCrawlerForMnl() throws JobExecutionException{
		jclqMatchCrawler.setPoolcode("mnl");
		jclqMatchCrawler.execute(null);
	}
	
	@Test
	public void testJclqMatchCrawlerForHdc() throws JobExecutionException{
		jclqMatchCrawler.setPoolcode("hdc");
		jclqMatchCrawler.execute(null);
	}
	
	@Test
	public void testJclqMatchCrawlerForHilo() throws JobExecutionException{
		jclqMatchCrawler.setPoolcode("hilo");
		jclqMatchCrawler.execute(null);
	}
	
	@Test
	public void testJczqMatchCrawler() throws JobExecutionException{
		jczqMatchCrawler.execute(null);
	}
	@Test
	public void testJczqOddsCrawlerForHhad() throws JobExecutionException{
		jczqOddsCrawler.setPoolcode("hhad");
		jczqOddsCrawler.execute(null);
	}
	@Test
	public void testJczqOddsCrawlerForHad() throws JobExecutionException{
		jczqOddsCrawler.setPoolcode("had");
		jczqOddsCrawler.execute(null);
	}
	@Test
	public void testJczqOddsCrawlerForCrs() throws JobExecutionException{
		jczqOddsCrawler.setPoolcode("crs");
		jczqOddsCrawler.execute(null);
	}
	@Test
	public void testJczqOddsCrawlerForTtg() throws JobExecutionException{
		jczqOddsCrawler.setPoolcode("ttg");
		jczqOddsCrawler.execute(null);
	}
	@Test
	public void testJczqOddsCrawlerForHafu() throws JobExecutionException{
		jczqOddsCrawler.setPoolcode("hafu");
		jczqOddsCrawler.execute(null);
	}
	
	@Test
	public void testDropMongodbCollectionName() throws JobExecutionException{
		for(String collectionName : mongoTemplate.getCollectionNames()){
			mongoTemplate.dropCollection(collectionName);
			logger.debug("=============>>collectionName:{}",collectionName);
		}
	}
}
