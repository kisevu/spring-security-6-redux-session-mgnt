package com.ameda.kevin.security_reactjs.repository;
/*
*
@author ameda
@project security-reactjs
*
*/

import com.ameda.kevin.security_reactjs.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity,Long> {
    Optional<RoleEntity> findByNameIgnoreCase(String name);
}
