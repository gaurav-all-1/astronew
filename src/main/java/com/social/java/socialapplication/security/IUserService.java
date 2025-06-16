package com.social.java.socialapplication.security;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.social.java.socialapplication.dto.PasswordDto;
import com.social.java.socialapplication.dto.UserDto;
import com.social.java.socialapplication.error.UserAlreadyExistException;
import com.social.java.socialapplication.model.PasswordResetToken;
import com.social.java.socialapplication.model.User;
import com.social.java.socialapplication.model.UserProfile;
import com.social.java.socialapplication.model.VerificationToken;

public interface IUserService {

    User registerNewUserAccount(UserDto accountDto,boolean isRecuiter) throws UserAlreadyExistException;

    User getUser(String verificationToken);

    void saveRegisteredUser(User user);

    void deleteUser(User user);

    void createVerificationTokenForUser(User user, String token);

    VerificationToken getVerificationToken(String VerificationToken);

    VerificationToken generateNewVerificationToken(String token);

    void createPasswordResetTokenForUser(User user, String token);

    User findUserByEmail(String email);

    PasswordResetToken getPasswordResetToken(String token);

    Optional<User> getUserByPasswordResetToken(String token);

    Optional<User> getUserByID(long id);

    void changeUserPassword(User user, String password);
    public String resetUserPassword(PasswordDto passwordDto) throws Exception;

    boolean checkIfValidOldPassword(User user, String password);

    String validateVerificationToken(String token);

    String generateQRUrl(User user) throws UnsupportedEncodingException;

    User updateUser2FA(boolean use2FA);
    
    public List<User> getUserByRoleName(String roleName)throws Exception;
    
    public void sendMailForForgotPasswordToUser(String email) throws Exception;
    
    public List<UserProfile> getUserProfilesByRoleParams(Map<String, Object> searchCriteria)throws Exception;

//    List<String> getUsersFromSessionRegistry();

//    NewLocationToken isNewLoginLocation(String username, String ip);

    String isValidNewLocationToken(String token);
    
    public User getRecruiter(String recruiterProfileId);

//    void addUserLocation(User user, String ip);
}
