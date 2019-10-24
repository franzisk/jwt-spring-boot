package com.safeplace.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.safeplace.config.jwt.AuthorizationRequestFilter;
import com.safeplace.config.jwt.NotAuthorizedEntryPoint;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private NotAuthorizedEntryPoint notAuthorizedEntryPoint;

	@Autowired
	private UserDetailsService appUserDetailsService;

	@Autowired
	private AuthorizationRequestFilter authorizationRequestFilter;

	@Value("${jwt.get.token.uri}")
	private String authenticationPath;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(appUserDetailsService).passwordEncoder(passwordEncoderBean());
	}

	// You can use: @Autowired PasswordEncoder encoder;
	@Bean
	public PasswordEncoder passwordEncoderBean() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().disable()
		.exceptionHandling().authenticationEntryPoint(notAuthorizedEntryPoint).and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		.authorizeRequests().anyRequest().authenticated();

		httpSecurity
			.addFilterBefore(authorizationRequestFilter, UsernamePasswordAuthenticationFilter.class);

		httpSecurity
			.headers()
			.frameOptions()
			.sameOrigin() 
			.cacheControl(); 
	}

	@Override
	public void configure(WebSecurity webSecurity) throws Exception {
		webSecurity.ignoring()
			.antMatchers(HttpMethod.POST, authenticationPath)
			.antMatchers(HttpMethod.OPTIONS, "/**").and().ignoring()
			.antMatchers(HttpMethod.GET, "/").and().ignoring()
			.antMatchers(HttpMethod.GET, "/actuator/**");		
	}
}
