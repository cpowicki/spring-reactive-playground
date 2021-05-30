package com.chaz.reactive.events;

import org.springframework.context.ApplicationEvent;

public class CompletedEvent extends ApplicationEvent {

    String msg;

    public CompletedEvent(Object source, String msg) {
        super(source);

        this.msg = msg;
    }
}
