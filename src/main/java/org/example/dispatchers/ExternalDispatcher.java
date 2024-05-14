package org.example.dispatchers;

import org.example.events.Event;
import org.example.events.FloorDispatchEvent;
import org.example.events.InElevatorDispatchEvent;
import org.example.model.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ExternalDispatcher implements Dispatcher, Subscriber {

    private ArrayList<InternalDispatcher> internalDispatchers;
    int lastCalledDispatcher;
    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalDispatcher.class.getSimpleName());

    @Override
    public void handleEvent(Event event) {
        if (event instanceof FloorDispatchEvent) {
            passToNextAvailableDispatcher((FloorDispatchEvent) event);
        }
        else if (event instanceof InElevatorDispatchEvent) {
            passToDispatcher((InElevatorDispatchEvent) event);
        }
    }

    private void passToDispatcher(InElevatorDispatchEvent event) {
        internalDispatchers.get(event.getElevatorOrigin()).handleEvent(event);
        LOGGER.debug("Dispatching InElevatorDispatchEvent to dispatcher: {}", event.getElevatorOrigin());
    }

    private void passToNextAvailableDispatcher(FloorDispatchEvent event) {
        lastCalledDispatcher = (lastCalledDispatcher + 1) % internalDispatchers.size();
        internalDispatchers.get(lastCalledDispatcher).handleEvent(event);
        LOGGER.debug("Dispatching FloorDispatchEvent to next available dispatcher: {}", lastCalledDispatcher);
    }

    public ExternalDispatcher(ArrayList<InternalDispatcher> internalDispatchers) {
        this.internalDispatchers = internalDispatchers;
        this.lastCalledDispatcher = -1;
    }

    public List<InternalDispatcher> getInternalDispatchers() {
        return internalDispatchers;
    }

    public void setInternalDispatchers(ArrayList<InternalDispatcher> internalDispatchers) {
        this.internalDispatchers = internalDispatchers;
    }

    public int getLastCalledDispatcher() {
        return lastCalledDispatcher;
    }

    public void setLastCalledDispatcher(int lastCalledDispatcher) {
        this.lastCalledDispatcher = lastCalledDispatcher;
    }
}
