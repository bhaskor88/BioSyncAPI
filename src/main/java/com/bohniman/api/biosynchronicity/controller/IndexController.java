package com.bohniman.api.biosynchronicity.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.bohniman.api.biosynchronicity.exception.BadRequestException;
import com.bohniman.api.biosynchronicity.payload.request.AddressRequest;
import com.bohniman.api.biosynchronicity.payload.request.AddtionalRequest;
import com.bohniman.api.biosynchronicity.payload.request.AuthenticationRequest;
import com.bohniman.api.biosynchronicity.payload.request.PasswordRequest;
import com.bohniman.api.biosynchronicity.payload.request.ProfileRequest;
import com.bohniman.api.biosynchronicity.payload.request.RegisterRequest;
import com.bohniman.api.biosynchronicity.payload.response.AuthenticationResponse;
import com.bohniman.api.biosynchronicity.payload.response.JsonResponse;
import com.bohniman.api.biosynchronicity.service.MyUserDetails;
import com.bohniman.api.biosynchronicity.service.UserService;
import com.bohniman.api.biosynchronicity.util.JwtTokenUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class IndexController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    JwtTokenUtil jwtUtil;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
            throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect Username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        String jwt = jwtUtil.generateToken(userDetails);
        MyUserDetails myUserDetails = (MyUserDetails) userDetails;
        JsonResponse res = new JsonResponse();
        res.setMessage("Login Successful !");
        res.setResult(true);
        res.setPayload(new AuthenticationResponse(jwt, myUserDetails.getUsername(), myUserDetails.getUserId(),
                myUserDetails.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toList())));
        return ResponseEntity.ok(res);
    }

    @RequestMapping(value = "/emailVerify", method = RequestMethod.POST)
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegisterRequest registerUser, BindingResult result) {
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
        // Trying to fire OTP for the email
        JsonResponse res = userService.fireOtp(registerUser);
        // If OTP fired SuccessFully
        if (res.getResult()) {
            return ResponseEntity.ok(res);
        } else {
            throw new BadRequestException(res.getMessage());
        }
    }

    // This Method will create a new user if not exist
    @RequestMapping(value = "/verifyOtp", method = RequestMethod.POST)
    public ResponseEntity<?> verifyOtp(@RequestBody @Valid RegisterRequest registerUser, BindingResult result) {
        // Data sanity check
        if (registerUser.getOtp() != null && registerUser.getOtp().length() == 6) {
            try {
                Integer i = Integer.parseInt(registerUser.getOtp());
            } catch (Exception e) {
                result.rejectValue("otp", "otp", "Invalid OTP Received");
            }
        } else {
            result.rejectValue("otp", "otp", "Invalid OTP Received");
        }

        if (result.hasErrors()) {
            List<FieldError> errors = result.getFieldErrors();
            List<String> message = new ArrayList<>();
            for (FieldError e : errors) {
                message.add("@" + e.getField() + " : " + e.getDefaultMessage());
            }
            return new ResponseEntity<JsonResponse>(new JsonResponse(false, message, "Data validation error"),
                    HttpStatus.BAD_REQUEST);
        }
        // Trying to verify OTP
        JsonResponse res = userService.verifyOtpAndCreateUser(registerUser);
        // If OTP verified SuccessFully
        if (res.getResult()) {
            return ResponseEntity.ok(res);
        } else {
            throw new BadRequestException(res.getMessage());
        }
    }

    @RequestMapping(value = "/createPassword", method = RequestMethod.POST)
    public ResponseEntity<?> createPassword(@RequestBody @Valid PasswordRequest request, BindingResult result) {
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
        JsonResponse res = userService.createPassword(request);
        // If Registered SuccessFully
        if (res.getResult()) {
            return ResponseEntity.ok(res);
        } else {
            throw new BadRequestException(res.getMessage());
        }
    }

    @RequestMapping(value = "/createProfile", method = RequestMethod.POST)
    public ResponseEntity<?> createProfile(@RequestBody @Valid ProfileRequest request, BindingResult result) {
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
        JsonResponse res = userService.createProfile(request);
        // If Registered SuccessFully
        if (res.getResult()) {
            return ResponseEntity.ok(res);
        } else {
            throw new BadRequestException(res.getMessage());
        }
    }

    @RequestMapping(value = "/createAddress", method = RequestMethod.POST)
    public ResponseEntity<?> createAddress(@RequestBody @Valid AddressRequest request, BindingResult result) {
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
        JsonResponse res = userService.createAddress(request);
        // If Registered SuccessFully
        if (res.getResult()) {
            return ResponseEntity.ok(res);
        } else {
            throw new BadRequestException(res.getMessage());
        }
    }

    @RequestMapping(value = "/createAdditional", method = RequestMethod.POST)
    public ResponseEntity<?> createAddtional(@RequestBody @Valid AddtionalRequest request, BindingResult result) {
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
        JsonResponse res = userService.createAddtional(request);
        // If Registered SuccessFully
        if (res.getResult()) {
            return ResponseEntity.ok(res);
        } else {
            throw new BadRequestException(res.getMessage());
        }
    }

    @RequestMapping(value = "/getOtpForgotPassword", method = RequestMethod.POST)
    public ResponseEntity<?> getOtpForgotPassword(@RequestBody @Valid RegisterRequest registerUser,
            BindingResult result) {
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
        // Trying to fire OTP for the email
        JsonResponse res = userService.fireOtpForgotPassword(registerUser);
        // If OTP fired SuccessFully
        if (res.getResult()) {
            return ResponseEntity.ok(res);
        } else {
            throw new BadRequestException(res.getMessage());
        }
    }

    @RequestMapping(value = "/verifyOtpForgotPassword", method = RequestMethod.POST)
    public ResponseEntity<?> verifyOtpForgotPassword(@RequestBody @Valid RegisterRequest registerUser,
            BindingResult result) {
        // Data sanity check
        if (registerUser.getOtp() != null && registerUser.getOtp().length() == 6) {
            try {
                Integer i = Integer.parseInt(registerUser.getOtp());
            } catch (Exception e) {
                result.rejectValue("otp", "otp", "Invalid OTP Received");
            }
        } else {
            result.rejectValue("otp", "otp", "Invalid OTP Received");
        }

        if (result.hasErrors()) {
            List<FieldError> errors = result.getFieldErrors();
            List<String> message = new ArrayList<>();
            for (FieldError e : errors) {
                message.add("@" + e.getField() + " : " + e.getDefaultMessage());
            }
            return new ResponseEntity<JsonResponse>(new JsonResponse(false, message, "Data validation error"),
                    HttpStatus.BAD_REQUEST);
        }
        // Trying to verify OTP
        JsonResponse res = userService.verifyOtpForgotPassword(registerUser);
        // If OTP verified SuccessFully
        if (res.getResult()) {
            return ResponseEntity.ok(res);
        } else {
            throw new BadRequestException(res.getMessage());
        }
    }



}
