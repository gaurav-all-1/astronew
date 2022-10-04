package com.nutrivillage.java.nutrivillageApplication.enums;

public enum SocialLoginProviderType {
	   GMAIL("Gmail");
    private String name;

    SocialLoginProviderType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
