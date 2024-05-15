package org.example.system;

import org.example.constants.Constants;
import org.example.dispatcher.ExternalDispatcher;
import org.example.dispatcher.InternalDispatcher;
import org.example.event.Event;
import org.example.event.EventType;
import org.example.event.FloorDispatchEvent;
import org.example.model.Elevator;
import org.example.model.EventBus;
import org.example.model.State;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SystemContext {

    private final List<Elevator> elevators;
    private final ExternalDispatcher externalDispatcher;
    private static final EventBus eventBus = EventBus.getInstance();

    public SystemContext() {

        this.elevators = new ArrayList<>();
        ArrayList<InternalDispatcher> dispatchers = new ArrayList<>();

        for(int i = 0; i <  Constants.NUM_ELEVATORS; i++) {
            Elevator elevator = new Elevator();
            InternalDispatcher dispatcher = new InternalDispatcher(elevator);

            elevators.add(elevator);
            dispatchers.add(dispatcher);
        }

        this.externalDispatcher = new ExternalDispatcher(dispatchers);

        SystemContext.eventBus.subscribe(EventType.FLOOR_DISPATCH_EVENT, this.externalDispatcher);
        SystemContext.eventBus.subscribe(EventType.IN_ELEVATOR_DISPATCH_EVENT, this.externalDispatcher);

    }

    public void updateElevatorStatus(int elevatorId, int currentFloor, int destinationFloor) throws IllegalArgumentException {

        if(elevatorId < 0 || elevatorId >= Constants.NUM_ELEVATORS) {
            throw new IllegalArgumentException("Invalid elevator id");
        }
        if(currentFloor < 0 || currentFloor > Constants.NUM_FLOORS) {
            throw new IllegalArgumentException("Invalid current floor number");
        }
        if(destinationFloor < 0 || destinationFloor > Constants.NUM_FLOORS) {
            throw new IllegalArgumentException("Invalid destination floor number");
        }

        this.elevators.get(elevatorId).updateState(currentFloor, destinationFloor);
    }

    public void publishEvent(int origin, Constants.Direction direction) throws IllegalArgumentException {
        Event event = new FloorDispatchEvent(origin, direction);

        if(origin < 0 || origin > Constants.NUM_FLOORS) {
            throw new IllegalArgumentException("Invalid floor number");
        }
        if (direction == null) {
            throw new IllegalArgumentException("Invalid direction");
        }

        eventBus.publish(event);
    }

    public List<State> getStates() {
        List<State> states = new ArrayList<>();
        for (Elevator elevator: this.elevators) {
            states.add(elevator.getState());
        }
        return states;
    }

    public void stopElevators() {
        for (Elevator elevator: this.elevators) {
            elevator.stop();
        }
    }

    public void forwardElevators() {
        for (Elevator elevator : this.elevators) {
            elevator.setIsActive(true);
        }
    }

}
