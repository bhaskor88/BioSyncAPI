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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.validator.constraints.Range;

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

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 3, max = 255)
    @Pattern(regexp = "(^[ a-zA-Z]+$)", message = "Only Alphabets and Spaces are Allowed")
    private String name;

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 255)
    @Pattern(regexp = "(^[ a-zA-Z0-9]+$)", message = "Only Alphabets and Spaces are Allowed")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 3, max = 255)
    private String password;

    // @NotNull(message = "Age cannot be empty")
    @Range(min = 0, max = 130)
    private Long age;

    // @NotBlank(message = "Gender cannot be blank")
    @Size(min = 3, max = 255)
    @Pattern(regexp = "(^(Male|Female)$)", message = "Only (Male/Female) values allowed")
    private String gender;

    // @NotBlank(message = "Blood Group cannot be blank")
    @Size(min = 1, max = 3)
    @Pattern(regexp = "(^(A|B|AB|O)[+-]$)", message = "Allowed Values are A,B,O,AB followed by +/-")
    private String bloodGroup;

    // @NotBlank(message = "Zip Code cannot be blank")
    @Size(min = 3, max = 255)
    @Pattern(regexp = "(^[ .-a-zA-Z0-9]+$)", message = "Allowed Special Characters are (.-)")
    private String zipCode;

    // @NotBlank(message = "City cannot be blank")
    @Size(min = 3, max = 255)
    @Pattern(regexp = "(^[ .,a-zA-Z0-9]+$)", message = "Only Alphabets, Spaces and (.,) are allowed")
    private String city;

    // @NotBlank(message = "State/Province cannot be blank")
    @Size(min = 3, max = 255)
    @Pattern(regexp = "(^[ .,a-zA-Z0-9]+$)", message = "Only Alphabets, Spaces and (.,) are allowed")
    private String stateProvince;

    // @NotBlank(message = "Country cannot be blank")
    @Size(min = 3, max = 255)
    @Pattern(regexp = "(^[ .,a-zA-Z0-9]+$)", message = "Only Alphabets, Spaces and (.,) are allowed")
    private String country;

    @NotBlank(message = "Mobile Number cannot be blank")
    @Pattern(regexp = "(^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$)", message = "Allowed Characters are Numbers, Space, (+/-)")
    private String mobileNumber;

    @NotBlank(message = "Email cannot be blank")
    @Size(min = 3, max = 255)
    @Pattern(regexp = "(^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:.[a-zA-Z0-9-]+)*$)", message = "Invalid email provided")
    private String email;

    private String notoficationToken;

    private String otp;

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

    @JsonIgnore
    @OneToMany(mappedBy = "masterUser")
    private Set<TransFamilyMember> familyMembers;
}
