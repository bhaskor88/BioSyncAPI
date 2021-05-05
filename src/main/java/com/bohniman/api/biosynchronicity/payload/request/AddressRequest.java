package com.bohniman.api.biosynchronicity.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequest {

    // TODO: Add appropriate validations

    @NotNull(message = "User Id cannot be empty")
    private Long userId;

    private String address;
    private String city;
    private String state;
    private String zip;

    @NotBlank(message = "Security Token is required")
    @Size(min = 16, max = 16, message = "Security Token should be 16 character long")
    private String token;

}
