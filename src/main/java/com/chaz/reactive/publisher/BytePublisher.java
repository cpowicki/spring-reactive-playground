package com.chaz.reactive.publisher;

import org.reactivestreams.Subscriber;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

@AllArgsConstructor
public class BytePublisher {
    
    Flux<Byte> producer;
    
    public void subscribe(Subscriber<Byte> subscriber) {
        this.producer.subscribe(subscriber);
    }
}
