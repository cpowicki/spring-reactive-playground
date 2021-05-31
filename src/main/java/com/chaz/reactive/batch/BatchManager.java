package com.chaz.reactive.batch;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class BatchManager<T> implements Subscriber<T> {

    boolean active;
    
    long lingerMs;
    long startMs;
    
    int batchSize;

    ArrayList<T> batch;
    BatchExecutor<T> executor;

    ExecutorService executorService;
    Subscription subscription;

    Random random = new Random();

    @Builder
    public BatchManager(long lingerMs, int batchSize, int threadCount, BatchExecutor<T> executor) {
        
        this.lingerMs = lingerMs;
        this.batchSize = batchSize;
        
        this.executor = executor;
        this.executorService = Executors.newFixedThreadPool(threadCount);

        batch = new ArrayList<>(batchSize);
    }

    public void subscribe(Flux<T> publisher) {
       
        publisher.subscribeOn(
            Schedulers.single()
        ).subscribe(this);
        
        Executors.newSingleThreadExecutor().submit(() -> {
            
            while (active || this.batch.size() > 0) {
                if (System.currentTimeMillis() - startMs > lingerMs && this.batch.size() > 0) { 
                    this.sendBatch();
                    this.requestItems(batchSize);  
                }
            }

            this.executorService.shutdown();
        });
    }

    public void requestItems(int items) {
        this.subscription.request(items);
        this.startMs = System.currentTimeMillis();
    }

    @Override
    public void onSubscribe(Subscription s) {
        this.subscription = s;
        this.active = true;
        this.requestItems(batchSize);
    }

    @Override
    public void onNext(T t) {
        
        synchronized(this.batch) {
            batch.add(t); 
        }       

        if (batch.size() >= batchSize) {
            this.sendBatch();
            this.requestItems(batchSize);
        }
    }

    @Override
    public void onError(Throwable t) {
        log.error("{}", t);
    }

    @Override
    public void onComplete() {
        active = false;
        subscription.cancel();
    }

    private void sendBatch() {
        synchronized(this.batch) {
            if (this.batch.isEmpty()) return;

            @SuppressWarnings("unchecked")
            List<T> batchClone = (List<T>)batch.clone();
            
            executorService.submit(() -> executor.execute(batchClone));
            
            batch.clear();
        }
    }
}
