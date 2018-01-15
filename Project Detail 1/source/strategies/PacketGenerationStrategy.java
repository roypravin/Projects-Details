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
public interface PacketGenerationStrategy {
    double getTimeToNextArrival(Source generator);
    
}
