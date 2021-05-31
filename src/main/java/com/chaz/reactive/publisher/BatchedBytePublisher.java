package com.chaz.reactive.publisher;

import java.util.List;

import org.reactivestreams.Subscriber;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

@AllArgsConstructor
public class BatchedBytePublisher {
    
    Flux<List<Byte>> producer;

    public void subscribe(Subscriber<List<Byte>> subscriber) {
        this.producer.subscribe(subscriber);
    }

    public Flux<List<Byte>> publisher() {
        return this.producer;
    }
}
