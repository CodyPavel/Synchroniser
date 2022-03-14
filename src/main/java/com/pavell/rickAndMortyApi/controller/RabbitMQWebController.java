package com.pavell.rickAndMortyApi.controller;

import com.pavell.rickAndMortyApi.entity.Character;
import com.pavell.rickAndMortyApi.service.impl.RabbitMQSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rabbitmq")
public class RabbitMQWebController {
    @Autowired
    RabbitMQSender rabbitMQSender;

    @GetMapping(value = "/producer")
    public String producer() {

        Character character = new Character();
        character.setName("name");

        rabbitMQSender.send(character);

        return "Message sent to the RabbitMQ JavaInUse Successfully";
    }

}