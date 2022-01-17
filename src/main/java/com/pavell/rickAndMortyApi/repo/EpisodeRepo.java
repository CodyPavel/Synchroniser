package com.pavell.rickAndMortyApi.repo;

import com.pavell.rickAndMortyApi.entity.Episode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EpisodeRepo extends PagingAndSortingRepository<Episode, Long> {

    @Query(value = "select count(*) from episode", nativeQuery = true)
    public Integer getCount();


}
