package com.chaz.reactive;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class StartUpHandler {

    private static final byte[] data = new byte[3];

    @EventListener
    public void start(ApplicationStartedEvent ev) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(3);
        
        buffer.put("abc".getBytes());
        buffer.flip();
        
        buffer.get(data);
        buffer.clear();
        
        String bString = new String(data, StandardCharsets.UTF_8);
        log.info(bString);

        buffer.put("def".getBytes());
        buffer.flip();

        buffer.get(data);

        bString = new String(data, StandardCharsets.UTF_8);
        log.info(bString);
    }
}
