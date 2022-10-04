package com.nurtivillage.java.nutrivillageApplication.security;

public interface ISecurityUserService {

    String validatePasswordResetToken(String token);

}
