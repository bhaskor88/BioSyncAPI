package com.bohniman.api.biosynchronicity.model;

import java.util.Date;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "auth_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MasterUser extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 255)
    // @Pattern(regexp = "(^[ a-zA-Z0-9]+$)", message = "Only Alphabets and Spaces are Allowed")
    @Pattern(regexp = "(^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:.[a-zA-Z0-9-]+)*$)", message = "Invalid email provided")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 3, max = 255)
    private String password;

    @NotBlank(message = "Mobile Number cannot be blank")
    @Pattern(regexp = "(^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$)", message = "Allowed Characters are Numbers, Space, (+/-)")
    private String mobileNumber;

    @NotBlank(message = "Email cannot be blank")
    @Size(min = 3, max = 255)
    @Pattern(regexp = "(^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:.[a-zA-Z0-9-]+)*$)", message = "Invalid email provided")
    private String email;

    private String notoficationToken;

    @Column(nullable = false, columnDefinition = "tinyint(1) default 0")
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

    @JsonIgnore
    @OneToMany(mappedBy = "masterUser")
    private Set<TransFamilyMember> familyMembers;
}
