package com.bohniman.api.biosynchronicity.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.bohniman.api.biosynchronicity.model.MasterUser;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class MyUserDetails implements UserDetails {

    /**
     * Custom User Details
     */
    private static final long serialVersionUID = 845754245631245L;

    private String username;
    private String password;
    private String mobileNo;
    private String email;
    private boolean isEnable;
    private boolean isAccountNotExpired;
    private boolean isCredentialsNotExpired;
    private boolean isAccountNotLocked;
    private List<GrantedAuthority> authorities;

    public MyUserDetails(MasterUser user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.mobileNo = user.getMobileNo();
        this.isEnable = user.isEnable();
        this.isAccountNotLocked = user.isAccountNotLocked();
        this.isAccountNotExpired = user.isAccountNotExpired();
        this.isCredentialsNotExpired = user.isCredentialsNotExpired();
        this.authorities = user.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getRoleName()))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNotExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNotLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNotExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnable;
    }

}