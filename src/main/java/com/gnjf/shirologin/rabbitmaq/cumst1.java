package com.gnjf.shirologin.rabbitmaq;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 接收消息
 */
@Component
@RabbitListener(queues="gnjf")
public class cumst1 {

    /**
     * 处理消息
     * @param msg
     */

    @RabbitHandler
    public void handlerMsg(String msg){
        System.out.println("1 接收到消息"+msg);
    }
}
