package com.pavell.rickAndMortyApi.service.impl;

import com.pavell.rickAndMortyApi.entity.Character;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Value("${javainuse.rabbitmq.exchange}")
    private String exchange;

    @Value("${javainuse.rabbitmq.routingkey}")
    private String routingkey;

    public void send(Character character) {
        rabbitTemplate.convertAndSend(exchange, routingkey, character);
        System.out.println("Send msg = " + character);
    }
}