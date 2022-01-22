package com.pavell.rickAndMortyApi.repo;

import com.pavell.rickAndMortyApi.entity.Character;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CharacterRepo
        extends PagingAndSortingRepository<Character, Long>, JpaSpecificationExecutor<Character> {

}
