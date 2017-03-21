package com.ct.core.amqp.listener.topic;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.ct.App;
import com.ct.soa.amqp.core.TopicRabbitConfig;

@Component 
@RabbitListener(queues = TopicRabbitConfig.Messages)
public class MessageAllListener {
	
	@Bean
    public Queue queueMessages() {
        return new Queue(TopicRabbitConfig.Messages);
    }
	
	@Bean
    Binding bindingExchangeMessages(Queue queueMessages, TopicExchange topicExchange) {
        return BindingBuilder.bind(queueMessages).to(topicExchange).with(TopicRabbitConfig.Messages.split("\\.")[0]+".#");
    }
	
	@RabbitHandler
    public void receive(String data) {
        System.out.println("Received "+App.ListenerName+" Topic Queues Message Data<" + data + ">");
    }
}
