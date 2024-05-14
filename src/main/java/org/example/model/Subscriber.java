package org.example.model;

import org.example.event.Event;

public interface Subscriber {

    void handleEvent(Event event);

}
