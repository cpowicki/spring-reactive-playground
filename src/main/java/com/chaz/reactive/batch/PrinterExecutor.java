package com.chaz.reactive.batch;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collector;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PrinterExecutor implements BatchExecutor<Byte> {

    @Override
    public void execute(List<Byte> items) {

        // if (new Random().nextBoolean()) {
        //     while (true) {}
        // }

        byte[] bytes = items.stream()
            .collect(
                Collector.of(
                    ByteArrayOutputStream::new, 
                    ByteArrayOutputStream::write, 
                    (one, two) -> {
                        try {
                            two.writeTo(one);
                        } catch (IOException e) {}
                        return two;
                    },
                    ByteArrayOutputStream::toByteArray
                )
            );

        log.info(new String(bytes));
    }
}
