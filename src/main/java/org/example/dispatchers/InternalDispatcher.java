package org.example.dispatchers;

import org.example.events.Event;
import org.example.events.FloorDispatchEvent;
import org.example.events.InElevatorDispatchEvent;
import org.example.model.Elevator;

public class InternalDispatcher implements Dispatcher {

    private int id;
    private Elevator elevator;

    public void handleEvent(Event event) {
        if(event instanceof FloorDispatchEvent) {
            elevator.addRequest(((FloorDispatchEvent) event).getFloorOrigin());
        } else if (event instanceof InElevatorDispatchEvent) {
            elevator.addRequest(((InElevatorDispatchEvent) event).getTargetFloor());
        }
    }

    public InternalDispatcher(Elevator elevator) {
        this.id = elevator.getId();
        this.elevator = elevator;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Elevator getElevator() {
        return elevator;
    }

    public void setElevator(Elevator elevator) {
        this.elevator = elevator;
    }

}
