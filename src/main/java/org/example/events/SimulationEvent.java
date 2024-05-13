package org.example.events;

public class SimulationEvent extends Event {

    EventType eventType;

    @Override
    public EventType getEventType() {
        return eventType;
    }
}
