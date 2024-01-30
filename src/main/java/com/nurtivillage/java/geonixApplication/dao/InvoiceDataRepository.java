package com.nurtivillage.java.geonixApplication.dao;

import com.nurtivillage.java.geonixApplication.model.InvoiceData;
import com.nurtivillage.java.geonixApplication.model.User;
import com.nurtivillage.java.geonixApplication.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.stream.Stream;

public interface InvoiceDataRepository extends JpaRepository<InvoiceData, Long> {


}
