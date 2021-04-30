package com.bohniman.api.biosynchronicity.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonResponse {
    
    private Boolean result = false;
    private Object payload;
    private String message;

}
