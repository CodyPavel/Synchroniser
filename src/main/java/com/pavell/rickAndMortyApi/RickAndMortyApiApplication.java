package com.pavell.rickAndMortyApi;

import com.pavell.rickAndMortyApi.service.CharacterService;
import com.pavell.rickAndMortyApi.service.EpisodeService;
import com.pavell.rickAndMortyApi.service.LocationService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;

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
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    CommandLineRunner runner(EpisodeService episodeService,
                             CharacterService characterService,
                             LocationService locationService,
                             RestTemplate restTemplate) {
        return args -> {

            LinkedHashMap<String, String> urls = new LinkedHashMap<>();

            urls.put("locations", "https://rickandmortyapi.com/api/location");
            urls.put("episodes", "https://rickandmortyapi.com/api/episode");
            urls.put("characters", "https://rickandmortyapi.com/api/character");

            urls.forEach((resourcesName, url) -> {
                switch (resourcesName) {
                    case "characters":
                        characterService.parseAndSaveAll(restTemplate, urls.get(resourcesName));
                        break;
                    case "locations":
                        locationService.parseAndSaveAll(restTemplate, urls.get(resourcesName));
                        break;
                    case "episodes":
                        episodeService.parseAndSaveAll(restTemplate, urls.get(resourcesName));
                        break;
                    default:
                }
            });
        };
    }

}
