package com.pavell.rickAndMortyApi.rabbitmq;

import com.pavell.rickAndMortyApi.entity.Character;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RabbitMQConsumer {

    @RabbitListener(queues = "${javainuse.rabbitmq.queue}")
    public void receivedMessage(Character character) {
        log.info("Recieved Message From RabbitMQ: Character with name=" + character.getName());
    }
}
