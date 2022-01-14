package com.pavell.rickAndMortyApi.service;

import com.pavell.rickAndMortyApi.entity.Episode;
import com.pavell.rickAndMortyApi.model.episode.PageEpisode;
import com.pavell.rickAndMortyApi.model.episode.Result;
import com.pavell.rickAndMortyApi.repo.EpisodeRepo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EpisodeService {

    private ModelMapper modelMapper = new ModelMapper();

    private EpisodeRepo episodeRepo;

    public Iterable<Episode> list() {
        return episodeRepo.findAll();
    }

    public Episode getById(Long id) {
        return episodeRepo.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public EpisodeService(EpisodeRepo episodeRepo) {
        this.episodeRepo = episodeRepo;
    }

    public Episode save(Episode episode) {
        return episodeRepo.save(episode);
    }

    public void save(List<Episode> episodes) {
        episodeRepo.saveAll(episodes);
    }

    public void parseAndSaveAll(RestTemplate restTemplate, String url) {
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
                Episode episode = modelMapper.map(result, Episode.class);
                episodes.add(episode);
            });
        });

        save(episodes);
    }

}
