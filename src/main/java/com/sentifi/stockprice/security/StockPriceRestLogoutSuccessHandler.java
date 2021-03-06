package com.sentifi.stockprice.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class StockPriceRestLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler{
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		setRedirectStrategy(new StockPriceNoRedirectStrategy());
		super.onLogoutSuccess(request, response, authentication);
		
	}
}
