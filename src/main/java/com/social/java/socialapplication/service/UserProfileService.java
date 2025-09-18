package com.social.java.socialapplication.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.social.java.socialapplication.dao.DoctorProfileRepository;
import com.social.java.socialapplication.model.DoctorProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.social.java.socialapplication.dao.UserProfileRepository;
import com.social.java.socialapplication.dao.UserRepository;
import com.social.java.socialapplication.error.GenericException;
import com.social.java.socialapplication.model.User;
import com.social.java.socialapplication.model.UserProfile;

@Service
public class UserProfileService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserProfileRepository userProfileRepository;

	@Autowired
	DoctorProfileRepository doctorProfileRepository;
		

	public UserProfile updateUserProfile(String userId, UserProfile profile) throws Exception {
		try {
			Optional<User> user = userRepository.findById(Long.parseLong(userId));
			if (user.isPresent()) {

				User profileUser = user.get();
				if (profileUser.getUserProfile() == null) {
					profileUser.setUserProfile(profile);
					return userRepository.save(profileUser).getUserProfile();
				} else {
					UserProfile savedUserProfile = user.get().getUserProfile();
					profile.setId(savedUserProfile.getId());
					
					profile.setProfilePicName(savedUserProfile.getProfilePicName());
					return userProfileRepository.save(profile);
				}

			} else {
				throw new GenericException("User id is not available for this ID=" + userId);
			}

		} catch (Exception e) {
			// TODO: handle exception
			throw new GenericException(e.getMessage());
		}
	}

	public DoctorProfile updateDoctorProfile(String userId, DoctorProfile profile) throws Exception {
		try {
			Optional<User> user = userRepository.findById(Long.parseLong(userId));
			if (user.isPresent()) {

				User profileUser = user.get();
				if (profileUser.getDoctorProfile() == null) {
					profileUser.setDoctorProfile(profile);
					return userRepository.save(profileUser).getDoctorProfile();
				} else {
					DoctorProfile savedDoctorProfile = user.get().getDoctorProfile();
					profile.setId(savedDoctorProfile.getId());

					profile.setProfilePicName(savedDoctorProfile.getProfilePicName());
					return doctorProfileRepository.save(profile);
				}

			} else {
				throw new GenericException("User id is not available for this ID=" + userId);
			}

		} catch (Exception e) {
			// TODO: handle exception
			throw new GenericException(e.getMessage());
		}
	}
	
	public UserProfile updateUserProfilePic(UserProfile profile) throws Exception {
		try {
			return userProfileRepository.save(profile);
		} catch (Exception e) {
			// TODO: handle exception
			throw new GenericException(e.getMessage());
		}
	}
	
	
	
	
	public UserProfile getUserProfile(String userId)throws Exception
	{
		try {
			
			Optional<User> user = userRepository.findById(Long.parseLong(userId));
			if(user.isPresent())
			{
				UserProfile profile = user.get().getUserProfile();
				if(profile==null)
				{
					throw new GenericException("Profile is not yet completed by the user");
				}
				return profile;
			}
			else
			{
				throw new GenericException("Didn't find any user by this Id");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			throw new GenericException(e.getMessage());
		}
	}
	
	
	public UserProfile getUserProfileById(String userProfileId)throws Exception
	{
		try {
			
			Optional<UserProfile> userProfile = userProfileRepository.findById(Integer.parseInt(userProfileId));
			if(userProfile.isPresent())
			{
				UserProfile profile = userProfile.get();
				if(profile==null)
				{
					throw new GenericException("Profile is not yet completed by the user");
				}
				return profile;
			}
			else
			{
				throw new GenericException("Didn't find any user by this Id");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			throw new GenericException(e.getMessage());
		}
	}
	
	
	
	

	
	
	public User getUser(String userId) throws Exception{
		try {
			
			Optional<User> user = userRepository.findById(Long.parseLong(userId));
			if(user.isPresent())
			{
				User savedUser = user.get();
				if(savedUser==null)
				{
					throw new GenericException("No user found for this ID");
				}
				return savedUser;
			}
			else
			{
				throw new GenericException("Didn't find any user by this Id");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			throw new GenericException(e.getMessage());
		}
	}

	public User getUserByPhone(String phoneNo) throws Exception{
		try {
			Optional<User> user = Optional.of(userRepository.findByPhoneNo(phoneNo));
			if(user.isPresent())
			{
				User savedUser = user.get();
				if(savedUser==null)
				{
					throw new GenericException("No user found by this phone no.");
				}
				return savedUser;
			}
			else
			{
				throw new GenericException("Didn't find any user by this phone no.");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			throw new GenericException(e.getMessage());
		}
	}
	
	public List<User> getUsers() throws Exception{
		try {
			List<User> userList = new ArrayList<User>();
			Iterable<User> users = userRepository.findAll();
			users.forEach(userList::add);
			if(userList.size()>0)
			{
				return userList;
			}
			else
			{
				throw new GenericException("Didn't find any users");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			throw new GenericException(e.getMessage());
		}
	}
	
	
	
	
	
}
