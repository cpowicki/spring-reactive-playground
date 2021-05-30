package com.chaz.reactive.publisher;

import org.reactivestreams.Subscriber;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

@AllArgsConstructor
public class BytePublisher {
    
    Flux<Byte> producer;
    
    public void subscribe(Subscriber<Byte> subscriber) {
        this.producer.subscribe(subscriber);
    }

    public Flux<Byte> publisher() {
        return this.producer;
    }
}
