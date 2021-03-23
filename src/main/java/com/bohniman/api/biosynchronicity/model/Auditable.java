package com.bohniman.api.biosynchronicity.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import static javax.persistence.TemporalType.TIMESTAMP;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {

    @CreatedBy
    @Column(updatable = false)
    protected String createdBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    @Column(updatable = false)
    protected Date createdOn;

    @LastModifiedBy
    protected String updatedBy;

    @LastModifiedDate
    @Temporal(TIMESTAMP)
    protected Date updatedOn;

    public Auditable() {
    }

}