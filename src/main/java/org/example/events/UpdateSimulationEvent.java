package org.example.events;

public class UpdateSimulationEvent extends Event {

    EventType eventType = EventType.UPDATE_SIMULATION;
    private final int elevatorId;
    private final int currentFloor;
    private final int destinationFloor;

    public UpdateSimulationEvent(int elevatorId, int currentFloor, int destinationFloor) {
        this.elevatorId = elevatorId;
        this.currentFloor = currentFloor;
        this.destinationFloor = destinationFloor;
    }

    @Override
    public EventType getEventType() {
        return eventType;
    }

    public int getElevatorId() {
        return elevatorId;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public int getDestinationFloor() {
        return destinationFloor;
    }
}
