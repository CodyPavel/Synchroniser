package com.pavell.rickAndMortyApi.repo;

import com.pavell.rickAndMortyApi.entity.character.Origin;
import org.springframework.data.repository.CrudRepository;

public interface OriginRepo extends CrudRepository<Origin, Long> {
}
