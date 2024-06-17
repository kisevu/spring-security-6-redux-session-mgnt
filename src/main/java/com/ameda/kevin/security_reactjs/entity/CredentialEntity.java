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

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Table(name = "credentials")
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class CredentialEntity extends Auditable{
    private String password;
    @OneToOne(targetEntity = UserEntity.class,fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("user_id")
    private UserEntity userEntity;

    public CredentialEntity(UserEntity userEntity,String password){
        this.userEntity = userEntity;
        this.password = password;
    }

}
