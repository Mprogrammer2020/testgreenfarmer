package com.greenelegentfarmer;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@EnableAsync
@EnableCaching
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebConfiguration {

	@Autowired
	private AuthEntryPoint authenticationEntryPoint;

	@Autowired
	private RequestFilter requestFilter;
	
	@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
	  CorsConfiguration configuration = new CorsConfiguration();
	  configuration.setAllowedOrigins(Arrays.asList("*"));
	  configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","PATCH", "DELETE", "OPTIONS", "HEAD"));
	  configuration.setAllowedHeaders(Arrays.asList("*"));
	  configuration.setMaxAge(3600L);
	  
	  UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	  source.registerCorsConfiguration("/**", configuration);
	  return source;
	}
	
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		// We don't need CSRF for this example
				httpSecurity.httpBasic().disable()
								 .csrf().disable()
								 .cors().and()
				// dont authenticate this particular request
				.authorizeRequests().antMatchers("/login","/sign-up/**","/password/**","/static/**",
								"/box","/box/{id}","/contact-us").permitAll().and()
				
				//open api defination 
				//URL -> http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/
				
				// all other requests need to be authenticated
				.authorizeRequests().antMatchers("/v3/api-docs/**","/swagger-ui/**").permitAll().
				anyRequest().authenticated().and().
				// make sure we use stateless session; session won't be used to
				// store user's state.
				exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		// Add a filter to validate the tokens with every request
		httpSecurity.addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class);
		
		return httpSecurity.build();
    }
}
