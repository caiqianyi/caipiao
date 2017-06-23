package com.ct.soa.amqp.core;

import org.springframework.context.annotation.Configuration;

@Configuration
public class DirectRabbitConfig {

    public final static String QueueFoo = "foo";

    
    public final static String JOB_QUEUE_A="A";
    
    public final static String AlarmMailQueue = "alarm.mail.queue";
}
