package org.example.model;

import org.example.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.LinkedBlockingDeque;

public class Elevator {

    private static int nextId = 0;
    private final int id;
    private final int speed = Constants.ELEVATOR_SPEED;
    private State state;
    private final LinkedBlockingDeque<Integer> floorQueue = new LinkedBlockingDeque<>();
    private volatile boolean isActive = false;
    private final Object lock = new Object();
    private final Thread elevatorThread;
    private final Logger LOGGER = LoggerFactory.getLogger(Elevator.class.getSimpleName());

    public Elevator() {
        this.id = getNextId();
        this.state = new State(this.id, 0, 0);
        this.elevatorThread = new Thread(this::processJobs);
        this.elevatorThread.start();
    }

    public void addRequest(int floor) {
        try {
            floorQueue.put(floor);
            LOGGER.debug("[{}] Floor {} added to the queue of elevator {}", this.id, floor, this.id);
            LOGGER.debug("[{}] Queue is {}", this.id, getFloorQueueContentsAsString());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void processJobs() {
        while (true) {

            LOGGER.trace("Elevator {} is checking if it can process jobs... ({})", this.id, this.isActive);

            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            synchronized (lock) {
                while (!isActive) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        LOGGER.error("[{}] Elevator thread interrupted", this.id);
                        return;
                    }
                }

                processNextRequest();

            }
        }
    }

    private void processNextRequest() {
        try {
            if (floorQueue.isEmpty()) {
                return;
            }

            int targetFloor = floorQueue.peek();

            if (targetFloor == state.getCurrentFloor()) {
                LOGGER.info("[{}] Already at floor: {}", this.id, targetFloor);
                floorQueue.take();
                this.setIsActive(false);
                return;
            }

            moveToFloor(targetFloor);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.error("[{}] Elevator moving interrupted", this.id);
        }
    }

    private void moveToFloor(int targetFloor) throws InterruptedException {

        double distance = Math.abs(state.getCurrentFloor() - targetFloor);
        double neededTime = distance / speed;

        state.setDestinationFloor(targetFloor);

        LOGGER.info("[{}] Moving from {} to {}. This will take {} seconds", this.id, state.getCurrentFloor(), targetFloor, neededTime);

        Thread.sleep((long) (neededTime * 1000L + 100L));

        state.setCurrentFloor(targetFloor);
        LOGGER.info("[{}] Arrived at {}", this.id, state.getCurrentFloor());
        floorQueue.take();
        LOGGER.debug("[{}] Remaining in queue: {}", this.id, getFloorQueueContentsAsString());

        this.setIsActive(false);
    }

    private synchronized int getNextId() {
        return nextId++;
    }

    public int getId() {
        return this.id;
    }

    public void stop() {
        isActive = false;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
        synchronized (lock) {
            lock.notify();
        }
    }

    public void updateState(int currentFloor, int destinationFloor) {
        this.state.setCurrentFloor(currentFloor);
        this.state.setDestinationFloor(destinationFloor);
        this.floorQueue.addFirst(destinationFloor);
    }

    private String getFloorQueueContentsAsString() {
        int size = floorQueue.size();
        Integer[] queueContents = floorQueue.toArray(new Integer[size]);
        return Arrays.toString(queueContents);
    }
}
