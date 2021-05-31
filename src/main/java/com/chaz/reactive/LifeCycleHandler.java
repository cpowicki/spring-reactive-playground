package com.chaz.reactive;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

import com.chaz.reactive.batch.BatchManager;
import com.chaz.reactive.events.CompletedEvent;
import com.chaz.reactive.publisher.BatchedBytePublisher;
import com.chaz.reactive.publisher.BytePublisher;
import com.chaz.reactive.subscriber.ByteSubscriber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Component
public class LifeCycleHandler {

    @Autowired
	BytePublisher publisher;

    @Autowired
    BatchedBytePublisher batchedPublisher;

    @Autowired
    BatchManager<Byte> byteManager;

    @Autowired
    ByteSubscriber subscriber;

    @EventListener
    public void start(ApplicationReadyEvent ev) {
        log.info("Starting");
        // byteManager.subscribe(publisher.publisher());

        // batchedPublisher.publisher()
        //     .subscribe(batch -> {
        //         byte[] data = new byte[batch.size()];
        //         for (int i = 0; i < batch.size(); i++) {
        //             data[i] = batch.get(i).byteValue();
        //         }
        //         String s = new String(data, StandardCharsets.UTF_8);
        //         log.info(s);
        //     });

        String[] data = new String[]{"a", "b", "c", "d", "e", "f", "g"};

        Flux.fromArray(data)
            .doOnNext(b -> log.info("{}", b))
            .bufferTimeout(2, Duration.ofSeconds(5L))
            .doOnNext(batch -> {})
            .subscribeOn(Schedulers.single())
            .doOnError(Exception.class, e -> log.error("{}", e))
            .subscribe();
    }

    @EventListener
    public void shutdown(CompletedEvent ev) {
        log.info("Shutting down");
    }
}
