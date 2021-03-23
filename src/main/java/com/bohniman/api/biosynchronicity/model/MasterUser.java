package com.bohniman.api.biosynchronicity.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "auth_users")
@Data
@NoArgsConstructor
public class MasterUser extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Size(min = 3, max = 255)
    @Pattern(regexp = "(^[ a-zA-Z]+$)", message = "Name contains alphabets only")
    private String name;

    @Column(unique = true, updatable = false)
    @Size(max = 30)
    private String username;

    @JsonIgnore
    @Size(max = 255)
    private String password;

    @Column(unique = true)
    @Email(message = "Email should be valid")
    @Size(max = 255)
    private String email;

    @Pattern(regexp = "(^[0-9]{10})", message = "Mobile no. must contains 10 digits")
    private String mobileNo;

    @Column(nullable = false, columnDefinition = "tinyint(1) default 1")
    private boolean isEnable;

    @Column(nullable = false, columnDefinition = "tinyint(1) default 1")
    private boolean isAccountNotExpired;

    @Column(nullable = false, columnDefinition = "tinyint(1) default 1")
    private boolean isCredentialsNotExpired;

    @Column(nullable = false, columnDefinition = "tinyint(1) default 1")
    private boolean isAccountNotLocked;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "auth_user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<MasterRole> roles;
}
