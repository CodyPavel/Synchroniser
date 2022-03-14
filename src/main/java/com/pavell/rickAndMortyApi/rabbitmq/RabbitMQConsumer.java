package com.pavell.rickAndMortyApi.rabbitmq;

import com.pavell.rickAndMortyApi.entity.Character;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConsumer {

    @RabbitListener(queues = "${javainuse.rabbitmq.queue}")
    public void receivedMessage(Character character){
        System.out.println( "Recieved Message From RabbitMQ: Character with name=" + character.getName());
    }
}
