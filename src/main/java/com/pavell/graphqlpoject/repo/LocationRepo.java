package com.pavell.graphqlpoject.repo;

import com.pavell.graphqlpoject.entity.Location;
import org.springframework.data.repository.CrudRepository;

public interface LocationRepo extends CrudRepository<Location, Long> {
}
