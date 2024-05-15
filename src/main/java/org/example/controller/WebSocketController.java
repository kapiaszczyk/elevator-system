package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.model.State;
import org.example.simulation.SimulationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.List;

@Tag(name = "Websocket controller", description = "This class is responsible for sending the elevator states to the subscribed clients.")
@Controller
public class WebSocketController {

    @Autowired
    private SimulationService service;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketController.class.getSimpleName());

    @Operation(summary = "Send elevator states to the subscribed clients.")
    @Scheduled(fixedRate = 100)
    public void sendWebSocketMessage() {
        try {
            List<State> states = service.fetchStates();
            messagingTemplate.convertAndSend("/elevator-states", states);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

    }

}