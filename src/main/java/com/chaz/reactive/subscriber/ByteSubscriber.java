package com.chaz.reactive.subscriber;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ByteSubscriber implements Subscriber<Byte> {
    int capacity;
    ByteBuffer buffer;
    byte[] out;
    
    Subscription subscription;

    public ByteSubscriber(int c) {
        capacity = c;
        buffer = ByteBuffer.allocate(c);
        out = new byte[c];
    }

    @Override
    public void onSubscribe(Subscription s) {
        this.subscription = s;
        subscription.request(10);
    }

    @Override
    public void onNext(Byte t) {
        log.info("recieved {}", t.byteValue());
        buffer.put(t.byteValue());
        
        if (buffer.capacity() == buffer.position()) {
            flush();
        } else {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                log.error("{}", e);
            }
        }
    }

    @Override
    public void onError(Throwable t) {
        log.error("{}", t);       
    }

    @Override
    public void onComplete() {
        flush();
        log.info("Completed");
    }   

    private void flush() {
        int last = buffer.position();
        buffer.flip().get(out, 0, last);
            
        String s = new String(out, StandardCharsets.UTF_8);
        log.info("{}", s.substring(0, last));
        
        subscription.request(capacity);

        buffer.clear();
    }
}
