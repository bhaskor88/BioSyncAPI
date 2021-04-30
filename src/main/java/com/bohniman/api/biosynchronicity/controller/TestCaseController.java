package com.bohniman.api.biosynchronicity.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.bohniman.api.biosynchronicity.exception.BadRequestException;
import com.bohniman.api.biosynchronicity.model.TransTestResult;
import com.bohniman.api.biosynchronicity.payload.response.JsonResponse;
import com.bohniman.api.biosynchronicity.security.SecurityUtil;
import com.bohniman.api.biosynchronicity.service.TestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestCaseController {
    
    @Autowired
    SecurityUtil securityUtil;

    @Autowired
    TestService testService;

    @RequestMapping(value = "/createTest", method = RequestMethod.POST)
    public ResponseEntity<JsonResponse> createTest(@Valid @RequestBody TransTestResult testResult,
            BindingResult result) {
        Long userId = securityUtil.getCurrentLoggedUserId();
        if (userId == null) {
            throw new BadRequestException("Unable to determine the logged in user");
        }
        // Data sanity check
        if (result.hasErrors()) {
            List<FieldError> errors = result.getFieldErrors();
            List<String> message = new ArrayList<>();
            for (FieldError e : errors) {
                message.add("@" + e.getField() + " : " + e.getDefaultMessage());
            }
            return new ResponseEntity<JsonResponse>(new JsonResponse(false, message, "Data validation error"),
                    HttpStatus.BAD_REQUEST);
        }

        JsonResponse res = testService.createTest(testResult, userId);
        if (!res.getResult()) {
            throw new BadRequestException(res.getMessage());
        } else {
            return ResponseEntity.ok(res);
        }
    }


}
