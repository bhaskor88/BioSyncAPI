package com.bohniman.api.biosynchronicity.util;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ConfigurationProperties(prefix = "crypto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CryptoProperties {
    private String secretCode;
    private String initVector;
}
