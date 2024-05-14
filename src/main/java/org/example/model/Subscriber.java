package org.example.model;

import org.example.events.Event;

public interface Subscriber {

    void handleEvent(Event event);

}
