package org.example.simulation;

import org.example.constants.Constants;
import org.example.emitters.DispatchEventEmitter;
import org.example.events.*;
import org.example.model.State;
import org.example.system.ElevatorSystem;

import java.util.List;

public class SimulationController implements Subscriber {

    ElevatorSystem elevatorSystem;
    DispatchEventEmitter dispatchEventEmitter = new DispatchEventEmitter();
    private volatile boolean simulationRunning = false;
    private SimulationEventBus simulationEventBus = SimulationEventBus.getInstance();

    public SimulationController(ElevatorSystem elevatorSystem) {
        simulationEventBus.subscribe(this);
        this.elevatorSystem = elevatorSystem;
    }

    @Override
    public void handleEvent(Event event) {

        switch (event.getEventType()) {
            case EventType.START_SIMULATION:
                startSimulation();
                break;
            case EventType.STOP_SIMULATION:
                stopSimulation();
                break;
            case EventType.FORWARD_SIMULATION:
                forwardSimulation();
                break;
            case EventType.EMIT_EVENT_SIMULATION:
                emitEvent(event);
                break;
            case EventType.UPDATE_SIMULATION:
                updateSimulation(event);
                break;
            case EventType.FETCH_STATES_SIMULATION:
                fetchStates();
                break;
            case EventType.START_RANDOM_EVENT_EMITTER:
                startRandomEventEmitter();
            case EventType.STOP_RANDOM_EVENT_EMITTER:
                stopRandomEventEmitter();
            default:
                break;
        }
    }

    public void stopRandomEventEmitter() {
        if (simulationRunning) {
            dispatchEventEmitter.stop();
        }
    }

    public void startRandomEventEmitter() {
        if (simulationRunning) {
            dispatchEventEmitter.start();
        }
    }

    public List<State> fetchStates() {
        if (simulationRunning) {
            List<State> states = elevatorSystem.states();
            for (State state: states) {
                System.out.println(state);
            }
            return states;
        }
        return null;
    }

    public void updateSimulation(Event e) {

        UpdateSimulationEvent event;

        if (e instanceof UpdateSimulationEvent) {
            event = (UpdateSimulationEvent) e;
        }
        else {
            throw new IllegalArgumentException();
        }

        int elevator = event.getElevatorId();
        int currentFloor = event.getCurrentFloor();
        int destinationFloor = event.getDestinationFloor();

        if (simulationRunning) {
            elevatorSystem.update(elevator, currentFloor, destinationFloor);
        }
    }

    public void emitEvent(Event e) {

        EmitPickupEvent event;

        if (e instanceof EmitPickupEvent) {
            event= (EmitPickupEvent) e;
        } else {
            throw new IllegalArgumentException();
        }

        int originFloor = event.getFloorOrigin();
        Constants.Direction direction = event.getDirection();

        if (simulationRunning) {
            elevatorSystem.pickup(originFloor, direction);
        }
    }

    public void forwardSimulation() {
        if(!simulationRunning) {
            simulationRunning = true;
        }
        if (simulationRunning) {
            elevatorSystem.step();
            dispatchEventEmitter.step();
        }
    }

    public void stopSimulation() {
        simulationRunning = false;
        elevatorSystem.stopAll();
    }

    private void startSimulation() {
        // do nothing
    }

}
