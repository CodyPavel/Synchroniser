package com.pavell.rickAndMortyApi.repo;

import com.pavell.rickAndMortyApi.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUsername(String username);
}
