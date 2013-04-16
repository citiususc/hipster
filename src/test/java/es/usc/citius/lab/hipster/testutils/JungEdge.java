/*
 * Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS).
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

package es.usc.citius.lab.hipster.testutils;

import edu.uci.ics.jung.graph.DirectedGraph;
import java.awt.Point;

/**
 * Class only for test purposes: definition of a JungEdge to create a
 * {@link DirectedGraph}.
 *
 * @author Adrián González Sieira
 * @since 26-03-2013
 * @version 1.0
 */
public class JungEdge<E> {

    private E source, dest;
    private Double cost;

    public JungEdge(E source, E dest, Double cost) {
        this.source = source;
        this.dest = dest;
        this.cost = cost;
    }

    public Double getCost() {
        return cost;
    }

    public E getSource() {
        return source;
    }

    public E getDest() {
        return dest;
    }
    
    @Override
    public String toString() {
    	return source + "->" + dest + " " + cost;
    }
}
