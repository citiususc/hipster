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

import es.usc.citius.lab.hipster.algorithm.ADStar;
import es.usc.citius.lab.hipster.algorithm.AStar;
import es.usc.citius.lab.hipster.function.CostFunction;
import es.usc.citius.lab.hipster.function.HeuristicFunction;
import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.node.ADStarNode;
import es.usc.citius.lab.hipster.node.ConsistentADStarNodeBuilder;
import es.usc.citius.lab.hipster.node.AStarDoubleNodeBuilder;
import es.usc.citius.lab.hipster.node.DefaultADStarNodeBuilder;
import es.usc.citius.lab.hipster.node.InconsistentADStarNodeBuilder;
import es.usc.citius.lab.hipster.node.NodeBuilder;
import es.usc.citius.lab.hipster.node.Transition;
import es.usc.citius.lab.hipster.util.maze.StringMaze;
import java.awt.Point;

/**
 * This class creates the iterators for different algorithms using
 * {@link StringMaze} as base.
 *
 * @author Adrián González Sieira
 * @since 26-03-2013
 * @version 1.0
 */
public class AlgorithmIteratorFromMazeCreator {

    public static AStar<Point> astar(final StringMaze maze, boolean useHeuristic) {
        HeuristicFunction<Point, Double> heuristic = defaultHeuristicFunction(maze);

        CostFunction<Point, Double> cost = defaultCostFunction();

        TransitionFunction<Point> transition = defaultTransitionFunction(maze);

        AStar<Point> it;
        if (useHeuristic) {
            it = new AStar<Point>(maze.getInitialLoc(), transition, new AStarDoubleNodeBuilder<Point>(cost, heuristic));
        } else {
            it = new AStar<Point>(maze.getInitialLoc(), transition, new AStarDoubleNodeBuilder<Point>(cost));
        }
        return it;
    }
    
    public static ADStar<Point> adstar(final StringMaze maze){
        HeuristicFunction<Point, Double> heuristic = defaultHeuristicFunction(maze);

        CostFunction<Point, Double> cost = defaultCostFunction();

        TransitionFunction<Point> transition = defaultTransitionFunction(maze);
        
        NodeBuilder<Point, ADStarNode<Point>> defaultBuilder = new DefaultADStarNodeBuilder<Point>();
        
        return new ADStar<Point>(maze.getInitialLoc(), 
                maze.getGoalLoc(), 
                transition, 
                heuristic,
                defaultBuilder,
                new ConsistentADStarNodeBuilder<Point>(defaultBuilder, cost),
                new InconsistentADStarNodeBuilder<Point>(transition, cost, defaultBuilder));
    }

    public static HeuristicFunction<Point, Double> defaultHeuristicFunction(final StringMaze maze) {
        return new HeuristicFunction<Point, Double>() {
            public Double estimate(Point from) {
                Point goal = maze.getGoalLoc();
                return Math.sqrt((from.x - goal.x) * (from.x - goal.x)
                        + (from.y - goal.y) * (from.y - goal.y));
            }
        };
    }

    public static TransitionFunction<Point> defaultTransitionFunction(final StringMaze maze) {
        return new TransitionFunction<Point>() {
            public Iterable<Transition<Point>> from(Point fromState) {
                return Transition.map(fromState,
                        maze.validLocationsFrom(fromState));
            }
        };
    }

    public static CostFunction<Point, Double> defaultCostFunction() {
        return new CostFunction<Point, Double>() {
            public Double evaluate(Transition<Point> successor) {
                Point from = successor.from();
                Point to = successor.to();
                if (from != null) {
                    return Math.sqrt((from.x - to.x) * (from.x - to.x)
                            + (from.y - to.y) * (from.y - to.y));
                }
                return 0.0;
            }
        };
    }
}
