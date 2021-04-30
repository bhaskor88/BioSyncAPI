package com.bohniman.api.biosynchronicity.payload.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private String jwtToken;
    private String userName;
    private Long userId;
    List<String> roles;
}
