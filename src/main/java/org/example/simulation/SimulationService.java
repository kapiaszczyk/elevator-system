package org.example.simulation;

import org.example.constants.Constants;
import org.example.emitter.DispatchEventEmitter;
import org.example.event.Event;
import org.example.event.FloorDispatchEvent;
import org.example.model.State;
import org.example.system.ElevatorSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimulationService {

    ElevatorSystem elevatorSystem;
    DispatchEventEmitter dispatchEventEmitter = new DispatchEventEmitter();
    private volatile boolean simulationRunning = false;
    private volatile boolean simulationRunningAuto = false;
    private static final Logger LOGGER = LoggerFactory.getLogger(SimulationService.class.getSimpleName());

    @Autowired
    public SimulationService(ElevatorSystem elevatorSystem) {
        this.elevatorSystem = elevatorSystem;
    }

    public void forwardSimulation() {
        if (simulationRunning) {
            LOGGER.info("Forwarding simulation");
            elevatorSystem.step();
            dispatchEventEmitter.step();
        }
    }

    public void stopSimulation() {
        LOGGER.info("Stopping simulation");
        simulationRunning = false;
        elevatorSystem.stopAll();
        dispatchEventEmitter.stop();
    }

    public void startSimulation() {
        LOGGER.info("Starting simulation");
        simulationRunning = true;
    }

    public void emitSingleRandomEvent() {
        LOGGER.info("Emitting a single random event");
        if (simulationRunning) {
            dispatchEventEmitter.step();
        }
    }

    public void stopRandomEventEmitter() {
        if (simulationRunning) {
            LOGGER.info("Stopping random event emitter");
            dispatchEventEmitter.stop();
        }
    }

    public void startRandomEventEmitter() {
        if (simulationRunning) {
            LOGGER.info("Starting random event emitter");
            dispatchEventEmitter.start();
        }
    }

    public List<State> fetchStates() {
        return elevatorSystem.states();
    }

    public void updateSimulation(int elevatorId, int currentFloor, int destinationFloor) {
        elevatorSystem.update(elevatorId, currentFloor, destinationFloor);
        LOGGER.info("Received update: Elevator {}, currentFloor {}, destinationFloor {}", elevatorId, currentFloor, destinationFloor);
    }

    public void emitEvent(Event e) {

        FloorDispatchEvent event;

        if (e instanceof FloorDispatchEvent) {
            event = (FloorDispatchEvent) e;
        } else {
            throw new IllegalArgumentException();
        }

        int originFloor = event.getFloorOrigin();
        Constants.Direction direction = event.getDirection();

        LOGGER.info("Event received: FloorDispatchEvent with originFloor {} and direction {}", originFloor, direction);
        elevatorSystem.pickup(originFloor, direction);

    }

    public void startSimulationAuto() {
        LOGGER.info("Starting simulation in auto mode");
        if(simulationRunning) {
            stopSimulation();
        }
        dispatchEventEmitter.start();
        this.simulationRunningAuto = true;
    }

    public void stopSimulationAuto() {
        LOGGER.info("Stopping simulation in auto mode");
        dispatchEventEmitter.stop();
        this.simulationRunningAuto = false;

    }

    @Scheduled(fixedRate = 1000)
    public void forwardSimulationScheduled() {
        if (simulationRunningAuto) {
            LOGGER.info("Forwarding simulation automatically");
            elevatorSystem.step();
        }
    }

    public boolean isSimulationRunning() {
        return simulationRunning;
    }

    public boolean isSimulationRunningAuto() {
        return simulationRunningAuto;
    }

}
