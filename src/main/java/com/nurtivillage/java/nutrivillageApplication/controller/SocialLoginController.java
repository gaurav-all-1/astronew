package com.nurtivillage.java.nutrivillageApplication.controller;

import java.util.Map;

import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.nurtivillage.java.nutrivillageApplication.service.ResponseService;
import com.nurtivillage.java.nutrivillageApplication.service.SocialOauthService;

@Controller
@RequestMapping("/provider")

public class SocialLoginController {
	
	@Autowired
	private SocialOauthService socialOauthService;
	   @RequestMapping(value = "/google/token", method = RequestMethod.GET)
	    public RedirectView getGoogleOauthToken(
	            @RequestParam(value = "referral", defaultValue = "DEFAULT") String referral
	    )  {
	        return new RedirectView(socialOauthService.getGoogleTokenUrl(referral));
	    }
	   
	   @GetMapping(value = "/google/callback")
	    public RedirectView handleGoogleOauthResponse(@RequestParam(value = "error", defaultValue = "") String error,
	                                                  @RequestParam(value = "code", defaultValue = "") String code,
	                                                  @RequestParam(value = "state", defaultValue = "") String state,
	                                                  @RequestParam(value = "error_description", defaultValue = "") String error_description,
	                                               HttpSession session) throws Exception {
	        if (error.length() > 0) {
	            throw new AuthenticationException(error);
	        }
	      return new RedirectView(socialOauthService.GoogleCallback(code, state));
	    }
	   

}
