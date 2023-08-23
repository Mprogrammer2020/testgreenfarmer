package com.greenelegentfarmer;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.greenelegentfarmer.util.JwtTokenUtil;

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint, Serializable {

	@Autowired
	private JwtTokenUtil tokenUtil;

	private static final long serialVersionUID = -7858869558953243875L;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,AuthenticationException authException) throws IOException {

		String authHeader = request.getHeader("authorization");
		String token = null;
		if (authHeader != null) {
			token = authHeader.replace("Bearer ", "");
			
				try {
				    DecodedJWT decodedJWT = JWT.decode(token);
				    
				    if(decodedJWT.getExpiresAt().before(new Date())) {
				    	System.out.println("refreshing expired token for "+decodedJWT.getSubject());
				    	token=tokenUtil.doGenerateToken(new HashMap<String, Object>(), decodedJWT.getSubject());
				    }
				} catch (JWTDecodeException e) {
					System.out.println("Can't decode this token:: "+token);
			}	
		}

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType("application/json");
		PrintWriter pw = response.getWriter();
		
		pw.write("{\"message\":\"You are not authorised to access this resource!\",\"data\":\"[]\""+(token==null ? "" : ",\"token\":\""+token+"\"")+"}");
		pw.close();
	}
}
