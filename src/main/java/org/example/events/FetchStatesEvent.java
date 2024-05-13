package org.example.events;

public class FetchStatesEvent extends Event {

    EventType eventType = EventType.FETCH_STATES_SIMULATION;

    @Override
    public EventType getEventType() {
        return eventType;
    }

}
