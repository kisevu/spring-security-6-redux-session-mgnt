package com.ameda.kevin.security_reactjs.service.impl;/*
*
@author ameda
@project security-reactjs
*
*/

import com.ameda.kevin.security_reactjs.cache.CacheStore;
import com.ameda.kevin.security_reactjs.domain.RequestContext;
import com.ameda.kevin.security_reactjs.domain.dto.User;
import com.ameda.kevin.security_reactjs.entity.ConfirmationEntity;
import com.ameda.kevin.security_reactjs.entity.CredentialEntity;
import com.ameda.kevin.security_reactjs.entity.RoleEntity;
import com.ameda.kevin.security_reactjs.entity.UserEntity;
import com.ameda.kevin.security_reactjs.enumeration.Authority;
import com.ameda.kevin.security_reactjs.enumeration.EventType;
import com.ameda.kevin.security_reactjs.enumeration.LoginType;
import com.ameda.kevin.security_reactjs.events.UserEvent;
import com.ameda.kevin.security_reactjs.exceptions.APIException;
import com.ameda.kevin.security_reactjs.repository.ConfirmationRepository;
import com.ameda.kevin.security_reactjs.repository.CredentialRepository;
import com.ameda.kevin.security_reactjs.repository.RoleRepository;
import com.ameda.kevin.security_reactjs.repository.UserRepository;
import com.ameda.kevin.security_reactjs.service.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

import static com.ameda.kevin.security_reactjs.utils.UserUtils.createUserEntity;
import static com.ameda.kevin.security_reactjs.utils.UserUtils.fromUserEntity;

@Service
@Transactional(rollbackOn = Exception.class)
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
private final UserRepository userRepository;
private final RoleRepository roleRepository;
private final CredentialRepository credentialRepository;
private final ConfirmationRepository confirmationRepository;
//private final PasswordEncoder bCryptPasswordEncoder;
private final ApplicationEventPublisher publisher;
private final CacheStore<String,Integer> userCache;
private final ObjectMapper objectMapper;

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

    @Override
    public void verifyAccountToken(String token) {
        var confirmationEntity = getUserConfirmation(token);
        UserEntity userEntity = getUserEntityByEmail(confirmationEntity.getUserEntity().getEmail());
        userEntity.setEnabled(true); // enabling /active user account
        userRepository.save(userEntity);
        confirmationRepository.delete(confirmationEntity);
    }

    /*
  @author ameda
  */
    /*
    * in the below method we're checking the login counts and type,
    * if it exceeds five the account is temporarily held for like 15minutes straight
    * if it is an attempt we need to check if the user is already in the cache
    *  and if they're in the cache then we need setting the login attempt to 0 and claim the
    * account to have been locked,
    * else we need to increase their attempt.
    * Regardless of the loginType we are updating the required fields in DB
    * */
    @Override
    public void updateLoginAttempt(String email, LoginType loginType) {
        var userEntity = getUserEntityByEmail(email);
        RequestContext.setUserId(userEntity.getId());
        switch (loginType){
            case LOGIN_ATTEMPT -> {
                if(userCache.get(userEntity.getEmail())==null){
                    //first time login
                    userEntity.setLoginAttempts(0);
                    userEntity.setAccountNonLocked(true);
                }
                //not first time login
                userEntity.setLoginAttempts(userEntity.getLoginAttempts() + 1);
                userCache.put(userEntity.getEmail(),userEntity.getLoginAttempts());
                if(userCache.get(userEntity.getEmail())>5){
                    userEntity.setAccountNonLocked(false);
                    //account is locked
                }
            }
            case LOGIN_SUCCESS -> {
                userEntity.setAccountNonLocked(true);
                userEntity.setLoginAttempts(0);
                userEntity.setLastLogin(LocalDateTime.now());
                userCache.evict(userEntity.getEmail());
            }
        }
        userRepository.save(userEntity);
    }

    @Override
    public User getUserByUserId(String userId) {
        var userEntity = userRepository.findUserByUserId(userId).orElseThrow(()-> new APIException("User not found"));
        return fromUserEntity(userEntity,userEntity.getRole(),getUserCredentialById(userEntity.getId()));
    }

    @Override
    public User getUserByEmail(String email) {
        var  userEntity = getUserEntityByEmail(email);
        return fromUserEntity(userEntity,userEntity.getRole(),getUserCredentialById(userEntity.getId()));
    }



    @Override
    public CredentialEntity getUserCredentialById(Long userId) {
        var credentialById = credentialRepository.getCredentialsByUserEntityId(userId);
        return credentialById.orElseThrow(()-> new APIException("unable to find user credentials."));
    }

    private UserEntity getUserEntityByEmail(String email) {
        var userByEmail = userRepository.findByEmailIgnoreCase(email);
        return userByEmail
                .orElseThrow(
                        ()->new APIException("user with passed email could be found."));
    }

    private  ConfirmationEntity getUserConfirmation(String token) {
        return confirmationRepository.findByKey(token)
                .orElseThrow(
                        ()->new APIException("Confirmation entity with passed token or key  could not be found.")
                );
    }


    private UserEntity createNewUser(String firstName,String lastName,String email){
        var role = getRoleName(Authority.USER.name());
        return  createUserEntity(firstName,lastName,email,role);
    }
}
