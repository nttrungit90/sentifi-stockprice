package com.sentifi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.sentifi.stockprice.security.StockPriceRestAuthenticationEntryPoint;
import com.sentifi.stockprice.security.StockPriceRestAuthenticationFailureHandler;
import com.sentifi.stockprice.security.StockPriceRestAuthenticationSuccessHandler;
import com.sentifi.stockprice.security.StockPriceRestLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class StockPriceWebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private StockPriceRestAuthenticationEntryPoint stockPriceRestAuthenticationEntryPoint;
    @Autowired
    private StockPriceRestAuthenticationSuccessHandler stockPriceRestAuthenticationSuccessHandler;
    @Autowired
    private StockPriceRestAuthenticationFailureHandler stockPriceRestAuthenticationFailureHandler;
    @Autowired
    private StockPriceRestLogoutSuccessHandler stockPriceRestLogoutSuccessHandler;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	// allow all user to use all API of system now
    	http.authorizeRequests().antMatchers("/").anonymous();
        http.csrf().disable();
        http.exceptionHandling().authenticationEntryPoint(stockPriceRestAuthenticationEntryPoint);
        http.formLogin().successHandler(stockPriceRestAuthenticationSuccessHandler);
        http.formLogin().failureHandler(stockPriceRestAuthenticationFailureHandler);
        
        http.logout().deleteCookies("JSESSIONID");
        http.logout().logoutSuccessHandler(stockPriceRestLogoutSuccessHandler);
        
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }
}
