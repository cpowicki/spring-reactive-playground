package com.chaz.reactive;

import com.chaz.reactive.events.CompletedEvent;
import com.chaz.reactive.publisher.BytePublisher;
import com.chaz.reactive.subscriber.ByteSubscriber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LifeCycleHandler {

    @Autowired
	BytePublisher publisher;
    
    @Autowired
    @Qualifier("secondaryByteSubscriber")
    ByteSubscriber secondarySubscriber;

    @Autowired
    ByteSubscriber subscriber;

    @EventListener
    public void start(ApplicationStartedEvent ev) {
        log.info("Starting");
        publisher.subscribe(subscriber);
        publisher.subscribe(secondarySubscriber);
    }

    @EventListener
    public void shutdown(CompletedEvent ev) {
        log.info("Shutting down");
    }
}