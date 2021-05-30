package com.chaz.reactive.batch;

import java.util.List;

public interface BatchExecutor<T> {

    void execute(List<T> items);
    
}
