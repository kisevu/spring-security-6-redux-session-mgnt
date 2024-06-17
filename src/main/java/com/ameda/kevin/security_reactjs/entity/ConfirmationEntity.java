package com.ameda.kevin.security_reactjs.entity;/*
*
@author ameda
@project security-reactjs
*
*/

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.UUID;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Table(name = "confirmations")
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ConfirmationEntity extends Auditable{
    private String key;
    @OneToOne(targetEntity = UserEntity.class,fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("user_id")
    private UserEntity userEntity;
    /*
    * generation of key
    * in constructor
    * */
    public ConfirmationEntity(UserEntity userEntity){
        this.userEntity = userEntity;
        this.key = UUID.randomUUID().toString();
    }

}
