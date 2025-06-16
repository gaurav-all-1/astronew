package com.social.java.socialapplication.security;

public interface ISecurityUserService {

    String validatePasswordResetToken(String token);

}
