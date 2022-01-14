package com.pavell.rickAndMortyApi.repo;

import com.pavell.rickAndMortyApi.entity.Location;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface LocationRepo extends CrudRepository<Location, Long> {
    // TODO: Re-wright to HQL
    @Query(value = "select max(id) from location", nativeQuery = true)
    public Long getMaxId();


}
