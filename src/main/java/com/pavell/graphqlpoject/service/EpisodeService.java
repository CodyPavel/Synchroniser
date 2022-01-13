package com.pavell.graphqlpoject.service;

import com.pavell.graphqlpoject.entity.Episode;
import com.pavell.graphqlpoject.repo.EpisodeRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Vector;

@Service
public class EpisodeService {

    private EpisodeRepo episodeRepo;

    public EpisodeService(EpisodeRepo episodeRepo) {
        this.episodeRepo = episodeRepo;
    }

    public Iterable<Episode> list() {
        return episodeRepo.findAll();
    }

    public Episode save(Episode episode) {
        return episodeRepo.save(episode);
    }

    public void save(List<Episode> episodes) {
        episodeRepo.saveAll(episodes);
    }

}
