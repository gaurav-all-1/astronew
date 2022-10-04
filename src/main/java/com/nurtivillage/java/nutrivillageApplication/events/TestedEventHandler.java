package com.nurtivillage.java.nutrivillageApplication.events;

import org.apache.catalina.webresources.war.Handler;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class TestedEventHandler {
    
    @EventListener
    @Async
    public void handelEvent(TestedEvent<String> testedEvent) throws InterruptedException{
        Thread.sleep(5000); 
        System.out.println("hello sir");
    }
}
