package com.ameda.kevin.security_reactjs.service;/*
*
@author ameda
@project security-reactjs
*
*/

import com.ameda.kevin.security_reactjs.entity.ConfirmationEntity;
import com.ameda.kevin.security_reactjs.entity.CredentialEntity;
import com.ameda.kevin.security_reactjs.entity.RoleEntity;
import com.ameda.kevin.security_reactjs.entity.UserEntity;
import com.ameda.kevin.security_reactjs.enumeration.Authority;
import com.ameda.kevin.security_reactjs.enumeration.EventType;
import com.ameda.kevin.security_reactjs.events.UserEvent;
import com.ameda.kevin.security_reactjs.exceptions.APIException;
import com.ameda.kevin.security_reactjs.repository.ConfirmationRepository;
import com.ameda.kevin.security_reactjs.repository.CredentialRepository;
import com.ameda.kevin.security_reactjs.repository.RoleRepository;
import com.ameda.kevin.security_reactjs.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.ameda.kevin.security_reactjs.utils.UserUtils.createUserEntity;

@Service
@Transactional(rollbackOn = Exception.class)
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
private final UserRepository userRepository;
private final RoleRepository roleRepository;
private final CredentialRepository credentialRepository;
private final ConfirmationRepository confirmationRepository;
//private final PasswordEncoder bCryptPasswordEncoder;
private final ApplicationEventPublisher publisher;

    @Override
    public void createUser(String firstName, String lastName, String email, String password) {
       var userEntity = userRepository.save(createNewUser(firstName,lastName,email));
        CredentialEntity credentialEntity = new CredentialEntity(userEntity,
                password);
        credentialRepository.save(credentialEntity);
        var confirmationEntity = new ConfirmationEntity(userEntity);
        confirmationRepository.save(confirmationEntity);
        publisher.publishEvent(new UserEvent(userEntity,
                EventType.REGISTRATION, Map.of("key",confirmationEntity.getKey())));
    }

    @Override
    public RoleEntity getRoleName(String name) {
        var role = roleRepository.findByNameIgnoreCase(name);
        return role.orElseThrow(()->new APIException("role not found"));
    }


    private UserEntity createNewUser(String firstName,String lastName,String email){
        var role = getRoleName(Authority.USER.name());
        return  createUserEntity(firstName,lastName,email,role);
    }
}
