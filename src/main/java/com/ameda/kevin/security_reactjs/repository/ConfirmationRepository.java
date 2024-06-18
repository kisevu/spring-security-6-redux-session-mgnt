package com.ameda.kevin.security_reactjs.repository;
/*
*
@author ameda
@project security-reactjs
*
*/

import com.ameda.kevin.security_reactjs.entity.ConfirmationEntity;
import com.ameda.kevin.security_reactjs.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationRepository extends JpaRepository<ConfirmationEntity,Long> {
    Optional<ConfirmationEntity> findByKey(String key);
    Optional<ConfirmationEntity> findByUserEntity(UserEntity userEntity);
}
