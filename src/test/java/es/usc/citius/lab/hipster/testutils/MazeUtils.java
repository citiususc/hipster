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

import es.usc.citius.lab.hipster.algorithm.ADStar;
import es.usc.citius.lab.hipster.algorithm.AStar;
import es.usc.citius.lab.hipster.algorithm.BellmanFord;
import es.usc.citius.lab.hipster.algorithm.multiobjective.maze.Maze2D;
import es.usc.citius.lab.hipster.function.CostFunction;
import es.usc.citius.lab.hipster.function.HeuristicFunction;
import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.function.impl.CostOperator;
import es.usc.citius.lab.hipster.function.impl.Product;
import es.usc.citius.lab.hipster.node.NodeFactory;
import es.usc.citius.lab.hipster.node.Transition;
import es.usc.citius.lab.hipster.node.adstar.ADStarNode;
import es.usc.citius.lab.hipster.node.adstar.ADStarNodeBuilder;
import es.usc.citius.lab.hipster.node.adstar.ADStarNodeUpdater;
import es.usc.citius.lab.hipster.node.informed.HeuristicNodeImplFactory;

/**
 * This class creates the iterators for different algorithms using
 * {@link Maze2D} as base.
 *
 * @author Adrián González Sieira
 * @since 26-03-2013
 * @version 1.0
 */
public class MazeUtils {

    public static AStar<Point, Double> astar(final Maze2D maze, boolean useHeuristic) {
        HeuristicFunction<Point, Double> heuristic = defaultHeuristicFunction(maze);

        CostFunction<Point, Double> cost = defaultCostFunction();

        TransitionFunction<Point> transition = defaultTransitionFunction(maze);
        /*
        HeuristicAlgorithmBuilder<Point> builder = new HeuristicAlgorithmBuilder<Point>(maze.getInitialLoc(), transition)
                .cost(cost)
                .heuristic(heuristic)
                .build(new AStarFactory<Point>());*/

        AStar<Point,Double> it;
        if (useHeuristic) {
        	it = AStar.getSearchIterator(maze.getInitialLoc(), transition).cost(cost).heuristic(heuristic).build();
        } else {
        	it = AStar.getSearchIterator(maze.getInitialLoc(), transition).cost(cost).build();
        }
        return it;
    }

    public static ADStar<Point, Double> adstar(final Maze2D maze, boolean useHeuristic) {
        HeuristicFunction<Point, Double> heuristic;
        if(useHeuristic){
            heuristic = defaultDoubleHeuristicFunction(maze);
        }
        else{
            heuristic = new HeuristicFunction<Point, Double>() {

                public Double estimate(Point state) {
                    return 0d;
                }
            };
        }

        CostFunction<Point, Double> cost = defaultDoubleCostFunction();

        TransitionFunction<Point> transition = defaultTransitionFunction(maze);

        NodeFactory<Point, ADStarNode<Point, Double>> defaultBuilder = new ADStarNodeBuilder<Point, Double>(0d, Double.MAX_VALUE);

        ADStarNodeUpdater<Point, Double> updater = new ADStarNodeUpdater<Point, Double>(cost, heuristic, CostOperator.doubleAdditionOp(), new Product(), 1.0);

        return new ADStar<Point, Double>(
                maze.getInitialLoc(),
                maze.getGoalLoc(),
                transition,
                transition,
                defaultBuilder,
                updater);	
    }

    public static ADStar<Point, Double> adstar(final Maze2D maze) {
        return adstar(maze, false);
    }
    
    public static BellmanFord<Point,Double> bellmanFord(final Maze2D maze, boolean useHeuristic) {
        CostFunction<Point, Double> cost = defaultCostFunction();

        TransitionFunction<Point> transition = defaultTransitionFunction(maze);

        BellmanFord<Point,Double> it;

        it = new BellmanFord<Point,Double>(maze.getInitialLoc(), transition, new HeuristicNodeImplFactory<Point, Double>(cost, CostOperator.doubleAdditionOp()).toCostNodeFactory());
        return it;
    }

    public static HeuristicFunction<Point, Double> defaultHeuristicFunction(final Maze2D maze) {
        return new HeuristicFunction<Point, Double>() {
            public Double estimate(Point from) {
                return new Double(from.distance(maze.getGoalLoc()));
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
                return (successor.from() != null)?new Double(successor.from().distance(successor.to())): 0d;
            }
        };
    }
    
    public static CostFunction<Point, Double> defaultDoubleCostFunction() {
        return new CostFunction<Point, Double>() {
            public Double evaluate(Transition<Point> successor) {
                return (successor.from() != null) ? new Double(successor.from().distance(successor.to())) : 0d;
            }
        };
    }
    
    
    public static HeuristicFunction<Point, Double> defaultDoubleHeuristicFunction(final Maze2D maze) {
        return new HeuristicFunction<Point, Double>() {
            public Double estimate(Point from) {
                return new Double(from.distance(maze.getGoalLoc()));
            }
        };
    }
}
