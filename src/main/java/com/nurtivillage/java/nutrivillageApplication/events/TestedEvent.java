package com.nurtivillage.java.nutrivillageApplication.events;

import org.springframework.context.ApplicationEvent;

public class TestedEvent<T> extends ApplicationEvent {
    private T data;
    public TestedEvent(T event){
        super(event);
        System.out.println(event);
    }

    public T getData() {
        return data;
    }
}
