package com.pavell.rickAndMortyApi.service.impl;

import com.pavell.rickAndMortyApi.entity.Role;
import com.pavell.rickAndMortyApi.repo.RoleRepo;
import com.pavell.rickAndMortyApi.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepo roleJpaRepository;

    @Override
    public Role save(Role roleEntity) {
        log.info("Saving role {} to the database", roleEntity.getName());
        return roleJpaRepository.save(roleEntity);
    }


}
