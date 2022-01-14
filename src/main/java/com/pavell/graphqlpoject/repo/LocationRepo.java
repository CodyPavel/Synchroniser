package com.pavell.graphqlpoject.repo;

import com.pavell.graphqlpoject.entity.Location;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface LocationRepo extends CrudRepository<Location, Long> {

    @Query(value = "select max(id) from location", nativeQuery = true)
    public Long getMaxId();

}
