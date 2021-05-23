package com.bohniman.api.biosynchronicity.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordRequest {

    @NotNull(message = "User Id cannot be empty")
    private Long userId;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 24, message = "Password can be only between 8 to 24 characters long")
    @Pattern(regexp = "(^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,24}$)", message = "Password must contain one number, one uppercase and one lowe case alphabet, one special character")
    private String password;

    @NotBlank(message = "Confirm Password cannot be blank")
    @Size(min = 8, max = 24, message = "Confirm Password can be only between 8 to 24 characters long")
    @Pattern(regexp = "(^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,24}$)", message = "Confirm Password must contain one number, one uppercase and one lowe case alphabet, one special character")
    private String cpassword;

    @NotBlank(message = "Security Token is required")
    @Size(min = 16, max = 16, message = "Security Token should be 16 character long")
    private String token;

}
