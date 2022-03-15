package com.pavell.rickAndMortyApi.service.impl;

import com.pavell.rickAndMortyApi.entity.Character;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RabbitMQSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Value("${javainuse.rabbitmq.exchange}")
    private String exchange;

    @Value("${javainuse.rabbitmq.routingkey}")
    private String routingkey;

    public void send(Character character) {
        rabbitTemplate.convertAndSend(exchange, routingkey, character);
        log.info("Send msg RabbitMQ Character name= " + character.getName());
        System.out.println();
    }
}