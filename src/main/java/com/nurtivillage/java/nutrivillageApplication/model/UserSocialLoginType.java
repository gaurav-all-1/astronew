package com.nurtivillage.java.nutrivillageApplication.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.nutrivillage.java.nutrivillageApplication.enums.SocialLoginProviderType;

@Entity
public class UserSocialLoginType {
	  @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long id;
	@ManyToOne
	private User user;
	
	private SocialLoginProviderType loginProvider;
	
	private String providerId;

	public UserSocialLoginType(User user, SocialLoginProviderType loginProvider, String providerId) {
		super();
		this.user = user;
		this.loginProvider = loginProvider;
		this.providerId = providerId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public SocialLoginProviderType getLoginProvider() {
		return loginProvider;
	}

	public void setLoginProvider(SocialLoginProviderType loginProvider) {
		this.loginProvider = loginProvider;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
	
}
