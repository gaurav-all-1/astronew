package com.nutrivillage.java.nutrivillageApplication.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ConfigurationProperties(prefix="social.login")
public class SocialLoginProperties {
	private SocialLogin google;
	
	public SocialLogin getGoogle() {
		return google;
	}

	public void setGoogle(SocialLogin google) {
		this.google = google;
	}

	public static class SocialLogin{
	private String clientId;
      private String secret;
      private String oauth;
      private String accessTokenUrl;
      private String scope;
      private String redirect;
      private String state;
      private String authority;
      private String callbackPageUrl;
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getSecret() {
		return secret;
	}
	public String getCallbackPageUrl() {
		return callbackPageUrl;
	}
	public void setCallbackPageUrl(String callbackPageUrl) {
		this.callbackPageUrl = callbackPageUrl;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public String getOauth() {
		return oauth;
	}
	public void setOauth(String oauth) {
		this.oauth = oauth;
	}
	public String getAccessTokenUrl() {
		return accessTokenUrl;
	}
	public void setAccessTokenUrl(String accessTokenUrl) {
		this.accessTokenUrl = accessTokenUrl;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getRedirect() {
		return redirect;
	}
	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	}
	   @Bean
	    public RestTemplate restTemplate() {
	        return new RestTemplate();
	    }
}
