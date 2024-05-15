package org.example.controller;

import org.example.constants.Constants;
import org.example.event.FloorDispatchEvent;
import org.example.model.State;
import org.example.simulation.SimulationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SimulationControllerTests {

    @Mock
    private SimulationService service;

    @InjectMocks
    private SimulationController controller;


    @Test
    public void givenStartSimulation_whenStartSimulation_thenShouldReturnOk() {
        ResponseEntity<String> response = controller.startSimulation();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service, times(1)).startSimulation();
    }

    @Test
    public void givenStopSimulation_whenStopSimulation_thenShouldReturnOk() {
        ResponseEntity<String> response = controller.stopSimulation();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service, times(1)).stopSimulation();
        verify(service, times(1)).stopRandomEventEmitter();
    }

    @Test
    public void givenForwardSimulation_whenForwardSimulation_thenShouldReturnOk() {
        ResponseEntity<String> response = controller.forwardSimulation();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service, times(1)).forwardSimulation();
    }

    @Test
    public void givenEmitSingleRandomEvent_whenEmitSingleRandomEvent_thenShouldReturnOk() {
        ResponseEntity<String> response = controller.emitSingleRandomEvent();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service, times(1)).emitSingleRandomEvent();
    }

    @Test
    public void givenEmitter_whenStartEmitter_thenShouldReturnOk() {
        ResponseEntity<String> response = controller.startEmitter();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service, times(1)).startRandomEventEmitter();
    }

    @Test
    public void givenEmitter_whenStopEmitter_thenShouldReturnOk() {
        ResponseEntity<String> response = controller.stopEmitter();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service, times(1)).stopRandomEventEmitter();
    }

    @Test
    public void givenAutoMode_whenStartAutoMode_thenShouldReturnOk() {
        ResponseEntity<String> response = controller.startAutoMode();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service, times(1)).startSimulationAuto();
    }

    @Test
    public void givenAutoMode_whenStopAutoMode_thenShouldReturnOk() {
        ResponseEntity<String> response = controller.stopAutoMode();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service, times(1)).stopSimulationAuto();
    }

    @Test
    public void givenState_whenGetState_thenShouldReturnState() {
        List<State> states = new ArrayList<>();
        when(service.fetchStates()).thenReturn(states);

        ResponseEntity<List<State>> response = controller.getState();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(states, response.getBody());
        verify(service, times(1)).fetchStates();
    }

    @Test
    public void givenFloorDispatchEvent_whenEmitEvent_thenShouldReturnOk() {
        FloorDispatchEvent event = new FloorDispatchEvent(1, Constants.Direction.DOWN);
        ResponseEntity<String> response = controller.emitEvent(event);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service, times(1)).emitEvent(event);
    }

    @Test
    public void givenElevator_whenUpdateElevatorState_thenShouldReturnOk() {
        int elevatorId = 1;
        int currentFloor = 2;
        int destinationFloor = 3;

        ResponseEntity<String> response = controller.updateElevatorState(elevatorId, currentFloor, destinationFloor);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service, times(1)).updateSimulation(elevatorId, currentFloor, destinationFloor);
    }

    @Test
    public void givenModel_whenRenderIndex_thenShouldReturnIndex() {
        String expectedViewName = "index";
        Model model = mock(Model.class);

        String viewName = controller.renderIndex(model);

        assertEquals(expectedViewName, viewName);
    }

}
