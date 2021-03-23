package com.bohniman.api.biosynchronicity.service;

import java.util.Optional;

import com.bohniman.api.biosynchronicity.model.MasterUser;
import com.bohniman.api.biosynchronicity.repository.MasterUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    MasterUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MasterUser> user = userRepository.findByUsername(username);
        user.orElseThrow(() -> new UsernameNotFoundException("User not found : " + username));
        return user.map(MyUserDetails::new).get();
    }
}
