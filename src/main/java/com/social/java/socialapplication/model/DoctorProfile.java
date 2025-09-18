package com.social.java.socialapplication.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
public class DoctorProfile {

	@Id
	@Column(unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String firstName;
    private String lastName;
    private String email;
	private String aboutMe;
	private String phone;
	private String profilePicName;
	private String placeOfBirth;
	private String state;
	private String pin;
	private String city;
	private String address;
	private Date dob;
	private String bloodGroup;
	private LocalDateTime timeOfBirth;
	private String country;
	private String gender;
	private String department;
	private String designation;
	private String medicalRegistrationNumber;
	private String languageSpoken;
	private String consultationCharge;

	@OneToMany(cascade = CascadeType.ALL)
	private List<EducationalInformation> educationalInformations;

	@OneToMany(cascade = CascadeType.ALL)
	private List<Awards> awards;

	@OneToMany(cascade = CascadeType.ALL)
	private List<Certification> certifications;

	public List<Certification> getCertifications() {
		return certifications;
	}

	public void setCertifications(List<Certification> certifications) {
		this.certifications = certifications;
	}

	public List<Awards> getAwards() {
		return awards;
	}

	public void setAwards(List<Awards> awards) {
		this.awards = awards;
	}

	public List<EducationalInformation> getEducationalInformations() {
		return educationalInformations;
	}

	public void setEducationalInformations(List<EducationalInformation> educationalInformations) {
		this.educationalInformations = educationalInformations;
	}

	public String getConsultationCharge() {
		return consultationCharge;
	}

	public void setConsultationCharge(String consultationCharge) {
		this.consultationCharge = consultationCharge;
	}

	public String getLanguageSpoken() {
		return languageSpoken;
	}

	public void setLanguageSpoken(String languageSpoken) {
		this.languageSpoken = languageSpoken;
	}

	public String getMedicalRegistrationNumber() {
		return medicalRegistrationNumber;
	}

	public void setMedicalRegistrationNumber(String medicalRegistrationNumber) {
		this.medicalRegistrationNumber = medicalRegistrationNumber;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}



	public int getId() {
		return id;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public DoctorProfile() {
		super();
		// TODO Auto-generated constructor stub
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAboutMe() {
		return aboutMe;
	}
	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}
	public String getProfilePicName() {
		return profilePicName;
	}
	public void setProfilePicName(String profilePicName) {
		this.profilePicName = profilePicName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPlaceOfBirth() {
		return placeOfBirth;
	}
	public void setPlaceOfBirth(String placeOfBirth) {
		this.placeOfBirth = placeOfBirth;
	}
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public LocalDateTime getTimeOfBirth() {
		return timeOfBirth;
	}
	public void setTimeOfBirth(LocalDateTime timeOfBirth) {
		this.timeOfBirth = timeOfBirth;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}


    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }
}
