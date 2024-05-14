package org.example.event;

import org.example.constants.Constants;

public class FloorDispatchEvent extends DispatchEvent {

    public final EventType eventType = EventType.FLOOR_DISPATCH_EVENT;
    private int floorOrigin;
    private Constants.Direction direction;

    @Override
    public EventType getEventType() {
        return eventType;
    }

    public FloorDispatchEvent(int floorOrigin, Constants.Direction direction) {
        this.floorOrigin = floorOrigin;
        this.direction = direction;
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

    @Override
    public String toString() {
        return "FloorDispatchEvent{" +
                "floorOrigin=" + floorOrigin +
                ", direction=" + direction +
                '}';
    }
}
