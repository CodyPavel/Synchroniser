package com.pavell.graphqlpoject.repo;

import com.pavell.graphqlpoject.entity.character.Character;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CharacterRepo extends CrudRepository<Character, Long> {

}
