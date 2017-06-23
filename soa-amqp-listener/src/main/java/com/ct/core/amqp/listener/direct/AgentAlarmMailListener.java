package com.ct.core.amqp.listener.direct;

import java.util.Arrays;

import javax.mail.MessagingException;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.ct.commons.mail.JMailSender;
import com.ct.commons.mail.JMailTemplate;
import com.ct.commons.mail.alarm.AlarmMailMQMessage;
import com.ct.commons.mail.alarm.AlarmMailTemplate;
import com.ct.soa.amqp.core.DirectRabbitConfig;
import com.google.gson.Gson;


/**
 * 预警邮件队列消费者
 * @author caiqianyi
 *
 */
@Component
@RabbitListener(queues = DirectRabbitConfig.AlarmMailQueue)
public class AgentAlarmMailListener {

	@Bean
    public Queue queueAgentAlarmMail() {
        return new Queue(DirectRabbitConfig.AlarmMailQueue);
    }

    @Bean
    Binding bindingDirectExchangeAgentAlarmMail(Queue queueAgentAlarmMail, DirectExchange directExchange) {
        return BindingBuilder.bind(queueAgentAlarmMail).to(directExchange).with(DirectRabbitConfig.AlarmMailQueue);
    }
	
	@RabbitHandler
    public void receive(Object data) {
		Message message = (Message) data;
		String json = new String(message.getBody());
		try {
			String str = json.replace("\\\"", "\"");
			str = str.substring(1, str.length()-1);
			
			AlarmMailMQMessage ammessage = new Gson().fromJson(str, AlarmMailMQMessage.class);
			
			JMailSender sender = new JMailSender("smtp.exmail.qq.com","platformalerm@ixianlai.com","Platform2017");
			
			JMailTemplate template = new JMailTemplate();
			
			AlarmMailTemplate mailTemplate = AlarmMailTemplate.valueOfTemplate(ammessage.getTemplte_id());
			template.setContent(mailTemplate.getContent(ammessage.getDatas().toArray()));
			template.setSubject(mailTemplate.getSubject());
			sender.send(Arrays.asList(mailTemplate.getRecipients().split(",")), template);
		}  catch (MessagingException e) {
			e.printStackTrace();
		}  catch (Exception e) {
			e.printStackTrace();
		}
    }
	
}
