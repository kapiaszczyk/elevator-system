package org.example.emitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class EventEmitter {

    private final Thread thread;
    private final AtomicBoolean stopEvent;
    private final Logger LOGGER;

    public EventEmitter() {
        this.thread = new Thread(this::emitEvents);
        this.stopEvent = new AtomicBoolean(false);
        this.LOGGER = LoggerFactory.getLogger(getClass().getSimpleName());
    }

    public void start() {
        if (thread.isAlive()) {
            LOGGER.info("{} is already running.", getClass().getSimpleName());
            return;
        }
        thread.start();
        LOGGER.info("{} started.", getClass().getSimpleName());
    }

    public void stop() {
        if (thread.isAlive()) {
            stopEvent.set(true);
            try {
                thread.join();
            } catch (InterruptedException e) {
                LOGGER.warn("Interrupted while waiting for thread to join.");
                Thread.currentThread().interrupt();
            }
            LOGGER.info("{} stopped.", getClass().getSimpleName());
        } else {
            LOGGER.info("{} is not running.", getClass().getSimpleName());
        }
    }

    private void emitEvents() {
        Random random = new Random();
        while (!stopEvent.get()) {
            try {
                Thread.sleep(random.nextInt(5_000) + 1_000);
                generateEvent();
            } catch (InterruptedException e) {
                LOGGER.warn("Thread interrupted while emitting events.");
                Thread.currentThread().interrupt();
            }
        }
    }

    protected abstract void generateEvent();

}
