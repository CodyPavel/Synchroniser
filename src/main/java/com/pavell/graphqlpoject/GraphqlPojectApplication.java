package com.pavell.graphqlpoject;

import com.pavell.graphqlpoject.entity.Episode;
import com.pavell.graphqlpoject.model.episode.PageEpisode;
import com.pavell.graphqlpoject.model.episode.Result;
import com.pavell.graphqlpoject.service.EpisodeService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class GraphqlPojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraphqlPojectApplication.class, args);
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
    CommandLineRunner runner(EpisodeService episodeService, RestTemplate restTemplate) {
        return args -> {

            PageEpisode pageEpisode = restTemplate.getForObject("https://rickandmortyapi.com/api/episode", PageEpisode.class);

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
        };
    }
}
