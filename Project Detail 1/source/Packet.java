/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;
import components.Clock;
/**
 *
 * @author Pravin Roy
 */
public class Packet {
    private final double arrivalTime;
    private final double virtualSpacingTimestamp;
    private final int size;

    public Packet(double virtualSpacingTimestamp, int sizeInBytes) {
        this(Clock.getCurrentTime(), virtualSpacingTimestamp, sizeInBytes);
    }

    public Packet(double arrivalTime, double virtualSpacingTimestamp, int sizeInBytes) {
        this.arrivalTime = arrivalTime;
        this.virtualSpacingTimestamp = virtualSpacingTimestamp;
        this.size = sizeInBytes;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public double getVirtualSpacingTimestamp() {
        return virtualSpacingTimestamp;
    }

    public int getSize() {
        return size;
    }
    
}
