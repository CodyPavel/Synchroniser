package com.pavell.rickAndMortyApi;

import com.pavell.rickAndMortyApi.entity.Episode;
import com.pavell.rickAndMortyApi.entity.Location;
import com.pavell.rickAndMortyApi.entity.character.Character;
import com.pavell.rickAndMortyApi.model.character.PageCharacter;
import com.pavell.rickAndMortyApi.model.episode.PageEpisode;
import com.pavell.rickAndMortyApi.model.episode.Result;
import com.pavell.rickAndMortyApi.model.location.PageLocation;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

            HashMap<String, String> urls = new HashMap<>();

            urls.put("characters", "https://rickandmortyapi.com/api/character");
            urls.put("locations", "https://rickandmortyapi.com/api/location");
            urls.put("episodes", "https://rickandmortyapi.com/api/episode");

            urls.forEach((resourcesName, url) -> {
                switch (resourcesName) {
                    case "characters":
                        parseAndSaveCharacters(locationService, characterService, restTemplate, urls.get(resourcesName));
                        break;
                    case "locations":
                        parseAndSaveLocations(locationService, restTemplate, urls.get(resourcesName));
                        break;
                    case "episodes":
                        parseAndSaveEpisodes(episodeService, restTemplate, urls.get(resourcesName));
                        break;
                    default:

                }
            });
          int intForTest = 0;
        };
    }

    private void parseAndSaveCharacters(LocationService locationService, CharacterService characterService, RestTemplate restTemplate, String url) {
        PageCharacter pageCharacter = restTemplate.getForObject(url, PageCharacter.class);

        List<PageCharacter> pageCharacterList = new ArrayList<>();
        while (true) {
            pageCharacterList.add(pageCharacter);
            pageCharacter = restTemplate.getForObject(pageCharacter.getInfo().getNext(), PageCharacter.class);
            if (pageCharacter.getInfo().getNext() == null) {
                pageCharacterList.add(pageCharacter);
                break;
            }
        }

        pageCharacterList.forEach(pageCharacterElement -> {
            List<com.pavell.rickAndMortyApi.model.character.Result> results = pageCharacterElement.getResults();
            results.forEach(result -> {
                Character character = modelMapper().map(result, Character.class);
                Long locationMaxId = locationService.getMaxId();
                character.getLocation().setId(locationMaxId == null ? 1 : locationMaxId + 1);
                characterService.save(character);
            });
        });
    }

    private void parseAndSaveLocations(LocationService locationService, RestTemplate restTemplate, String url) {
        PageLocation pageLocation = restTemplate.getForObject(url, PageLocation.class);

        List<PageLocation> pageLocationList = new ArrayList<>();
        while (true) {
            pageLocationList.add(pageLocation);
            pageLocation = restTemplate.getForObject(pageLocation.getInfo().getNext(), PageLocation.class);
            if (pageLocation.getInfo().getNext() == null) {
                pageLocationList.add(pageLocation);
                break;
            }
        }

        ArrayList<Location> locations = new ArrayList<Location>();
        pageLocationList.forEach(pageLocationElement -> {
            List<com.pavell.rickAndMortyApi.model.location.Result> results = pageLocationElement.getResults();
            results.forEach(result -> {
                Location location = modelMapper().map(result, Location.class);
                locations.add(location);
            });
        });

        locationService.save(locations);
    }

    private void parseAndSaveEpisodes(EpisodeService episodeService, RestTemplate restTemplate, String url) {
        PageEpisode pageEpisode = restTemplate.getForObject(url, PageEpisode.class);

        List<PageEpisode> pageEpisodeList = new ArrayList<>();
        while (true) {
            pageEpisodeList.add(pageEpisode);
            pageEpisode = restTemplate.getForObject(pageEpisode.getInfo().getNext(), PageEpisode.class);
            if (pageEpisode.getInfo().getNext() == null) {
                pageEpisodeList.add(pageEpisode);
                break;
            }
        }

        ArrayList<Episode> episodes = new ArrayList<Episode>();
        pageEpisodeList.forEach(pageEpisodeElement -> {
            List<Result> results = pageEpisodeElement.getResults();
            results.forEach(result -> {
                Episode episode = modelMapper().map(result, Episode.class);
                episodes.add(episode);
            });
        });

        episodeService.save(episodes);
    }
}
