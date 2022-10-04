package com.nurtivillage.java.nutrivillageApplication.dto;

import javax.persistence.Lob;

public class ProductUpdateDto {
@Lob
private String description;
@Lob
private String additional;
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public String getAdditional() {
	return additional;
}
public void setAdditional(String additional) {
	this.additional = additional;
}

}
