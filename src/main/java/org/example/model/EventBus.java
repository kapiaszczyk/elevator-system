package org.example.model;

import org.example.event.Event;
import org.example.event.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventBus {

    private final Map<EventType, List<Subscriber>> subscribers;
    private static volatile EventBus instance;
    private static final Object mutex = new Object();
    private static final Logger LOGGER = LoggerFactory.getLogger(EventBus.class.getSimpleName());

    private EventBus() {
        this.subscribers = new HashMap<>();
    }

    public static EventBus getInstance() {
        EventBus result = instance;
        if (result == null) {
            synchronized (mutex) {
                result = instance;
                if (result == null) instance = result = new EventBus();
            }
        }
        return result;
    }

    public void subscribe(EventType eventType, Subscriber subscriber) {
        List<Subscriber> eventSubscribers = subscribers.computeIfAbsent(eventType, k -> new ArrayList<>());
        eventSubscribers.add(subscriber);
    }

    public void publish(Event event) {
        EventType eventType = event.getEventType();
        if (subscribers.containsKey(eventType)) {
            LOGGER.debug("Publishing event: {}", event);
            List<Subscriber> eventSubscribers = subscribers.get(eventType);
            for (Subscriber subscriber : eventSubscribers) {
                subscriber.handleEvent(event);
            }
        }
    }

}
