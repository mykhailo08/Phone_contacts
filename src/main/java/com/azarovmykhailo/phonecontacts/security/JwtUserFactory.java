package com.azarovmykhailo.phonecontacts.security;

import com.azarovmykhailo.phonecontacts.entity.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;

public final class JwtUserFactory {

    public JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        return new JwtUser(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                new ArrayList<SimpleGrantedAuthority>(),
                true
        );
    }


}
