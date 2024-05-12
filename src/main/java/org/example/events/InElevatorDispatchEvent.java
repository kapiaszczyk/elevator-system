package org.example.events;

public class InElevatorDispatchEvent extends DispatchEvent {

    public final EventType eventType = EventType.IN_ELEVATOR_DISPATCH_EVENT;
    private int elevatorOrigin;
    private int targetFloor;

    @Override
    public EventType getEventType() {
        return eventType;
    }

    public InElevatorDispatchEvent(int elevatorOrigin, int targetFloor) {
        this.elevatorOrigin = elevatorOrigin;
        this.targetFloor = targetFloor;
    }

    public int getElevatorOrigin() {
        return elevatorOrigin;
    }

    public void setElevatorOrigin(int elevatorOrigin) {
        this.elevatorOrigin = elevatorOrigin;
    }

    public int getTargetFloor() {
        return targetFloor;
    }

    public void setTargetFloor(int targetFloor) {
        this.targetFloor = targetFloor;
    }

    @Override
    public String toString() {
        return "InElevatorDispatchEvent{" +
                "elevatorOrigin=" + elevatorOrigin +
                ", targetFloor=" + targetFloor +
                '}';
    }
}
