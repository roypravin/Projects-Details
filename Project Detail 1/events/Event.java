/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package events;

/**
 *
 * @author Pravin Roy
 */
public class Event {
    public static final int SERVER_EVENT = -1;

    private final int queueId;
    private final EventType eventType;
    private final double time;

    public Event(EventType eventType, double time) {
        this.eventType = eventType;
        this.time = time;
        queueId = SERVER_EVENT;
    }

    public Event(EventType eventType, double time, int queueId) {
        this.eventType = eventType;
        this.time = time;
        this.queueId = queueId;
    }

    public int getQueueId() {
        return queueId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public double getTime() {
        return time;
    }
    
}
