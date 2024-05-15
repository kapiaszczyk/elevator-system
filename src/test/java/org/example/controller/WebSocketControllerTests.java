package org.example.controller;

import org.example.model.State;
import org.example.simulation.SimulationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class WebSocketControllerTests {

    @InjectMocks
    private WebSocketController webSocketController;

    @Mock
    private SimulationService simulationService;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenSendWebSocketMessage_whenSendWebSocketMessage_thenShouldSendWebSocketMessage() {
        List<State> states = new ArrayList<>();

        when(simulationService.fetchStates()).thenReturn(states);
        webSocketController.sendWebSocketMessage();

        verify(messagingTemplate, times(1)).convertAndSend("/elevator-states", states);
    }


}
