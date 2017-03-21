package com.ct.core.amqp.listener.direct;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.ct.App;
import com.ct.soa.amqp.core.DirectRabbitConfig;

@Component
@RabbitListener(queues = DirectRabbitConfig.QueueFoo)
public class FooListener {
	
	@Bean
    public Queue queueFoo() {
        return new Queue(DirectRabbitConfig.QueueFoo);
    }

    @Bean
    Binding bindingDirectExchangeFoo(Queue queueFoo, DirectExchange directExchange) {
        return BindingBuilder.bind(queueFoo).to(directExchange).with(DirectRabbitConfig.QueueFoo);
    }
	
	@RabbitHandler
    public void receive(String data) {
        System.out.println("Received "+App.ListenerName+" Direct Queue Foo Data<" + data + ">");
    }
}
