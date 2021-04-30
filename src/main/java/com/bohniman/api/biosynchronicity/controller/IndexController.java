package com.bohniman.api.biosynchronicity.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.bohniman.api.biosynchronicity.exception.BadRequestException;
import com.bohniman.api.biosynchronicity.model.MasterUser;
import com.bohniman.api.biosynchronicity.payload.request.AuthenticationRequest;
import com.bohniman.api.biosynchronicity.payload.request.OtpVerify;
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
        System.out.println("Enter Auth");
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect Username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        System.err.println(userDetails);

        String jwt = jwtUtil.generateToken(userDetails);
        MyUserDetails myUserDetails = (MyUserDetails) userDetails;
        JsonResponse res = new JsonResponse();
        res.setMessage("Login Successful !");
        res.setResult(true);
        res.setPayload(new AuthenticationResponse(jwt, myUserDetails.getUsername(),myUserDetails.getUserId(),
        myUserDetails.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toList())));
        return ResponseEntity.ok(res);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> registerUser(@RequestBody @Valid MasterUser user, BindingResult result) {
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
        JsonResponse res = userService.registerUser(user);
        // If Registered SuccessFully
        if (res.getResult()) {
            return ResponseEntity.ok(res);
        } else {
            throw new BadRequestException(res.getMessage());
        }
    }

    @RequestMapping(value = "/verifyOtp", method = RequestMethod.POST)
    public ResponseEntity<?> registerUser(@RequestBody @Valid OtpVerify otpDetails, BindingResult result) {
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
        JsonResponse res = userService.verifyOtp(otpDetails);
        // If Registered SuccessFully
        if (res.getResult()) {
            return ResponseEntity.ok(res);
        } else {
            throw new BadRequestException(res.getMessage());
        }
    }
}
