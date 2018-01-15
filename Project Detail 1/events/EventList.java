/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package events;

import java.util.Comparator;
import java.util.PriorityQueue;
/**
 *
 * @author Pravin Roy
 */
public class EventList {
    private PriorityQueue<Event> events;

    public EventList() {
        this.events = new PriorityQueue<>(5, Comparator.comparingDouble(Event::getTime));
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public Event popNextEvent() {
        return events.poll();
    }

    public Event peekNextEvent() {
        return events.peek();
    }

    public boolean isEmpty() {
        return events.isEmpty();
    }
}
