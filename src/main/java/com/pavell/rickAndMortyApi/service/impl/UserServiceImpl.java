package com.pavell.rickAndMortyApi.service.impl;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.pavell.rickAndMortyApi.entity.Role;
import com.pavell.rickAndMortyApi.entity.User;
import com.pavell.rickAndMortyApi.repo.RoleRepo;
import com.pavell.rickAndMortyApi.repo.UserRepo;
import com.pavell.rickAndMortyApi.service.UserService;
import com.pavell.rickAndMortyApi.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final String USER_NOT_FOUND_MESSAGE = "User with username %s not found";

    private final UserRepo userJpaRepository;
    private final RoleRepo roleJpaRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userJpaRepository.findByUsername(username);
        if (user == null) {
            String message = String.format(USER_NOT_FOUND_MESSAGE, username);
            log.error(message);
            throw new UsernameNotFoundException(message);
        } else {
            log.debug("User found in the database: {}", username);
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            user.getRoles().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            });
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
        }
    }

    @Override
    public User save(User user) {
        log.info("Saving user {} to the database", user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userJpaRepository.save(user);
    }


    @Override
    public User addRoleToUser(String username, String roleName) {
        log.info("Adding role {} to user {}", roleName, username);
        User userEntity = userJpaRepository.findByUsername(username);
        Role roleEntity = roleJpaRepository.findByName(roleName);
        userEntity.getRoles().add(roleEntity);
        return userEntity;
    }

    @Transactional(readOnly = true)
    @Override
    public User findByUsername(String username) {
        log.info("Retrieving user {}", username);
        return userJpaRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        log.info("Retrieving all users");
        return userJpaRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Map<String, String> refreshToken(String authorizationHeader, String issuer) throws BadJOSEException,
            ParseException, JOSEException {

        String refreshToken = authorizationHeader.substring("Bearer ".length());
        UsernamePasswordAuthenticationToken authenticationToken = JwtUtil.parseToken(refreshToken);
        String username = authenticationToken.getName();
        User userEntity = findByUsername(username);
        List<String> roles = userEntity.getRoles().stream().map(Role::getName).collect(Collectors.toList());
        String accessToken = JwtUtil.createAccessToken(username, issuer, roles);
        return Map.of("access_token", accessToken, "refresh_token", refreshToken);
    }

}
