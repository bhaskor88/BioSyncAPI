package com.bohniman.api.biosynchronicity.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.bohniman.api.biosynchronicity.exception.BadRequestException;
import com.bohniman.api.biosynchronicity.model.TransFamilyMember;
import com.bohniman.api.biosynchronicity.payload.response.JsonResponse;
import com.bohniman.api.biosynchronicity.security.SecurityUtil;
import com.bohniman.api.biosynchronicity.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    SecurityUtil securityUtil;

    @RequestMapping(value = "/addFamilyMember", method = RequestMethod.POST)
    public ResponseEntity<JsonResponse> addFamilyMember(@Valid @RequestBody TransFamilyMember member,
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
        // Trying to register user
        JsonResponse res = userService.addFamilyMember(member, userId);
        if (!res.getResult()) {
            throw new BadRequestException(res.getMessage());
        } else {
            return ResponseEntity.ok(res);
        }
    }

    @RequestMapping(value = "/getFamilyMember", method = RequestMethod.POST)
    public ResponseEntity<JsonResponse> addFamilyMember() {
        Long userId = securityUtil.getCurrentLoggedUserId();
        if (userId == null) {
            throw new BadRequestException("Unable to determine the logged in user");
        }
        // Trying to register user
        JsonResponse res = userService.getFamilyMembers(userId);
        if (!res.getResult()) {
            throw new BadRequestException(res.getMessage());
        } else {
            return ResponseEntity.ok(res);
        }
    }

    @RequestMapping(value = "/deleteFamilyMember/{familyMemberId}", method = RequestMethod.DELETE)
    public ResponseEntity<JsonResponse> deleteFamilyMember(@PathVariable Long familyMemberId) {
        Long userId = securityUtil.getCurrentLoggedUserId();
        if (userId == null) {
            throw new BadRequestException("Unable to determine the logged in user");
        }
        // Trying to register user
        JsonResponse res = userService.deleteFamilyMembers(familyMemberId, userId);
        if (!res.getResult()) {
            throw new BadRequestException(res.getMessage());
        } else {
            return ResponseEntity.ok(res);
        }
    }

    @RequestMapping(value = "/updateFamilyMember", method = RequestMethod.POST)
    public ResponseEntity<JsonResponse> updateFamilyMember(@Valid @RequestBody TransFamilyMember member,
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
        // Trying to register user
        JsonResponse res = userService.updateFamilyMember(member, userId);
        if (!res.getResult()) {
            throw new BadRequestException(res.getMessage());
        } else {
            return ResponseEntity.ok(res);
        }
    }

    @RequestMapping(value = "/getUserProfile", method = RequestMethod.POST)
    public ResponseEntity<JsonResponse> getUserProfile() {
        Long userId = securityUtil.getCurrentLoggedUserId();
        if (userId == null) {
            throw new BadRequestException("Unable to determine the logged in user");
        }
        // Trying to register user
        JsonResponse res = userService.getUserProfile(userId);
        if (!res.getResult()) {
            throw new BadRequestException(res.getMessage());
        } else {
            return ResponseEntity.ok(res);
        }
    }

}
