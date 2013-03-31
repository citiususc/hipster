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
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import es.usc.citius.lab.hipster.util.maze.StringMaze;
import java.awt.Point;

/**
 * Class to obtain an instance of {@link DirectedGraph} (JUNG library) from an
 * instance of {@link StringMaze}.
 *
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @author Pablo Rodríguez Mier <pablo.rodriguez.mier@usc.es>
 * @since 26-03-2013
 * @version 1.0
 */
public class JungDirectedGraphFromMazeCreator {

    /**
     * Conversion process.
     *
     * @param maze instance of {@link StringMaze}
     * @return instance of {@link DirectedGraph}
     */
    public static DirectedGraph<Point, JungEdge> create(StringMaze maze) {
        // Create a graph from maze
        DirectedGraph<Point, JungEdge> graph = new DirectedSparseGraph<Point, JungEdge>();
        // Convert maze to graph. For each cell, add all valid neighbors with
        // their costs
        for (Point source : maze.getMazePoints()) {
            if (!graph.containsVertex(source)) {
                graph.addVertex(source);
            }
            for (Point dest : maze.validLocationsFrom(source)) {
                if (!graph.containsVertex(dest)) {
                    graph.addVertex(dest);
                }
                double edgeCost = Math.sqrt((source.x - dest.x)
                        * (source.x - dest.x) + (source.y - dest.y)
                        * (source.y - dest.y));
                JungEdge e = new JungEdge(source, dest, edgeCost);
                if (!graph.containsEdge(e)) {
                    graph.addEdge(e, source, dest);
                }
            }
        }
        return graph;
    }
}
