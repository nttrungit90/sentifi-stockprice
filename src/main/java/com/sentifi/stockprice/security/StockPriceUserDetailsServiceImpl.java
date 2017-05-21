package com.sentifi.stockprice.security;

import java.util.ArrayList;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class StockPriceUserDetailsServiceImpl implements UserDetailsService{

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	// always return a fate user so that client can logged in the system
        return new StockPriceCustomUser(username, "password", new ArrayList<GrantedAuthority>(),
    			1, "firstName", "lastName", 1);
    }
}