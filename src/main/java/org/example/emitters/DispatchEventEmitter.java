package org.example.emitters;

import org.example.constants.Constants;
import org.example.events.Event;
import org.example.events.FloorDispatchEvent;
import org.example.events.InElevatorDispatchEvent;
import org.example.model.State;

import java.util.Random;

public class DispatchEventEmitter extends EventEmitter {

    private static final State.EventBus eventBus = State.EventBus.getInstance();

    public void step() {
        this.generateEvent();
    }

    @Override
    protected void generateEvent() {
        Random random = new Random();
        boolean isElevatorEvent = random.nextBoolean();
        int randomFloor = random.nextInt(Constants.NUM_FLOORS);

        if(isElevatorEvent) {
            int randomElevator = random.nextInt(Constants.NUM_ELEVATORS);
            Event event = new InElevatorDispatchEvent(randomElevator, randomFloor);
            eventBus.publish(event);
        } else {
            boolean isUpDirection = random.nextBoolean();
            Constants.Direction randomDirection;
            if (isUpDirection) {
                randomDirection = Constants.Direction.UP;
            } else  {
                randomDirection = Constants.Direction.DOWN;
            }
            Event event = new FloorDispatchEvent(randomFloor, randomDirection);
            eventBus.publish(event);
        }
    }

}
