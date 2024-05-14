package org.example.dispatcher;

import org.example.event.FloorDispatchEvent;
import org.example.event.InElevatorDispatchEvent;
import org.example.model.Elevator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InternalDispatcherTest {

    private InternalDispatcher internalDispatcher;
    private Elevator elevator;

    @Before
    public void setUp() {
        elevator = mock(Elevator.class);
        internalDispatcher = new InternalDispatcher(elevator);
    }

    @Test
    public void givenHandleEvent_whenFloorDispatchEvent_thenShouldAddFloorRequestToElevator() {
        FloorDispatchEvent event = mock(FloorDispatchEvent.class);
        int floorOrigin = 5;
        event.setFloorOrigin(floorOrigin);

        internalDispatcher.handleEvent(event);

        verify(elevator).addRequest(floorOrigin);
    }

    @Test
    public void givenHandleEvent_whenInElevatorDispatchEvent_thenShouldAddTargetFloorRequestToElevator() {
        InElevatorDispatchEvent event = mock(InElevatorDispatchEvent.class);
        int targetFloor = 10;
        event.setTargetFloor(targetFloor);

        internalDispatcher.handleEvent(event);

        verify(elevator).addRequest(targetFloor);
    }

    @Test
    public void givenInternalDispatcher_whenSetElevator_thenShouldSetElevatorCorrectly() {
        Elevator expectedElevator = mock(Elevator.class);

        internalDispatcher.setElevator(expectedElevator);

        Elevator actualElevator = internalDispatcher.getElevator();
        assertEquals(expectedElevator, actualElevator);
    }
}