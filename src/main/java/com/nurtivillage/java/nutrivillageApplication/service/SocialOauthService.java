package com.nurtivillage.java.nutrivillageApplication.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.support.URIBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.nurtivillage.java.nutrivillageApplication.dao.UserRepository;
import com.nurtivillage.java.nutrivillageApplication.dao.UserSocialLoginTypeRepository;
import com.nurtivillage.java.nutrivillageApplication.dto.SocialLoginTokenResponseDto;
import com.nurtivillage.java.nutrivillageApplication.dto.TokenData;
import com.nurtivillage.java.nutrivillageApplication.error.GenericException;
import com.nurtivillage.java.nutrivillageApplication.jwt.JwtTokenUtil;
import com.nurtivillage.java.nutrivillageApplication.model.User;
import com.nurtivillage.java.nutrivillageApplication.model.UserSocialLoginType;
import com.nutrivillage.java.nutrivillageApplication.enums.SocialLoginProviderType;
import com.nutrivillage.java.nutrivillageApplication.properties.SocialLoginProperties;


@Service
public class SocialOauthService {
	private final static Logger log=LogManager.getLogger(SocialOauthService.class);
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private SocialLoginProperties socialLoginProperties;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private PasswordEncoder passwordEncoder;
	private final String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%&";
	@Autowired
	private UserSocialLoginTypeRepository userLoginRepo;
	
	   public String getGoogleTokenUrl(String referral) {
	        String redirectUrl = socialLoginProperties.getGoogle().getRedirect();
	        String sb = socialLoginProperties.getGoogle().getOauth() + "?client_id=" + socialLoginProperties.getGoogle().getClientId() +
	                "&response_type=code" + "&scope=" + socialLoginProperties.getGoogle().getScope() + "&redirect_uri=" +
	                redirectUrl + "&state=" + referral;
	        return URIBuilder.fromUri(sb).build().toString();
	    }
	   
	   
	    public String GoogleCallback(String code, String state) throws Exception {
	        TokenData tokenData = null;
	        try {
	            tokenData = getGoogleIdTokenData(code, state);
	            return authenticateUser(tokenData,SocialLoginProviderType.GMAIL);
	        } catch (Exception e) {

	        	MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
	            params.add("Error", "Error while sign in from google");

	        	log.error("Error occured while sign in with google: "+e.getMessage());
	        	return buildUrl(socialLoginProperties.getGoogle().getCallbackPageUrl(),params);
	        }
	    }
	    
	    private TokenData getGoogleIdTokenData(String code, String state) throws Exception {

	        ImmutableMap<String, String> map = ImmutableMap.<String, String>builder().put("code", code)
	                .put("client_id", socialLoginProperties.getGoogle().getClientId()).put("client_secret", socialLoginProperties.getGoogle().getSecret())
	                .put("redirect_uri", socialLoginProperties.getGoogle().getRedirect()).put("state", state).put("grant_type", "authorization_code")
	                .build();
	        HttpHeaders httpHeaders = new HttpHeaders();
	        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
	        HttpEntity<String> requestEntity = new HttpEntity<>(getHttpRequestBody(map), httpHeaders);
	        ResponseEntity<SocialLoginTokenResponseDto> tokenResponse = restTemplate.postForEntity(socialLoginProperties.getGoogle().getAccessTokenUrl(), requestEntity, SocialLoginTokenResponseDto.class);

	        if (!tokenResponse.getStatusCode().is2xxSuccessful() || tokenResponse.getBody() == null) {
//	            log.error("error while signing google response code : {}, response body : {}", tokenResponse.getStatusCode(), tokenResponse);
//	            throw new GenericException(tokenResponse.getStatusCode().value(), "Error from Google");
	            throw new GenericException("Error from Google");
	        }
	        return objectMapper.readValue(Base64.decodeBase64(tokenResponse.getBody().getId_token().split("\\.")[1]), TokenData.class);
	    }
	    
	    private String authenticateUser(TokenData tokenData,SocialLoginProviderType authType) {
	    	User user=userRepo.findByEmail(tokenData.getEmail());
	    	if(user==null) {
	    		user=registerSocialUser(tokenData);}
	    		return createResponse(user);
	    		
	    	
	    	
	    }
	    private User registerSocialUser(TokenData tokenData) {
	    	log.info("Registering new User with email: "+tokenData.getEmail());
	    	User user=new User();
	    	user.setFirstName(tokenData.getGiven_name());
	    	user.setLastName(tokenData.getFamily_name());
	    	user.setEmail(tokenData.getEmail());
	    	String password=generateRandomAlphaNumeric(9);
	    	user.setPassword(passwordEncoder.encode(password));
	    	user=userRepo.save(user);
	    	UserSocialLoginType userLoginType=new UserSocialLoginType(user,SocialLoginProviderType.GMAIL,tokenData.getSub());
	    	userLoginRepo.save(userLoginType);
	    	log.info("User Registered Successfully");
	    	return user;
	    }
	    private String getHttpRequestBody(ImmutableMap<String, String> map) throws IOException {
	        Iterator<String> parameterIterator = map.keySet().iterator();
	        String parameterName;
	        StringBuilder requestParams = new StringBuilder();
	        while (parameterIterator.hasNext()) {
	            parameterName = parameterIterator.next();
	            requestParams.append(URLEncoder.encode(parameterName, "UTF-8"));
	            requestParams.append("=").append(URLEncoder.encode(map.get(parameterName), "UTF-8"));
	            requestParams.append("&");
	        }
	        return requestParams.deleteCharAt(requestParams.length() - 1).toString();
	    }
	    public String generateRandomAlphaNumeric(int length) {
	        Random rnd = new Random();
	        StringBuilder sb = new StringBuilder(length);
	        for (int i = 0; i < length; i++)
	            sb.append(chars.charAt(rnd.nextInt(chars.length())));
	        return sb.toString();
	    }
	    public String createResponse(User user) {
	    	String token=jwtTokenUtil.createToken(user.getEmail());
	    	MultiValueMap<String,String> map=new LinkedMultiValueMap<>();
	    	map.add("id", user.getId().toString());
	    	map.add("token", token);
	    	map.add("email", user.getEmail());
	    	
	    	return buildUrl(socialLoginProperties.getGoogle().getCallbackPageUrl(),map);
	    }
	    private String buildUrl(String baseUrl, MultiValueMap<String, String> params) {
	        return URIBuilder.fromUri(baseUrl).queryParams(params).build().toString();
	    }
	    
}
