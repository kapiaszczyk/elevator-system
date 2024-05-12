package org.example;

import org.example.emitters.DispatchEventEmitter;
import org.example.system.ElevatorSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Logger LOGGER = LoggerFactory.getLogger(Main.class.getSimpleName());

        ElevatorSystem elevatorSystem = new ElevatorSystem();
        DispatchEventEmitter eventEmitter = new DispatchEventEmitter();

        try {
            eventEmitter.start();

            Scanner scanner = new Scanner(System.in);
            LOGGER.info("Running until a key is pressed...");

            while (!scanner.hasNext()) {
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        } finally {
            LOGGER.info("Shutting down...");
            eventEmitter.stop();
            elevatorSystem.stopAll();
        }

    }
}