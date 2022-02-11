package com.pavell.rickAndMortyApi.repo;

import com.pavell.rickAndMortyApi.entity.Character;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface CharacterRepo
        extends PagingAndSortingRepository<Character, Long>, JpaSpecificationExecutor<Character> {

    @Query(value = "select * from character c  where c.image = :image", nativeQuery = true)
    public Optional<Character> findByImage(String image);

    public List<Character> findAll();

}
