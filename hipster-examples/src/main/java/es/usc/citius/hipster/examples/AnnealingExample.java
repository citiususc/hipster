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
package es.usc.citius.hipster.examples;

import es.usc.citius.hipster.algorithm.localsearch.Annealing;
import es.usc.citius.hipster.model.Town;
import es.usc.citius.hipster.model.Trip;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Martin
 */
public class AnnealingExample {
    
     public static void main(String[] args) throws IOException 
     {
        List<Town> towns = new ArrayList();
        
        towns.add(new Town(55, 234));
        towns.add(new Town(123, 566));
        towns.add(new Town(234, 466));
        towns.add(new Town(111, 777));
        towns.add(new Town(333, 342));
        towns.add(new Town(356, 574));
        towns.add(new Town(123, 909));
        towns.add(new Town(22, 55));
        towns.add(new Town(44, 78));
        towns.add(new Town(999, 111));
        
         
        Annealing newExample = new Annealing(50000, towns, 0.002);
        
        Trip trip = newExample.runAnnealing();
        
        for(Town town : trip.getTownList())
        {
            System.out.println(town.getxCoord() + ", " + town.getyCoord());
        }
        
        System.out.println("Final trip total Distance: " + trip.getTownDistance());
     }
}
