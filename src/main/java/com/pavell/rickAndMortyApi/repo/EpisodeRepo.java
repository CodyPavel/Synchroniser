package com.pavell.rickAndMortyApi.repo;

import com.pavell.rickAndMortyApi.entity.Episode;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EpisodeRepo
        extends PagingAndSortingRepository<Episode, Long>, JpaSpecificationExecutor<Episode> {

    @Query(value = "select count(*) from episode", nativeQuery = true)
    public Integer getCount();

    public Optional<Episode> findByUrl(String url);

}
