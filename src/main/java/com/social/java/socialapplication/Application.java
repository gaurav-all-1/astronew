package com.social.java.socialapplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.social.java.socialapplication.dao.RoleRepository;
import com.social.java.socialapplication.dao.UserProfileRepository;
import com.social.java.socialapplication.dao.UserRepository;
import com.social.java.socialapplication.validation.Name;
import com.social.java.socialapplication.enums.SocialLoginProperties;
@EnableCaching
@SpringBootApplication
@EnableAsync
public class Application {

	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	UserRepository userRepository;
	

	
	@Autowired
	Name name;
	
	
	@Autowired
	UserProfileRepository userProfileRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
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
