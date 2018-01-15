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
public class ExponentialPacketGenerationStrategy implements PacketGenerationStrategy{
    private double lambda;

    public ExponentialPacketGenerationStrategy(double lambda) {
        this.lambda = lambda;
    }

    @Override
    public double getTimeToNextArrival(Source generator) {
        if(generator.isRunning()) {
            double result = generator.getExpRandom(1/lambda);
            if (result != 0)
                return result;
            else
                return Source.MIN_ARRIVAL_INTERVAL;
        } else {
            generator.setRunning(true);
            if(generator.getStartTime() == 0)
                return getTimeToNextArrival(generator);
            return generator.getStartTime();
        }
    }
    
}
