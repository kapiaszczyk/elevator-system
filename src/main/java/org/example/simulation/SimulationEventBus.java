package org.example.simulation;

import org.example.events.Event;
import org.example.events.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimulationEventBus {

    private Subscriber subscriber;
    private static volatile SimulationEventBus instance;
    private static final Object mutex = new Object();
    private static final Logger LOGGER = LoggerFactory.getLogger(SimulationEventBus.class.getSimpleName());


    public static SimulationEventBus getInstance() {
        SimulationEventBus result = instance;
        if (result == null) {
            synchronized (mutex) {
                result = instance;
                if(result == null) instance = result = new SimulationEventBus();
            }
        }
        return result;
    }

    public void subscribe(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public void publish(Event event) {
        subscriber.handleEvent(event);
    }

}
