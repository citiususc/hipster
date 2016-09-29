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
package es.usc.citius.hipster.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Martin
 */
public class Trip 
{
    private List<Town> townList;

    public Trip(List<Town> cityList) {
        this.townList = cityList;
    }
    
    public Trip(){
        townList = new ArrayList<Town>();
    }

    public List<Town> getTownList() {
        return townList;
    }
    
    public void setTown(int index, Town town)
    {
        townList.set(index, town);
    }
    
    public int getTownDistance()
    {
        int distance = 0;
        
        for(int i = 0; i < townList.size() - 1; i++ )
        {
            distance += (townList.get(i)).findDistanceToOtherTown(townList.get(i+1));
        }
        
        distance += (townList.get(townList.size() - 1)).findDistanceToOtherTown(townList.get(0));
        
        return distance;
    }
    
    
}
