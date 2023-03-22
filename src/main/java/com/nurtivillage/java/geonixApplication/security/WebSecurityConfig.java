package com.nurtivillage.java.geonixApplication.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import com.nurtivillage.java.geonixApplication.jwt.JwtAuthenticationEntryPoint;
import com.nurtivillage.java.geonixApplication.jwt.JwtRequestFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;
	
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // disable caching
		http.headers().cacheControl();
		http.requiresChannel().anyRequest().requiresSecure().and().cors().and().csrf().disable()
        .authorizeRequests()
        .antMatchers("/").permitAll()
        .antMatchers(HttpMethod.POST,"/user/registration").permitAll()
        .antMatchers(HttpMethod.POST,"/login").permitAll()
        .antMatchers(HttpMethod.GET,"/registrationConfirm").permitAll()
        .antMatchers(HttpMethod.GET,"/getProfilePic/**").permitAll()
        .antMatchers(HttpMethod.GET,"/getProfilePicByProfileId/**").permitAll()
        .antMatchers(HttpMethod.GET,"/console").permitAll()
        .antMatchers(HttpMethod.GET,"/GetCategories").permitAll()
        .antMatchers(HttpMethod.GET,"/getCategory/*").permitAll()
        .antMatchers(HttpMethod.GET,"/product/list").permitAll()
        .antMatchers(HttpMethod.GET,"/product/list-all").permitAll()
        .antMatchers(HttpMethod.GET,"/product/list/*").permitAll()
        .antMatchers(HttpMethod.POST,"/product/search").permitAll()
        .antMatchers(HttpMethod.GET,"/product/highlighter").permitAll()
        .antMatchers(HttpMethod.GET,"/product/test").permitAll()
        .antMatchers(HttpMethod.GET,"/product/info/*").permitAll()
        .antMatchers(HttpMethod.POST,"/product/test").permitAll()
        .antMatchers(HttpMethod.GET,"/product/menu").permitAll()
        .antMatchers(HttpMethod.GET,"/product/info/*").permitAll()
				.antMatchers(HttpMethod.GET,"/product/productinfo/*").permitAll()
        .antMatchers(HttpMethod.GET,"/getAllVariants").permitAll()
        .antMatchers(HttpMethod.GET,"/provider/google/token").permitAll()
        .antMatchers(HttpMethod.GET,"/provider/google/callback").permitAll()
        .antMatchers(HttpMethod.GET,"/provider/getPractice").permitAll()
        .antMatchers(HttpMethod.GET,"/getAllVariants").permitAll()
        .antMatchers(HttpMethod.GET,"/product/getProductsByVariant").permitAll()
        .antMatchers(HttpMethod.GET,"/review/list").permitAll()
        .antMatchers(HttpMethod.GET,"/offer/list").permitAll()
        .antMatchers(HttpMethod.GET,"/review/list").permitAll()
				.antMatchers(HttpMethod.GET,"/review/list").permitAll()
				.antMatchers(HttpMethod.GET,"/review/testsms/**").permitAll()
        .antMatchers(HttpMethod.GET,"/user/ForgotPassword").permitAll()
        .antMatchers(HttpMethod.PUT,"/user/changePassword").permitAll()
        .antMatchers(HttpMethod.GET,"/review/list/**").permitAll()
        .antMatchers(HttpMethod.GET,"/secured").permitAll()
        .antMatchers(HttpMethod.GET,"/offer/product/**").permitAll()
        .antMatchers(HttpMethod.POST,"/order/guest").permitAll()
        .antMatchers("/registrationAccountConfirm").permitAll()
        .antMatchers(HttpMethod.GET,"/findUserByPhone").permitAll()
        .antMatchers(HttpMethod.GET,"/validateOtp").permitAll()
        .antMatchers("/product/deleteimage/**").permitAll()
        .antMatchers("/badUser").permitAll()
				.antMatchers("/order/send").permitAll()
        
        .anyRequest().authenticated()
        .and().
        exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Add a filter to validate the tokens with every request
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        // Create a default account
    	auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
    }
    
    @Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
    
    @Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}   
    

}
