package com.pavell.rickAndMortyApi.repo;

import com.pavell.rickAndMortyApi.entity.Location;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface LocationRepo
        extends PagingAndSortingRepository<Location, Long>, JpaSpecificationExecutor<Location> {



    public Optional<Location> findByName(String name);

}
