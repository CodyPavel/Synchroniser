package com.pavell.graphqlpoject.repo;

import com.pavell.graphqlpoject.entity.Episode;
import org.springframework.data.repository.CrudRepository;

public interface EpisodeRepo extends CrudRepository<Episode, Long> {
}
