package com.bohniman.api.biosynchronicity.payload.request;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequest {

    @NotBlank(message = "First Name cannot be blank")
    @Size(min = 3, max = 255)
    @Pattern(regexp = "(^[ a-zA-Z]+$)", message = "Only Alphabets and Spaces are Allowed")
    private String firstName;

    @NotBlank(message = "Last Name cannot be blank")
    @Size(min = 3, max = 255)
    @Pattern(regexp = "(^[ a-zA-Z]+$)", message = "Only Alphabets and Spaces are Allowed")
    private String lastName;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date dob;

    @NotBlank(message = "Mobile Number cannot be blank")
    @Pattern(regexp = "(^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$)", message = "Allowed Characters are Numbers, Space, (+/-)")
    private String mobileNumber;

    @NotNull(message = "User Id cannot be empty")
    private Long userId;

    @NotBlank(message = "Security Token is required")
    @Size(min = 16, max = 16, message = "Security Token should be 16 character long")
    private String token;
}
