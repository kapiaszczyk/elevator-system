package org.example.model;

import org.example.constants.Constants;
import org.example.event.EventType;
import org.example.event.FloorDispatchEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class EventBusTests {

    private EventBus eventBus;

    @Mock
    private Subscriber subscriber;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        eventBus = EventBus.getInstance();
    }

    @Test
    void givenSubscriber_whenSubscribe_thenSubscriberAdded() {
        EventType eventType = EventType.FLOOR_DISPATCH_EVENT;
        FloorDispatchEvent event = new FloorDispatchEvent(1, Constants.Direction.DOWN);

        eventBus.subscribe(eventType, subscriber);
        eventBus.publish(event);

        verify(subscriber, times(1)).handleEvent(event);
    }

    @Test
    void givenNoSubscriber_whenPublish_thenNoSubscriberCalled() {
        EventType eventType = EventType.FLOOR_DISPATCH_EVENT;
        FloorDispatchEvent event = new FloorDispatchEvent(1, Constants.Direction.DOWN);

        eventBus.publish(event);

        verify(subscriber, never()).handleEvent(event);
    }

    @Test
    void givenMultipleSubscribers_whenSubscribe_thenAllSubscribersReceiveEvent() {
        EventType eventType = EventType.FLOOR_DISPATCH_EVENT;
        FloorDispatchEvent event = new FloorDispatchEvent(1, Constants.Direction.DOWN);

        Subscriber subscriber1 = mock(Subscriber.class);
        Subscriber subscriber2 = mock(Subscriber.class);

        List<Subscriber> subscribers = new ArrayList<>();
        subscribers.add(subscriber1);
        subscribers.add(subscriber2);

        eventBus.subscribe(eventType, subscriber1);
        eventBus.subscribe(eventType, subscriber2);
        eventBus.publish(event);

        verify(subscriber1, times(1)).handleEvent(event);
        verify(subscriber2, times(1)).handleEvent(event);
    }
}
