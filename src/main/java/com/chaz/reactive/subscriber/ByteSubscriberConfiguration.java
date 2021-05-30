package com.chaz.reactive.subscriber;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ByteSubscriberConfiguration {

    @Bean
    @Primary
    ByteSubscriber byteSubscriber() {
        return new ByteSubscriber(10);
    }

    @Bean
    ByteSubscriber secondaryByteSubscriber() {
        return new ByteSubscriber(10);
    }
}
