package com.pavell.rickAndMortyApi.repo;

import com.pavell.rickAndMortyApi.entity.Episode;
import org.springframework.data.repository.CrudRepository;

public interface EpisodeRepo extends CrudRepository<Episode, Long> {
}
