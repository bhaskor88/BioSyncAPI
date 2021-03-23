package com.bohniman.api.biosynchronicity.controller;

import com.bohniman.api.biosynchronicity.payload.request.AuthenticationRequest;
import com.bohniman.api.biosynchronicity.payload.response.AuthenticationResponse;
import com.bohniman.api.biosynchronicity.util.JwtTokenUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController("/admin")
public class AdminController {

}
