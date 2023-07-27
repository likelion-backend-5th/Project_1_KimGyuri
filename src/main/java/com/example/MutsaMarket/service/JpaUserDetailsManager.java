package com.example.MutsaMarket.service;

import com.example.MutsaMarket.entity.user.CustomUserDetails;
import com.example.MutsaMarket.entity.user.UserEntity;
import com.example.MutsaMarket.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
public class JpaUserDetailsManager implements UserDetailsManager {
    private final UserRepository userRepository;
    public JpaUserDetailsManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //회원가입
    @Override
    public void createUser(UserDetails user) {
        log.info("try create user: {}", user.getUsername());
        if (this.userExists(user.getUsername()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        try {
            userRepository.save(((CustomUserDetails) user).newEntity());
        } catch (ClassCastException e) {
            log.error("failed to cast to {}", CustomUserDetails.class);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //회원정보 수정
    @Override
    public void updateUser(UserDetails user) {
    }

    //회원 탈퇴
    @Override
    public void deleteUser(String username) {
    }

    //비밀번호 변경
    @Override
    public void changePassword(String oldPassword, String newPassword) {
    }

    //이미 존재하는 아이디인지 확인
    @Override
    public boolean userExists(String username) {
        log.info("check if user: {} exists", username);
        return this.userRepository.existsByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty())
            throw new UsernameNotFoundException(username);
        return CustomUserDetails.fromEntity(optionalUser.get());
    }
}
