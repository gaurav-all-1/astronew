package com.nurtivillage.java.geonixApplication.security;

public interface ISecurityUserService {

    String validatePasswordResetToken(String token);

}
