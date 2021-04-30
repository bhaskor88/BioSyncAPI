package com.bohniman.api.biosynchronicity.service;

import com.bohniman.api.biosynchronicity.model.TransTestResult;
import com.bohniman.api.biosynchronicity.payload.response.JsonResponse;

import org.springframework.stereotype.Service;

@Service
public class TestService {

    public JsonResponse createTest(TransTestResult testResult, Long userId) {
        JsonResponse res = new JsonResponse();



        return res;
    }
    
}
