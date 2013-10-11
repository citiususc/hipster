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

import es.usc.citius.lab.hipster.algorithm.problem.HeuristicSearchProblem;
import es.usc.citius.lab.hipster.util.maze.Maze2D;
import es.usc.citius.lab.hipster.function.CostFunction;
import es.usc.citius.lab.hipster.function.HeuristicFunction;
import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.function.impl.CostOperator;
import es.usc.citius.lab.hipster.node.Transition;

import java.awt.*;

public class MazeHeuristicSearchProblem implements HeuristicSearchProblem<Point, Double> {
    private final Maze2D maze;
    private boolean useHeuristic;

    public MazeHeuristicSearchProblem(Maze2D maze, boolean useHeuristic) {
        this.maze = maze;
        this.useHeuristic = useHeuristic;
    }

    public MazeHeuristicSearchProblem(Maze2D maze) {
        this.maze = maze;
        this.useHeuristic = false;
    }

    public TransitionFunction<Point> getTransitionFunction() {
        return new TransitionFunction<Point>() {
            public Iterable<Transition<Point>> from(Point current) {
                return Transition.map(current, maze.validLocationsFrom(current));
            }
        };
    }

    public CostFunction<Point, Double> getCostFunction() {
        return new CostFunction<Point, Double>() {
            public Double evaluate(Transition<Point> transition) {
                return transition.from().distance(transition.to());
            }
        };
    }

    public HeuristicFunction<Point, Double> getHeuristicFunction() {
        return new HeuristicFunction<Point, Double>() {
            public Double estimate(Point state) {
                if (useHeuristic) {
                    return state.distance(maze.getGoalLoc());
                } else {
                    return 0d;
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

    public CostOperator<Double> getAccumulator() {
        return CostOperator.doubleAdditionOp();
    }


}
