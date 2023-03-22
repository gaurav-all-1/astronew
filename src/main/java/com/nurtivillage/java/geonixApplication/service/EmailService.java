package com.nurtivillage.java.geonixApplication.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private SMSService smsService;
	
	public void sendOtpMessage(int otp, String number) throws MessagingException {
	
		smsService.sendSms("otp",number);
   }
	
}
	
