package com.pavell.rickAndMortyApi.service.impl;

import com.pavell.rickAndMortyApi.entity.RoleEntity;
import com.pavell.rickAndMortyApi.repo.RoleJpaRepository;
import com.pavell.rickAndMortyApi.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleJpaRepository roleJpaRepository;

    @Override
    public RoleEntity save(RoleEntity roleEntity) {
        log.info("Saving role {} to the database", roleEntity.getName());
        return roleJpaRepository.save(roleEntity);
    }


}
