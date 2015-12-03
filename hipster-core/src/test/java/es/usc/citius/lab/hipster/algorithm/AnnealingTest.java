package es.usc.citius.lab.hipster.algorithm;

import es.usc.citius.hipster.algorithm.localsearch.Annealing;
import es.usc.citius.hipster.model.Town;
import es.usc.citius.hipster.model.Trip;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/*
 * Copyright 2015 Centro de Investigación en Tecnoloxías da Información (CITIUS), University of Santiago de Compostela.
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

/**
 *
 * @author Martin
 */
public class AnnealingTest {
    
    @Test
    public void testAnnealing()
    {
        List<Town> towns = new ArrayList();
        
        towns.add(new Town(11, 22));
        towns.add(new Town(24, 45));
        towns.add(new Town(123, 34));
        
        Annealing newExample = new Annealing(50000, towns, 0.002);
        Trip trip = newExample.runAnnealing();
        
        assertEquals(trip.getTownDistance(), 237);
    }
    
    @Test
    public void testTown()
    {
        Town townOne = new Town(1,2);
        Town townTwo = new Town(3,4);
        
        assertEquals(townOne.getxCoord(), 1);
        assertEquals(townTwo.getyCoord(), 4);
        
        assertEquals(townOne.findDistanceToOtherTown(townTwo), Math.sqrt(8), 0.01f);
    }
    
    @Test
    public void testTrip()
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
        
        Trip trip = new Trip(towns);
        
        assertEquals(trip.getTownDistance(), 4746);
        
        for(int i = 0; i < towns.size(); i++)
        {
            assertEquals(towns.get(i).getxCoord(), trip.getTownList().get(i).getxCoord());
            assertEquals(towns.get(i).getyCoord(), trip.getTownList().get(i).getyCoord());
        }
        
        trip.setTown(1, new Town(1,1));
        assertEquals(trip.getTownList().get(1).getxCoord(), 1);
        assertEquals(trip.getTownList().get(1).getyCoord(), 1);
        
    }
}
