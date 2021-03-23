package com.bohniman.api.biosynchronicity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "auth_roles")
@Data
@NoArgsConstructor
public class MasterRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roleId;

    @Size(max = 30)
    @Column(unique = true, updatable = false)
    private String roleName;

    @Size(max = 300)
    private String description;

    @Column(nullable = false, columnDefinition = "tinyint(1) default 1")
    private String isEnable;
}
