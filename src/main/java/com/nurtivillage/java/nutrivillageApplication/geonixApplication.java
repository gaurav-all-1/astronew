package com.nurtivillage.java.geonixApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties.Tomcat.Resource;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nurtivillage.java.geonixApplication.dao.RoleRepository;
import com.nurtivillage.java.geonixApplication.dao.UserProfileRepository;
import com.nurtivillage.java.geonixApplication.dao.UserRepository;
import com.nurtivillage.java.geonixApplication.model.Location;
import com.nurtivillage.java.geonixApplication.model.Role;
import com.nurtivillage.java.geonixApplication.model.User;
import com.nurtivillage.java.geonixApplication.model.UserProfile;
import com.nurtivillage.java.geonixApplication.validation.Name;
import com.geonix.java.geonixApplication.properties.SocialLoginProperties;
@EnableCaching
@SpringBootApplication
@EnableAsync
public class geonixApplication {

	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	UserRepository userRepository;
	

	
	@Autowired
	Name name;
	
	
	@Autowired
	UserProfileRepository userProfileRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(geonixApplication.class, args);
	}
	

	
	 @Bean
	 public WebMvcConfigurer corsConfigurer() 
	    {
	        return new WebMvcConfigurer() {
	            @Override
	            public void addCorsMappings(CorsRegistry registry) {
	                registry.addMapping("/**").allowedOrigins("*")
					.allowedMethods("GET", "PUT", "POST", "PATCH", "DELETE", "OPTIONS");
	            }
	        };
	    }
	   @Bean
	    public CacheManager cacheManager() {
	        return new EhCacheCacheManager(cacheMangerFactory().getObject());
	    }

	    @Bean
	    public EhCacheManagerFactoryBean cacheMangerFactory() {
	        EhCacheManagerFactoryBean bean = new EhCacheManagerFactoryBean();
	        bean.setConfigLocation(new ClassPathResource("ehcache.xml"));
	        bean.setShared(true);
	        return bean;
	    }
	    
	    @Bean
	    public SocialLoginProperties socialLoginProperties() {
	    	return new SocialLoginProperties();
	    }
         
	    @Bean
	    public RestTemplate restTemplate() {
	        return new RestTemplate();
	    }
}
