/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source.strategies;

import source.Source;
/**
 *
 * @author Pravin Roy
 */
public class PoissonPacketGenerationStrategy implements PacketGenerationStrategy{
    private double mean;

    public PoissonPacketGenerationStrategy(double mean) {
        this.mean = mean;
    }

    @Override
    public double getTimeToNextArrival(Source generator) {
        if(generator.isRunning()) {
            double n = generator.getPoissonRandom(mean);
            return n != 0 ? n : Source.MIN_ARRIVAL_INTERVAL;
        } else {
            generator.setRunning(true);
            if(generator.getStartTime() == 0)
                return getTimeToNextArrival(generator);
            return generator.getStartTime();
        }
    }
}
