package com.pavell.rickAndMortyApi.repo;

import com.pavell.rickAndMortyApi.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleJpaRepository extends JpaRepository<RoleEntity, Long> {

    RoleEntity findByName(String name);
}
