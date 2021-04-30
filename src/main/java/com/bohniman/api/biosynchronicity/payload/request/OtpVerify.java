package com.bohniman.api.biosynchronicity.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpVerify {
    
    @NotBlank(message = "Otp is required")
    private String otp;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Mobile Number is required")
    private String mobileNumber;
}
