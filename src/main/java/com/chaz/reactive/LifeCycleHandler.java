package com.chaz.reactive;

import com.chaz.reactive.batch.BatchManager;
import com.chaz.reactive.events.CompletedEvent;
import com.chaz.reactive.publisher.BytePublisher;
import com.chaz.reactive.subscriber.ByteSubscriber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
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
    BatchManager<Byte> byteManager;

    @Autowired
    ByteSubscriber subscriber;

    @EventListener
    public void start(ApplicationReadyEvent ev) {
        log.info("Starting");
        byteManager.subscribe(publisher.publisher());
    }

    @EventListener
    public void shutdown(CompletedEvent ev) {
        log.info("Shutting down");
    }
}
