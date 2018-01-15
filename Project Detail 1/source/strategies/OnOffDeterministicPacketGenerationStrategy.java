/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source.strategies;

import components.Clock;
import source.Source;
/**
 *
 * @author Pravin Roy
 */
public class OnOffDeterministicPacketGenerationStrategy implements PacketGenerationStrategy{
    private double onDuration;
    private double offDuration;
    private double intervalBetweenPackets;
    private double startOfNextBurst;
    private double startOfNextIdle;
    private double burstRateInBps;

    public OnOffDeterministicPacketGenerationStrategy(double onDuration, double offDuration, double burstRateInBps) {
        this.onDuration = onDuration;
        this.offDuration = offDuration;
        this.burstRateInBps = burstRateInBps;
    }

    @Override
    public double getTimeToNextArrival(Source generator) {
        double currentTime = Clock.getCurrentTime();
        if(!generator.isRunning() && currentTime >= generator.getStartTime()) {
            generator.setRunning(true);
            startOfNextBurst = currentTime;
            startOfNextIdle = startOfNextBurst + offDuration;
            intervalBetweenPackets =  generator.getPacketSizeInBytes()/burstRateInBps;
        }

        double difference = Math.abs(currentTime - startOfNextIdle);

        if(generator.isRunning() && currentTime < startOfNextIdle && difference > 1e-10) {
            return intervalBetweenPackets;
        } else {
            startOfNextBurst = currentTime + offDuration;
            startOfNextIdle = startOfNextBurst + onDuration;
            return offDuration;
        }
    }
    
}
