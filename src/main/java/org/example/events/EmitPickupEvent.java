package org.example.events;

import org.example.constants.Constants;

public class EmitPickupEvent extends Event {

    EventType eventType = EventType.EMIT_EVENT_SIMULATION;
    private int floorOrigin;
    private Constants.Direction direction;

    public EmitPickupEvent(int floorOrigin, Constants.Direction direction) {
        this.floorOrigin = floorOrigin;
        this.direction = direction;
    }

    @Override
    public EventType getEventType() {
        return eventType;
    }

    public int getFloorOrigin() {
        return floorOrigin;
    }

    public void setFloorOrigin(int floorOrigin) {
        this.floorOrigin = floorOrigin;
    }

    public Constants.Direction getDirection() {
        return direction;
    }

    public void setDirection(Constants.Direction direction) {
        this.direction = direction;
    }
}
