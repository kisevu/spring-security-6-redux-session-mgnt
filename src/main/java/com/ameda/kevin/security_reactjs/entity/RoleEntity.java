package com.ameda.kevin.security_reactjs.entity;

import com.ameda.kevin.security_reactjs.enumeration.Authority;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

/*
*
@author ameda
@project security-reactjs
*
*/
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Table(name = "roles")
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class RoleEntity extends Auditable{
    private String name;
    private Authority authorities;
}
