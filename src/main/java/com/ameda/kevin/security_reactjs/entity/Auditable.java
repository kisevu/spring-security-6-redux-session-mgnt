package com.ameda.kevin.security_reactjs.entity;
/*
*
@author ameda
@project security-reactjs
*
*/


import com.ameda.kevin.security_reactjs.domain.RequestContext;
import com.ameda.kevin.security_reactjs.exceptions.APIException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.AlternativeJdkIdGenerator;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt","updatedAt"}, allowGetters = true)
public abstract class Auditable {
    @Id
    @SequenceGenerator(name = "primary_key_seq",sequenceName = "primary_key_seq",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "primary_key_seq")
    @Column(name = "id",updatable = false)
    private Long id;
    private String referenceId = new AlternativeJdkIdGenerator().generateId().toString();
    @NotNull
    private Long createdBy;
    @NotNull
    private Long updatedBy;
    @NotNull
    @Column(name = "created_at",nullable = false,updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;
    @NotNull
    @Column(name = "updated_at",nullable = false)
    @CreatedDate
    private LocalDateTime updatedAt;

    @PrePersist
    public void  beforePersist(){
        var userId = 0L;
//                RequestContext.getUserId();
//        if(userId == null){
//            throw new APIException("cannot persist without user Id in request context.");
//        }
        setCreatedAt(LocalDateTime.now());
        setCreatedBy(userId);
        setUpdatedBy(userId);
        setUpdatedAt(LocalDateTime.now());
    }

    @PreUpdate
    public void  beforeUpdate(){
        var userId = 0L;
//                RequestContext.getUserId();
//        if(userId == null){
//            throw new APIException("cannot update without user Id in request context.");
//        }
        setUpdatedAt(LocalDateTime.now());
        setUpdatedBy(userId);
    }
}
