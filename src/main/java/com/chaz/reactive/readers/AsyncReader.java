package com.chaz.reactive.readers;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AsyncReader implements Reader, CompletionHandler<Integer, ByteBuffer> {

    int bytesRead;
    long position;
    AsynchronousFileChannel asyncFileChannel;
    Consumer<ByteBuffer> consumer;

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        this.bytesRead = result;

        if (this.bytesRead < 0) return;

        buffer.flip();
        
        byte[] data = new byte[buffer.limit()];
        buffer.get(data);

        consumer.accept(ByteBuffer.wrap(data));
        buffer.clear();
        
        this.position += this.bytesRead;

        this.asyncFileChannel.read(buffer, this.position, buffer, this);
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        log.error("{}", exc);
    }

    @Override
    public void read(File file, Consumer<ByteBuffer> consumer) throws IOException {
        this.consumer = consumer;

        this.asyncFileChannel = AsynchronousFileChannel.open(
            file.toPath(), Collections.singleton(StandardOpenOption.READ), executorService
        );

        ByteBuffer buffer = ByteBuffer.allocate(FileCopyUtils.BUFFER_SIZE);

        this.asyncFileChannel.read(buffer, this.position, buffer, this);

        while (this.bytesRead > 0) {
            this.position = this.position + this.bytesRead;
            this.asyncFileChannel.read(buffer, this.position, buffer, this);
        }
    }
    
}
