package com.pavell.rickAndMortyApi.repo;

import com.pavell.rickAndMortyApi.entity.Character;
import org.springframework.data.repository.CrudRepository;

public interface CharacterRepo extends CrudRepository<Character, Long> {

}
