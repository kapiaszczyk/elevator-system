package org.example.model;

import org.example.events.Event;
import org.example.events.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class State {

    private int elevatorId;
    private int currentFloor;
    private Integer destinationFloor;

    public State(int elevatorId, int currentFloor, int destinationFloor) {
        this.elevatorId = elevatorId;
        this.currentFloor = currentFloor;
        this.destinationFloor = destinationFloor;
    }

    public int getElevatorId() {
        return elevatorId;
    }

    public void setElevatorId(int elevatorId) {
        this.elevatorId = elevatorId;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public Integer getDestinationFloor() {
        return destinationFloor;
    }

    public void setDestinationFloor(Integer destinationFloor) {
        this.destinationFloor = destinationFloor;
    }

    @Override
    public String toString() {
        return "State{" +
                "elevatorId=" + elevatorId +
                ", currentFloor=" + currentFloor +
                ", destinationFloor=" + destinationFloor +
                '}';
    }

    public static class EventBus {

        private final Map<EventType, List<Subscriber>> subscribers;
        private static volatile EventBus instance;
        private static final Object mutex = new Object();
        private static final Logger LOGGER = LoggerFactory.getLogger(EventBus.class.getSimpleName());

        private EventBus(){
            this.subscribers = new HashMap<>();
        }

        public static EventBus getInstance() {
            EventBus result = instance;
            if (result == null) {
                synchronized (mutex) {
                    result = instance;
                    if(result == null) instance = result = new EventBus();
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
}
