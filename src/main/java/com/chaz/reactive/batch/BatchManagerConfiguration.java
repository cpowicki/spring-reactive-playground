package com.chaz.reactive.batch;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchManagerConfiguration {
    
    @Bean
    public BatchManager<Byte> byteBatchManager() {
        PrinterExecutor executor = new PrinterExecutor();
        return BatchManager.<Byte>builder()
            .threadCount(3)
            .batchSize(3)
            .lingerMs(200L)
            .executor(executor)
            .build();
    }
}