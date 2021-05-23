package com.bohniman.api.biosynchronicity.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransFamilyMember extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First Name cannot be blank")
    @Size(min = 3, max = 255)
    @Pattern(regexp = "(^[ a-zA-Z]+$)", message = "Only Alphabets and Spaces are Allowed")
    private String firstName;

    @NotBlank(message = "Last Name cannot be blank")
    @Size(min = 3, max = 255)
    @Pattern(regexp = "(^[ a-zA-Z]+$)", message = "Only Alphabets and Spaces are Allowed")
    private String lastName;

    // @NotNull(message = "Age cannot be empty")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="dd-MM-yyyy")
    private Date dob;

    // @NotBlank(message = "Blood Group cannot be blank")
    @Size(min = 1, max = 3)
    @Pattern(regexp = "(^(A|B|AB|O)[+-]$)", message = "Allowed Values are A,B,O,AB followed by +/-")
    private String bloodGroup;

    @NotBlank(message = "Mobile Number cannot be blank")
    // @Pattern(regexp = "(^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$)", message = "Allowed Characters are Numbers, Space, (+/-)")
    private String mobileNumber;

    @NotBlank(message = "Email cannot be blank")
    @Size(min = 3, max = 255)
    @Pattern(regexp = "(^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:.[a-zA-Z0-9-]+)*$)", message = "Invalid email provided")
    private String email;

    // TODO: Check and Apply Not Blank validation if applicable
    private String address;
    private String city;
    private String state;
    private String zip;

    // @NotBlank(message = "Gender cannot be blank")
    @Size(min = 3, max = 255)
    @Pattern(regexp = "(^(Male|Female)$)", message = "Only (Male/Female) values allowed")
    private String gender;
    
    private String ethnicity;
    private String race;
    private String primaryUse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_user_id")
    private MasterUser masterUser;

    @Column(nullable = false, columnDefinition = "tinyint(1) default 1")
    private boolean isPrimary;

    @JsonIgnore
    @OneToMany(mappedBy = "familyMember")
    private Set<TransTestResult> testResults;
}
