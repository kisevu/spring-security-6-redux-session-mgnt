package com.ameda.kevin.security_reactjs.repository;
/*
*
@author ameda
@project security-reactjs
*
*/

import com.ameda.kevin.security_reactjs.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity> findByEmailIgnoreCase(String email);
    Optional<UserEntity> findUserByUserId(String userId);
}
