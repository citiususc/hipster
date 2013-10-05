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

import java.awt.Point;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import es.usc.citius.lab.hipster.function.CostFunction;
import es.usc.citius.lab.hipster.function.HeuristicFunction;
import es.usc.citius.lab.hipster.function.impl.CostOperator;
import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.node.Transition;
import es.usc.citius.lab.hipster.algorithm.multiobjective.maze.Maze2D;

public class JungMazeHeuristicProblem implements HeuristicSearchProblem<Point, Double> {
	private final DirectedGraph<Point, JungEdge<Point>> graph;
	private final Maze2D maze;
	private boolean useHeuristic;
	
	public JungMazeHeuristicProblem(Maze2D maze, boolean useHeuristic){
		this.maze = maze;
		this.graph = createGraphFrom(maze);
		this.useHeuristic = useHeuristic;
	}
	
	public TransitionFunction<Point> getTransitionFunction() {
		return new TransitionFunction<Point>() {
			public Iterable<Transition<Point>> from(Point current) {
				return Transition.map(current, graph.getNeighbors(current));
			}
		};
	}

	public CostFunction<Point, Double> getCostFunction() {
		return new CostFunction<Point, Double>() {
			public Double evaluate(Transition<Point> transition) {
				return new Double(graph.findEdge(transition.from(), transition.to()).getCost());
			}
		};
	}

	public HeuristicFunction<Point, Double> getHeuristicFunction() {
		return new HeuristicFunction<Point, Double>() {
			public Double estimate(Point state) {
				if (useHeuristic){
					return state.distance(maze.getGoalLoc());
				} else {
					return CostOperator.doubleAdditionOp().getIdentityElem();
				}
			}
		};
	}

	public Point getInitialState() {
		return maze.getInitialLoc();
	}

	public Point getGoalState() {
		return maze.getGoalLoc();
	}
	
	public static DirectedGraph<Point, JungEdge<Point>> createGraphFrom(Maze2D maze) {
        // Create a graph from maze
        DirectedGraph<Point, JungEdge<Point>> graph = new DirectedSparseGraph<Point, JungEdge<Point>>();
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
                JungEdge<Point> e = new JungEdge<Point>(source, dest, edgeCost);
                if (!graph.containsEdge(e)) {
                    graph.addEdge(e, source, dest);
                }
            }
        }
        return graph;
    }

	public CostOperator<Double> getAccumulator() {
		return CostOperator.doubleAdditionOp();
	}

	
	

}
