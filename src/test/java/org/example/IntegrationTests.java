package org.example;

import org.example.constants.Constants;
import org.example.event.EventType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTests {

    String EMIT_EVENT_URL = "/api/v1/simulation/system/event";
    String START_SIMULATION_URL = "/api/v1/simulation/start";
    String STOP_SIMULATION_URL = "/api/v1/simulation/stop";
    String FORWARD_SIMULATION_URL = "/api/v1/simulation/forward";
    String START_SIMULATION_AUTO_URL = "/api/v1/simulation/start-auto";
    String STOP_SIMULATION_AUTO_URL = "/api/v1/simulation/stop-auto";
    String START_RANDOM_EVENT_EMITTER_URL = "/api/v1/simulation/start-emitter";
    String STOP_RANDOM_EVENT_EMITTER_URL = "/api/v1/simulation/stop-emitter";
    String RANDOM_EVENT_URL = "/api/v1/simulation/random-event";
    String STATE_URL = "/api/v1/simulation/system/state";


    @Autowired
    private MockMvc mockMvc;

    @Test
    public void givenStartSimulation_whenStartSimulation_thenStatus200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(START_SIMULATION_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void givenStopSimulation_whenStopSimulation_thenStatus200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(STOP_SIMULATION_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void givenForwardSimulation_whenForwardSimulation_thenStatus200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(FORWARD_SIMULATION_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void givenEmitSingleRandomEvent_whenEmitSingleRandomEvent_thenStatus200() throws Exception {

        int floorOrigin = 0;
        Constants.Direction direction = Constants.Direction.UP;
        EventType eventType = EventType.FLOOR_DISPATCH_EVENT;


        String jsonPayload = "{"
                + "\"eventType\": \"" + eventType + "\","
                + "\"floorOrigin\": " + floorOrigin + ","
                + "\"direction\": \"" + direction + "\""
                + "}";

        mockMvc.perform(MockMvcRequestBuilders.post(EMIT_EVENT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void givenEmitSingleRandomEventWithInvalidFloor_whenEmitSingleRandomEvent_thenStatus400() throws Exception {

        int floorOrigin = Constants.NUM_FLOORS + 1;
        Constants.Direction direction = Constants.Direction.UP;
        EventType eventType = EventType.FLOOR_DISPATCH_EVENT;


        String jsonPayload = "{"
                + "\"eventType\": \"" + eventType + "\","
                + "\"floorOrigin\": " + floorOrigin + ","
                + "\"direction\": \"" + direction + "\""
                + "}";

        mockMvc.perform(MockMvcRequestBuilders.post(EMIT_EVENT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void givenEmitSingleRandomEventWithNullDirection_whenEmitSingleRandomEvent_thenStatus400() throws Exception {

        int floorOrigin = 0;
        Constants.Direction direction = null;
        EventType eventType = EventType.FLOOR_DISPATCH_EVENT;


        String jsonPayload = "{"
                + "\"eventType\": \"" + eventType + "\","
                + "\"floorOrigin\": " + floorOrigin + ","
                + "\"direction\": \"" + direction + "\""
                + "}";

        mockMvc.perform(MockMvcRequestBuilders.post(EMIT_EVENT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void givenEmitSingleRandomEventWithNoType_whenEmitSingleRandomEvent_thenStatus400() throws Exception {

        int floorOrigin = 0;
        Constants.Direction direction = Constants.Direction.UP;
        EventType eventType = null;


        String jsonPayload = "{"
                + "\"eventType\": \"" + eventType + "\","
                + "\"floorOrigin\": " + floorOrigin + ","
                + "\"direction\": \"" + direction + "\""
                + "}";

        mockMvc.perform(MockMvcRequestBuilders.post(EMIT_EVENT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void givenUpdate_whenUpdate_thenStatus200() throws Exception {

        int elevatorId = 0;
        int currentFloor = 0;
        int destinationFloor = 0;

        String url = "/api/v1/simulation/system/elevator/" + elevatorId + "/state/current-floor/" + currentFloor + "/destination-floor/" + destinationFloor;

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void givenUpdateWithInvalidElevatorId_whenUpdate_thenStatus400() throws Exception {
        int invalidElevatorId = Constants.NUM_ELEVATORS + 1;

        String url = "/api/v1/simulation/system/elevator/" + invalidElevatorId + "/state/current-floor/0/destination-floor/0";

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void givenUpdateWithInvalidFloor_whenUpdate_thenStatus400() throws Exception {
        int invalidFloor = Constants.NUM_FLOORS + 1;

        String url = "/api/v1/simulation/system/elevator/0/state/current-floor/" + invalidFloor + "/destination-floor/0";

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void givenUpdateWithInvalidDestination_whenUpdate_thenStatus400() throws Exception {

        int invalidDestination = Constants.NUM_FLOORS + 1;

        String url = "/api/v1/simulation/system/elevator/0/state/current-floor/0/destination-floor/" + invalidDestination;

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void givenGetState_whenGetState_thenStatus200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(STATE_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void givenStartSimulationAuto_whenStartSimulationAuto_thenStatus200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(START_SIMULATION_AUTO_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void givenStopSimulationAuto_whenStopSimulationAuto_thenStatus200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(STOP_SIMULATION_AUTO_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void givenStartRandomEventEmitter_whenStartRandomEventEmitter_thenStatus200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(START_RANDOM_EVENT_EMITTER_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void givenStopRandomEventEmitter_whenStopRandomEventEmitter_thenStatus200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(STOP_RANDOM_EVENT_EMITTER_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void givenEmitRandomEvent_whenEmitRandomEvent_thenStatus200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(RANDOM_EVENT_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
