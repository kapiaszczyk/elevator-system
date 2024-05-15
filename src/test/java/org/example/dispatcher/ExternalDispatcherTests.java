package org.example.dispatcher;

import org.example.event.FloorDispatchEvent;
import org.example.event.InElevatorDispatchEvent;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ExternalDispatcherTests {

    private ExternalDispatcher externalDispatcher;
    private ArrayList<InternalDispatcher> internalDispatchers;

    @Before
    public void setUp() {
        internalDispatchers = new ArrayList<>();
        internalDispatchers.add(mock(InternalDispatcher.class));
        internalDispatchers.add(mock(InternalDispatcher.class));
        internalDispatchers.add(mock(InternalDispatcher.class));
        externalDispatcher = new ExternalDispatcher(internalDispatchers);
    }

    @Test
    public void givenHandleEvent_whenFloorDispatchEvent_thenShouldPassToNextAvailableDispatcher() {
        FloorDispatchEvent event = mock(FloorDispatchEvent.class);
        int expectedDispatcherId = externalDispatcher.getLastCalledDispatcher() + 1;

        externalDispatcher.handleEvent(event);

        verify(internalDispatchers.get(expectedDispatcherId)).handleEvent(event);
        assertEquals(expectedDispatcherId, externalDispatcher.getLastCalledDispatcher());
    }

    @Test
    public void givenMaHandleEvent_whenManyFloorDispatchEvent_thenShouldPassToNextAvailableDispatcher() {
        FloorDispatchEvent event = mock(FloorDispatchEvent.class);
        int expectedDispatcherId = externalDispatcher.getLastCalledDispatcher() + 1;
        int firstDispatcherId = 0;
        int calls = 10;

        for(int i = 0; i < calls; i++) {
            externalDispatcher.handleEvent(event);
        }

        int expectedAmountOfCalls = (calls / internalDispatchers.size()) + 1;

        verify(internalDispatchers.get(firstDispatcherId), times(expectedAmountOfCalls)).handleEvent(event);
        assertEquals(expectedDispatcherId, externalDispatcher.getLastCalledDispatcher());
    }

    @Test
    public void givenHandleEvent_whenInElevatorDispatchEvent_thenShouldPassToDispatcher() {
        InElevatorDispatchEvent event = mock(InElevatorDispatchEvent.class);
        int elevatorId = 0;
        event.setElevatorOrigin(elevatorId);

        externalDispatcher.handleEvent(event);

        verify(internalDispatchers.get(elevatorId)).handleEvent(event);
    }


}