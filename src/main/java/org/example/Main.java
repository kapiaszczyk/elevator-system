package org.example;

import org.example.constants.Constants;
import org.example.emitters.DispatchEventEmitter;
import org.example.events.Event;
import org.example.events.FloorDispatchEvent;
import org.example.events.UpdateSimulationEvent;
import org.example.simulation.SimulationController;
import org.example.system.ElevatorSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Logger LOGGER = LoggerFactory.getLogger(Main.class.getSimpleName());

        ElevatorSystem elevatorSystem = new ElevatorSystem();
        SimulationController simulationController = new SimulationController(elevatorSystem);

        Scanner scanner = new Scanner(System.in);

        simulationController.startRandomEventEmitter();

        // b - start
        // s - stop
        // f - forward
        // g - getstates
        // e - manual event
        // u - update

        System.out.println("Waiting for user input...");

        while (true) {
            if (scanner.hasNext()) {
                String input = scanner.nextLine();

                if (!input.isEmpty()) {
                    char key = input.charAt(0); // Get the first character entered

                    // Execute different functions based on the key pressed
                    switch (key) {
                        case 'b':
                            System.out.println("Start simulation");
                            break;
                        case 'f':
                            System.out.println("Forward simulation");
                            simulationController.forwardSimulation();
                            break;
                        case 's':
                            System.out.println("Stop simulation");
                            simulationController.stopSimulation();
                            simulationController.stopRandomEventEmitter();
                            break;
                        case 'g':
                            System.out.println("Get states");
                            simulationController.fetchStates();
                            break;
                        case 'e':
                            System.out.println("Manual event");
                            Event event = new FloorDispatchEvent(5, Constants.Direction.UP);
                            simulationController.emitEvent(event);
                            break;
                        case 'u':
                            System.out.println("Update elevator state");
                            Event event1 = new UpdateSimulationEvent(0, 0, 2);
                            simulationController.updateSimulation(event1);
                            break;
                        case 'q':
                            System.out.println("Exiting...");
                            simulationController.stopSimulation();
                            simulationController.stopRandomEventEmitter();
                            scanner.close();
                            System.exit(0); // Exit the program
                            break;
                        default:
                            System.out.println("Unknown key pressed.");
                            break;
                    }
                }
            }
        }
    }
}