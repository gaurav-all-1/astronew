package com.nurtivillage.java.geonixApplication.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
public class SMSService {

    private static final Integer EXPIRE_MINS = 4;
    private LoadingCache<String, Integer> otpCache;

    public SMSService() {
        super();
        otpCache = CacheBuilder.newBuilder().expireAfterWrite(EXPIRE_MINS, TimeUnit.MINUTES)
                .build(new CacheLoader<String, Integer>() {
                    public Integer load(String key) {
                        return 0;
                    }
                });
    }

    public void sendSms(String orderId,String phonenumber)
    {
        try {
            Twilio.init("AC9952354b327bd57ae537e9886c303820", "1f47a1c96dbd90caceccda369addab6b");
            Message message = Message.creator(
                            new PhoneNumber("+91" + phonenumber),
                            new PhoneNumber("+15155828562"),
                            "Thank you for your order! Your purchase has been confirmed. Your order id is =" + orderId + "." + '\n' + "If you have any concerns please reach out to Geonix customer support at https://geonix.in")
                    .create();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public int generateOTP(String key) {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        otpCache.put(key, otp);
        return otp;
    }

    public int getOtp(String key) {
        try {
            return otpCache.get(key);
        } catch (Exception e) {
            return 0;
        }
    }

    //This method is used to clear the OTP catched already
    public void clearOTP(String key) {
        otpCache.invalidate(key);
    }



}
