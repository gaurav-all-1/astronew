package com.nurtivillage.java.geonixApplication.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class TestedEventPublisher {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void testPublish(){
        applicationEventPublisher.publishEvent(new TestedEvent<String>("event"));
    }
}
