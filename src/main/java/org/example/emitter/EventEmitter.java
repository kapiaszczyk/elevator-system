package org.example.emitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class EventEmitter {

    private final Thread thread;
    private volatile AtomicBoolean stopEvent = new AtomicBoolean(false);
    private final Logger LOGGER;

    public EventEmitter() {
        this.thread = new Thread(this::emitEvents);
        this.LOGGER = LoggerFactory.getLogger(getClass().getSimpleName());
    }

    public void start() {
        try {
            if (!thread.isAlive()) {
                thread.start();
                LOGGER.info("{} started.", getClass().getSimpleName());
            } else {
                LOGGER.info("{} is already running.", getClass().getSimpleName());
            }
        }
        catch (IllegalThreadStateException e) {
            LOGGER.error("Thread is already running.");
        }
    }

    public void stop() {
        if (thread.isAlive()) {
            stopEvent.set(true);
            try {
                thread.join();
                LOGGER.info("{} stopped.", getClass().getSimpleName());
            } catch (InterruptedException e) {
                LOGGER.warn("Interrupted while waiting for thread to join.");
                Thread.currentThread().interrupt();
            } finally {
                stopEvent.set(false);
            }
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

    public boolean isRunning() {
        return thread.isAlive();
    }

}
