package com.nurtivillage.java.nutrivillageApplication.service;

import java.util.List;

import org.springframework.stereotype.Service;
@Service
public class ResponseService {
    private boolean status;
    private String message;
    private List<?> data;
    private int totalPage;

    public ResponseService(){};

    public ResponseService(String msg,boolean status,List<?> data,int page){
        this.message = msg;
        this.status = status;
        this.data = data;
        this.totalPage = page;
    };

    public ResponseService(String msg,boolean status,List<?> data){
        this.message = msg;
        this.status = status;
        this.data = data;
    };

    public List<?> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
    
    public int getTotalPage() {
        return totalPage;
    }
    
    public boolean getStatus(){
        return status;
    }
}
