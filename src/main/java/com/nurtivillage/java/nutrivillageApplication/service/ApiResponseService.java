package com.nurtivillage.java.nutrivillageApplication.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class ApiResponseService {
    private String msg;
    private boolean status;
    private List<?> data;
    private Map<String,String> response;
    private int totalPage; 

    public ApiResponseService(){};
    public ApiResponseService(String msg,boolean status,List<?> data,int page){
        this.msg = msg;
        this.status = status;
        this.data = data;
        this.totalPage = page;
    };
    public ApiResponseService(String msg,boolean status,List<?> data){
        this.msg = msg;
        this.status = status;
        this.data = data;
    };
    public ApiResponseService(String msg,boolean status,List<?> data,Map<String,String> response){
        this.msg = msg;
        this.status = status;
        this.data = data;
        this.response=response;
    };

    public List<?> getData() {
        return data;
    }
    public String getMsg() {
        return msg;
    }
    public boolean getStatus(){
        return status;
    }

 
	public Map<String, String> getResponse() {
		return response;
	}
	public void setResponse(Map<String, String> response) {
		this.response = response;
	}
	public int getTotalPage() {
        return totalPage;
    }


}
