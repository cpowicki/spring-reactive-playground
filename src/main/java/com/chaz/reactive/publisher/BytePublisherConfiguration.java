package com.chaz.reactive.publisher;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Configuration
@Slf4j
public class BytePublisherConfiguration {

    private static byte[] ALPHABET = "abcdefghijklmnopqrstuvwxyz".getBytes();

    @Bean
    public BatchedBytePublisher batchedBytePublisher() {
        Byte[] data = new Byte[26];
        Arrays.setAll(data, i -> Byte.valueOf(ALPHABET[i]));
        
        Flux<List<Byte>> flux = Flux.fromArray(data)
            .doOnNext(b -> log.info("{}", b))
            .bufferTimeout(40, Duration.ofSeconds(5L));
            // .doOnNext(batch -> log.info("{}", b))

        
        return new BatchedBytePublisher(flux);
    }
    
    @Bean
    public BytePublisher bytePublisher() {
        
        Byte[] data = new Byte[26];
        Arrays.setAll(data, i -> Byte.valueOf(ALPHABET[i]));
        
        Flux<Byte> flux = Flux.fromArray(data);
            // .publishOn(Schedulers.fromExecutorService(executorService))
            // .subscribeOn(Schedulers.fromExecutorService(executorService));

        return new BytePublisher(flux);
    }
}
