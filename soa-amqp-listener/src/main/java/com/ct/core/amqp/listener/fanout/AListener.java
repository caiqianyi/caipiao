package com.ct.core.amqp.listener.fanout;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.ct.App;
import com.ct.soa.amqp.core.FanoutRabbitConfig;

@Component
@RabbitListener(queues = FanoutRabbitConfig.A)
public class AListener {
	
	@Bean
    public Queue fanoutAMessage() {
        return new Queue(FanoutRabbitConfig.A);
    }
    
    @Bean
    Binding bindingExchangeA(Queue fanoutAMessage,FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutAMessage).to(fanoutExchange);
    }
    
	@RabbitHandler
    public void receive(String data) {
        System.out.println("Received "+App.ListenerName+" Fanout Queue A Data<" + data + ">");
    }
}
