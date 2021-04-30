package com.bohniman.api.biosynchronicity.security;

import com.bohniman.api.biosynchronicity.service.MyUserDetails;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    public Long getCurrentLoggedUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof MyUserDetails) {
            return ((MyUserDetails) principal).getUserId();
        } else {
            return null;
        }
    }
}
