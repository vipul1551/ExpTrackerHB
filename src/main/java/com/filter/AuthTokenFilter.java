package com.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component
public class AuthTokenFilter implements Filter{

	@Override
	public void doFilter(ServletRequest reqx, ServletResponse respx, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest)reqx;
		String url = request.getRequestURL().toString();
		System.out.println("Incoming URL => " + url);
		System.out.println("Request Method => " + request.getMethod());
		
		if(url.contains("/public/") || request.getMethod().toLowerCase().equals("options")) {
			System.out.println("options by pass....");
			chain.doFilter(reqx, respx);
		}else {
			String  authToken = request.getHeader("authToken");
			System.out.println("authtoken => " + authToken);
			
			if(authToken == null || authToken.trim().length() != 16) {
				System.out.println("Token Verification Failed...");
				HttpServletResponse response = (HttpServletResponse) respx;
				response.setContentType("application/json");
				response.setStatus(401);
				response.getWriter().write("{'msg' : 'Please Login before access service'}");
			}else {
				System.out.println("User verified...");
				chain.doFilter(reqx, respx);
			}
		}
	}

	
}
