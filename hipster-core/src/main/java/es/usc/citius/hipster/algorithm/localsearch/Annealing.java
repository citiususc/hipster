/*
 * Copyright 2015 Centro de Investigaci�n en Tecnolox�as da Informaci�n (CITIUS), University of Santiago de Compostela.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package es.usc.citius.hipster.algorithm.localsearch;

import es.usc.citius.hipster.model.Town;
import es.usc.citius.hipster.model.Trip;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Martin
 */
public class Annealing {
    
    private double initialTemp;
    private List<Town> towns;
    private double cooling;

    public Annealing(double initialTemp, List<Town> towns, double cooling) {
        this.initialTemp = initialTemp;
        this.towns = towns;
        this.cooling = cooling;
    }
    
    public Trip runAnnealing()
    {
        Trip oldTrip = new Trip(towns);
        Trip solution = new Trip(towns);
        
        System.out.println("Intial trip total Distance: " + oldTrip.getTownDistance());
        
        for(double currentTemp = initialTemp; currentTemp > 1; currentTemp *= 1-cooling)
        {
            //Copy oldTrip to newTrip, but don't copy it such that it would cause pass by reference
            List<Town> towns = new ArrayList();
            towns.addAll(oldTrip.getTownList());
            Trip newTrip = new Trip(towns);
            
            //Genrate 2 random town indexes in array
            Random rand = new Random();
            int randTown1Index = rand.nextInt(newTrip.getTownList().size());
            
            rand = new Random();
            int randTown2Index = rand.nextInt(newTrip.getTownList().size());
            
            //Get 2 random towns
            Town randTown1 = newTrip.getTownList().get(randTown1Index);
            Town randTown2 = newTrip.getTownList().get(randTown2Index);
            
            //Swap the 2 towns
            newTrip.setTown(randTown2Index, randTown1);
            newTrip.setTown(randTown1Index, randTown2);
            
            //Find the 2 new trip distances
            int oldTripDistance = oldTrip.getTownDistance();
            int newTripDistance = newTrip.getTownDistance();
            
            //If the new trip is better, or is worse but we are early on in the search
            if(newTripDistance <= oldTripDistance || Math.exp((newTripDistance - oldTripDistance) / currentTemp)> Math.random())
            {
                oldTrip = new Trip(newTrip.getTownList());
            }
            
            //If the current trip is better, put that in as solution
            if(oldTrip.getTownDistance() < solution.getTownDistance())
            {
                solution = new Trip(oldTrip.getTownList());
            }
        }
        
        return solution;
    }
}
