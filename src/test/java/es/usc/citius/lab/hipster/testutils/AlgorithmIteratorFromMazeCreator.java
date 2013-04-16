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
import es.usc.citius.lab.hipster.algorithm.BellmanFord;
import es.usc.citius.lab.hipster.function.CostFunction;
import es.usc.citius.lab.hipster.function.HeuristicFunction;
import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.node.ADStarNode;
import es.usc.citius.lab.hipster.node.AStarDoubleNodeBuilder;
import es.usc.citius.lab.hipster.node.NodeBuilder;
import es.usc.citius.lab.hipster.node.ADStarNodeUpdater;
import es.usc.citius.lab.hipster.node.ADStarNodeBuilder;
import es.usc.citius.lab.hipster.node.Transition;
import es.usc.citius.lab.hipster.util.DoubleOperable;
import es.usc.citius.lab.hipster.util.maze.Maze2D;
import java.awt.Point;

/**
 * This class creates the iterators for different algorithms using
 * {@link Maze2D} as base.
 *
 * @author Adrián González Sieira
 * @since 26-03-2013
 * @version 1.0
 */
public class AlgorithmIteratorFromMazeCreator {

    public static AStar<Point> astar(final Maze2D maze, boolean useHeuristic) {
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

    public static ADStar<Point, DoubleOperable> adstar(final Maze2D maze) {
        HeuristicFunction<Point, DoubleOperable> heuristic = defaultDoubleOperableHeuristicFunction(maze);

        CostFunction<Point, DoubleOperable> cost = defaultDoubleOperableCostFunction();

        TransitionFunction<Point> transition = defaultTransitionFunction(maze);

        NodeBuilder<Point, ADStarNode<Point, DoubleOperable>> defaultBuilder = new ADStarNodeBuilder<Point, DoubleOperable>(DoubleOperable.MIN, DoubleOperable.MAX);

        ADStarNodeUpdater<Point, DoubleOperable> updater = new ADStarNodeUpdater<Point, DoubleOperable>(cost, heuristic, 1.0);

        return new ADStar<Point, DoubleOperable>(
                maze.getInitialLoc(),
                maze.getGoalLoc(),
                transition,
                transition,
                defaultBuilder,
                updater);
    }
    
    public static BellmanFord<Point> bellmanFord(final Maze2D maze, boolean useHeuristic) {
        CostFunction<Point, Double> cost = defaultCostFunction();

        TransitionFunction<Point> transition = defaultTransitionFunction(maze);

        BellmanFord<Point> it;

        it = new BellmanFord<Point>(maze.getInitialLoc(), transition, new AStarDoubleNodeBuilder<Point>(cost), null);
        
        return it;
    }

    public static HeuristicFunction<Point, Double> defaultHeuristicFunction(final Maze2D maze) {
        return new HeuristicFunction<Point, Double>() {
            public Double estimate(Point from) {
                return from.distance(maze.getGoalLoc());
            }
        };
    }

    public static TransitionFunction<Point> defaultTransitionFunction(final Maze2D maze) {
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
                return (successor.from() != null)?successor.from().distance(successor.to()):0.0;
            }
        };
    }
    
    public static CostFunction<Point, DoubleOperable> defaultDoubleOperableCostFunction() {
        return new CostFunction<Point, DoubleOperable>() {
            public DoubleOperable evaluate(Transition<Point> successor) {
                return (successor.from() != null) ? new DoubleOperable(successor.from().distance(successor.to())) : DoubleOperable.MIN;
            }
        };
    }
    
    
    public static HeuristicFunction<Point, DoubleOperable> defaultDoubleOperableHeuristicFunction(final Maze2D maze) {
        return new HeuristicFunction<Point, DoubleOperable>() {
            public DoubleOperable estimate(Point from) {
                return new DoubleOperable(from.distance(maze.getGoalLoc()));
            }
        };
    }
}
