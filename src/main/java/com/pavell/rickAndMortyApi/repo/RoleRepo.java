package com.pavell.rickAndMortyApi.repo;

import com.pavell.rickAndMortyApi.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Long> {

    Role findByName(String name);
}
