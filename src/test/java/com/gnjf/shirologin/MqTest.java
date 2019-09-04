package com.gnjf.shirologin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=ShirologinApplication.class)
public class MqTest {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSend1() {

        rabbitTemplate.convertAndSend("gnjf", "我发送数据了");
    }

}
