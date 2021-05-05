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
public class AddtionalRequest {

    // TODO: Add appropriate validations

    @NotNull(message = "User Id cannot be empty")
    private Long userId;

    @Size(min = 3, max = 255)
    @Pattern(regexp = "(^(Male|Female)$)", message = "Only (Male/Female) values allowed")
    private String gender;

    private String ethnicity;
    private String race;
    private String primaryUse;

    @NotBlank(message = "Security Token is required")
    @Size(min = 16, max = 16, message = "Security Token should be 16 character long")
    private String token;
}
