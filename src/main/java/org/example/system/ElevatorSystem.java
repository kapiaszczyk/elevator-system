package org.example.system;

import org.example.constants.Constants;
import org.example.model.State;

import java.util.List;

public class ElevatorSystem {

    private final SystemContext systemContext;

    public ElevatorSystem() {
        systemContext = new SystemContext();
    }

    public void pickup(int originFloor, Constants.Direction direction) {
        this.systemContext.publishEvent(originFloor, direction);
    }

    public void update(int elevatorId, int currentFloor, int destinationFloor) {
        this.systemContext.updateElevatorStatus(elevatorId, currentFloor, destinationFloor);
    }

    public List<State> states() {
        return this.systemContext.getStates();
    }

    public void stopAll() {
        this.systemContext.stopElevators();
    }

    public SystemContext getSystemContext() {
        return systemContext;
    }
}
