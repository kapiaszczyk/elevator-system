package org.example.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ElevatorTests {

    @Test
    public void givenElevator_whenUpdateState_thenStateUpdated() {
        Elevator elevator = new Elevator();
        State newState = new State(elevator.getId(), 3, 8);

        elevator.updateState(3, 8);

        State currentState = elevator.getState();
        assertEquals(newState.getCurrentFloor(), currentState.getCurrentFloor());
        assertEquals(newState.getDestinationFloor(), currentState.getDestinationFloor());
    }
}
