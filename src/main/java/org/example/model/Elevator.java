package org.example.model;

import org.example.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Elevator {

    private static int nextId = 0;

    private final int id;
    private final int speed;
    private State state;
    private final ArrayBlockingQueue<Integer> floorQueue;
    private volatile AtomicBoolean isActive;
    private final Thread elevatorThread;
    private final Logger LOGGER;

    public Elevator() {
        this.id = getNextId();
        this.speed = Constants.ELEVATOR_SPEED;
        this.state = new State(this.id, 0, 0);
        this.floorQueue = new ArrayBlockingQueue<>(100);
        this.isActive = new AtomicBoolean(false);
        this.elevatorThread = new Thread(this::processJobs);
        this.elevatorThread.start();
        this.LOGGER = LoggerFactory.getLogger(Elevator.class.getSimpleName());
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
            LOGGER.trace("Elevator {} is checking if it can process jobs... ({})", this.id, this.isActive.toString());

            while (isActive.get()) {
                try {
                    synchronized (elevatorThread) {
                        if (floorQueue.isEmpty()) {
                            continue;
                        }

                        int targetFloor = floorQueue.peek();

                        if (targetFloor == state.getCurrentFloor()) {
                            LOGGER.info("[{}] Already at floor: {}", this.id, targetFloor);
                            floorQueue.take();
                            this.setIsActive(false);
                            continue;
                        }

                        int distance = Math.abs(state.getCurrentFloor() - targetFloor);
                        int neededTime = distance / speed;
                        state.setDestinationFloor(targetFloor);

                        LOGGER.info("[{}] Moving from {} to {}. This will take {} seconds", this.id, state.getCurrentFloor(), targetFloor, neededTime);

                        Thread.sleep(neededTime * 1000L + 100L);

                        state.setCurrentFloor(targetFloor);
                        state.setDestinationFloor(-1);

                        LOGGER.info("[{}] Arrived at {}", this.id, state.getCurrentFloor());

                        floorQueue.take();

                        LOGGER.debug("[{}] Remaining in queue: {}", this.id, getFloorQueueContentsAsString());

                        this.setIsActive(false);

                    }

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            LOGGER.trace("Elevator {} is not active, stopped processing jobs...", this.id);

        }
    }


    private synchronized int getNextId() {
        return nextId++;
    }

    public int getId() {
        return this.id;
    }

    public void stop() {
        isActive.set(false);
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public AtomicBoolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        this.isActive.set(active);
    }

    public void updateState(int currentFloor, int destinationFloor) {
        this.state.setCurrentFloor(currentFloor);
        this.state.setDestinationFloor(destinationFloor);
    }

    private String getFloorQueueContentsAsString() {
        int size = floorQueue.size();
        Integer[] queueContents = floorQueue.toArray(new Integer[size]);
        return Arrays.toString(queueContents);
    }
}
