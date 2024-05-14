package org.example.controller;

import org.example.event.FloorDispatchEvent;
import org.example.model.State;
import org.example.simulation.SimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class SimulationController {

    @Autowired
    private SimulationService service;

    @PostMapping("/api/v1/simulation/start")
    public ResponseEntity<String> startSimulation() {
        service.startSimulation();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/api/v1/simulation/stop")
    public ResponseEntity<String> stopSimulation() {
        service.stopSimulation();
        service.stopRandomEventEmitter();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/api/v1/simulation/forward")
    public ResponseEntity<String> forwardSimulation() {
        service.forwardSimulation();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/api/v1/simulation/random-event")
    public ResponseEntity<String> emitSingleRandomEvent() {
        service.emitSingleRandomEvent();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/api/v1/simulation/start-emitter")
    public ResponseEntity<String> startEmitter() {
        service.startRandomEventEmitter();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/api/v1/simulation/stop-emitter")
    public ResponseEntity<String> stopEmitter() {
        service.stopRandomEventEmitter();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/api/v1/simulation/start-auto")
    public ResponseEntity<String> startAutoMode() {
        service.startSimulationAuto();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/api/v1/simulation/stop-auto")
    public ResponseEntity<String> stopAutoMode() {
        service.stopSimulationAuto();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/v1/simulation/system/state")
    public ResponseEntity<List<State>> getState() {
        return new ResponseEntity<>(service.fetchStates(), HttpStatus.OK);
    }

    @PostMapping("/api/v1/simulation/system/event")
    public ResponseEntity<String> emitEvent(@RequestBody FloorDispatchEvent event) {
        service.emitEvent(event);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/api/v1/simulation/system/elevator/{id}/state/current-floor/{current}/destination-floor/{destination}")
    public ResponseEntity<String> updateElevatorState(@PathVariable("id") int elevatorId, @PathVariable("current") int currentFloor, @PathVariable("destination") int destinationFloor) {
        service.updateSimulation(elevatorId, currentFloor, destinationFloor);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/")
    public String renderIndex(Model model) {
        return "index";
    }

}
