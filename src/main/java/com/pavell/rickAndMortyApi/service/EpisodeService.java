package com.pavell.rickAndMortyApi.service;

import com.pavell.rickAndMortyApi.entity.Episode;
import com.pavell.rickAndMortyApi.repo.EpisodeRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EpisodeService {

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

}
