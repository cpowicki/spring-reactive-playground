package com.chaz.reactive.publisher;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import reactor.core.publisher.Flux;

@Configuration
public class BytePublisherConfiguration {

    @Autowired
    ExecutorService executorService;
    
    @Bean
    public BytePublisher bytePublisher() {
        byte[] ALPHABET = "abcdefghijklmnopqrstuvwxyz".getBytes();
        
        Byte[] data = new Byte[26];
        Arrays.setAll(data, i -> Byte.valueOf(ALPHABET[i]));
        
        Flux<Byte> flux = Flux.fromArray(data);
            // .publishOn(Schedulers.fromExecutorService(executorService))
            // .subscribeOn(Schedulers.fromExecutorService(executorService));

        return new BytePublisher(flux);
    }
}
