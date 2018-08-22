package com.example.eventmvc.security;


import com.example.eventmvc.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyLogoutSuccessHandlerTwo implements LogoutSuccessHandler {
	@Override
	public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
		User principal = (User)authentication.getPrincipal();
		if(principal!=null){
			new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse, authentication);
			System.out.println(authentication);}
	}
//	@Override
//	public void onLogoutSuccess(HttpServletRequest request,
//			HttpServletResponse response, Authentication authentication)
//			throws IOException, ServletException {
//		if(authentication != null) {
//			System.out.println(authentication.getName());
//		}
//		//perform other required operation
//		response.setStatus(HttpStatus.OK.value());
//		response.getWriter().flush();
//	}
}