package com.pavell.rickAndMortyApi;

import com.pavell.rickAndMortyApi.entity.Role;
import com.pavell.rickAndMortyApi.entity.User;
import com.pavell.rickAndMortyApi.service.RoleService;
import com.pavell.rickAndMortyApi.service.UserService;
import com.pavell.rickAndMortyApi.service.impl.CharacterService;
import com.pavell.rickAndMortyApi.service.impl.EpisodeService;
import com.pavell.rickAndMortyApi.service.impl.LocationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@SpringBootApplication
public class RickAndMortyApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RickAndMortyApiApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    CommandLineRunner runner(EpisodeService episodeService,
                             CharacterService characterService,
                             LocationService locationService,
                             UserService userService,
                             RoleService roleService) {

        return args -> {
            roleService.save(new Role(null, "ROLE_USER"));
            roleService.save(new Role(null, "ROLE_ADMIN"));

            userService.save(new User(null, "pavel", "1234", new ArrayList<>()));
            userService.save(new User(null, "dima", "1234", new ArrayList<>()));

            userService.addRoleToUser("pavel", "ROLE_USER");
            userService.addRoleToUser("dima", "ROLE_ADMIN");
            userService.addRoleToUser("dima", "ROLE_USER");

            locationService.loadData();
            episodeService.loadData();
            characterService.loadData();
        };
    }
}
