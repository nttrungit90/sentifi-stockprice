package com.sentifi.stockprice.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
 
@Component
public class StockPriceRestAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
 
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
 
    	// don't redirect after login successfully so that we can return json value
    	setRedirectStrategy(new StockPriceNoRedirectStrategy());
        super.onAuthenticationSuccess(request, response, authentication);
        
		// return logged in user information in json format after login successfully
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("Login Successfully!");
    }
}
