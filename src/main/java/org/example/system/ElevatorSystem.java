package org.example.system;

import org.example.constants.Constants;
import org.example.model.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ElevatorSystem {

    private final SystemContext systemContext;

    @Autowired
    public ElevatorSystem() {
        systemContext = new SystemContext();
    }

    public void pickup(int originFloor, Constants.Direction direction) {
        this.systemContext.publishEvent(originFloor, direction);
    }

    public void update(int elevatorId, int currentFloor, int destinationFloor) {
        this.systemContext.updateElevatorStatus(elevatorId, currentFloor, destinationFloor);
    }

    public void step() {
        this.systemContext.forwardElevators();
    }

    public List<State> states() {
        return this.systemContext.getStates();
    }

    public void stopAll() {
        this.systemContext.stopElevators();
    }

}
