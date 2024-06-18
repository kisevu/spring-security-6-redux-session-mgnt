package com.ameda.kevin.security_reactjs.repository;
/*
*
@author ameda
@project security-reactjs
*
*/

import com.ameda.kevin.security_reactjs.entity.CredentialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CredentialRepository extends JpaRepository<CredentialEntity,Long> {
    Optional<CredentialEntity> getCredentialsByUserEntityId(Long userId);
}
