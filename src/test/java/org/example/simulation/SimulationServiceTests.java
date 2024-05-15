package org.example.simulation;

import org.example.constants.Constants;
import org.example.emitter.DispatchEventEmitter;
import org.example.event.Event;
import org.example.event.FloorDispatchEvent;
import org.example.model.State;
import org.example.system.ElevatorSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SimulationServiceTests {

    @Mock
    private ElevatorSystem elevatorSystem;

    @Mock
    private DispatchEventEmitter dispatchEventEmitter;

    @InjectMocks
    private SimulationService simulationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        simulationService = new SimulationService(elevatorSystem);
        simulationService.dispatchEventEmitter = dispatchEventEmitter;
    }

    @Test
    void givenForwardSimulation_whenForwardSimulation_shouldStepElevatorSystem() {
        simulationService.startSimulation();

        simulationService.forwardSimulation();

        verify(elevatorSystem, times(1)).step();
        verify(dispatchEventEmitter, times(1)).step();
    }

    @Test
    void givenStoppedSimulation_whenForwardSimulation_thenSimulationNotForwarded() {
        simulationService.stopSimulation();

        simulationService.forwardSimulation();

        verify(elevatorSystem, never()).step();
        verify(dispatchEventEmitter, never()).step();
    }

    @Test
    void givenStopSimulation_whenStopSimulation_thenElevatorSystemStoppedAndDispatchEventEmitterStopped() {
        simulationService.stopSimulation();

        verify(elevatorSystem, times(1)).stopAll();
        verify(dispatchEventEmitter, times(1)).stop();
    }

    @Test
    void givenStartSimulation_whenSimulationStarted_thenSimulationIsRunning() {
        simulationService.startSimulation();

        assertTrue(simulationService.isSimulationRunning());
    }

    @Test
    void emitSingleRandomEvent_shouldStepDispatchEventEmitterWhenSimulationRunning() {
        simulationService.startSimulation();

        simulationService.emitSingleRandomEvent();

        verify(dispatchEventEmitter, times(1)).step();
    }

    @Test
    void emitSingleRandomEvent_shouldNotStepDispatchEventEmitterWhenSimulationNotRunning() {
        simulationService.stopSimulation();

        simulationService.emitSingleRandomEvent();

        verify(dispatchEventEmitter, never()).step();
    }

    @Test
    void stopRandomEventEmitter_shouldStopDispatchEventEmitterWhenSimulationRunning() {
        simulationService.startSimulation();

        simulationService.stopRandomEventEmitter();

        verify(dispatchEventEmitter, times(1)).stop();
    }

    @Test
    void startRandomEventEmitter_shouldStartDispatchEventEmitterWhenSimulationRunning() {
        simulationService.startSimulation();
        simulationService.startRandomEventEmitter();

        verify(dispatchEventEmitter, times(1)).start();
    }

    @Test
    void startRandomEventEmitter_shouldNotStartDispatchEventEmitterWhenSimulationNotRunning() {
        simulationService.stopSimulation();

        simulationService.startRandomEventEmitter();

        verify(dispatchEventEmitter, never()).start();
    }

    @Test
    void fetchStates_shouldReturnListOfStatesFromElevatorSystem() {
        List<State> expectedStates = List.of(new State(0, 0, 0), new State(1, 0, 0));
        when(elevatorSystem.states()).thenReturn(expectedStates);

        List<State> actualStates = simulationService.fetchStates();

        assertEquals(expectedStates, actualStates);
    }

    @Test
    void updateSimulation_shouldUpdateElevatorSystemAndLogReceivedUpdate() {
        int elevatorId = 1;
        int currentFloor = 2;
        int destinationFloor = 3;

        simulationService.updateSimulation(elevatorId, currentFloor, destinationFloor);

        verify(elevatorSystem, times(1)).update(elevatorId, currentFloor, destinationFloor);
    }

    @Test
    void givenEmitEvent_whenPickupFromElevatorSystem_thenPickupCalled() {
        int originFloor = 1;
        Constants.Direction direction = Constants.Direction.UP;
        Event event = new FloorDispatchEvent(originFloor, direction);

        simulationService.emitEvent(event);

        verify(elevatorSystem, times(1)).pickup(originFloor, direction);
    }

    @Test
    void emitEvent_shouldThrowIllegalArgumentExceptionWhenEventIsNotFloorDispatchEvent() {
        Event event = mock(Event.class);

        assertThrows(IllegalArgumentException.class, () -> simulationService.emitEvent(event));
    }

    @Test
    void startSimulationAuto_shouldStartDispatchEventEmitterAndSetSimulationRunningAutoToTrue() {
        simulationService.startSimulationAuto();

        verify(dispatchEventEmitter, times(1)).start();
        assertTrue(simulationService.isSimulationRunningAuto());
    }

    @Test
    void stopSimulationAuto_shouldStopDispatchEventEmitterAndSetSimulationRunningAutoToFalse() {
        simulationService.stopSimulationAuto();

        verify(dispatchEventEmitter, times(1)).stop();
        assertFalse(simulationService.isSimulationRunningAuto());
    }

}
