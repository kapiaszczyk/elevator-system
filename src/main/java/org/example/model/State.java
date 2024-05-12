package org.example.model;

public class State {

    private int elevatorId;
    private int currentFloor;
    private int destinationFloor;

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

    public int getDestinationFloor() {
        return destinationFloor;
    }

    public void setDestinationFloor(int destinationFloor) {
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
}
