/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.usc.citius.lab.hipster.testutils;

import es.usc.citius.lab.hipster.algorithm.AstarIterator;
import es.usc.citius.lab.hipster.function.CostFunction;
import es.usc.citius.lab.hipster.function.HeuristicFunction;
import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.node.NumericNodeBuilder;
import es.usc.citius.lab.hipster.node.Transition;
import es.usc.citius.lab.hipster.util.maze.StringMaze;
import java.awt.Point;

/**
 *
 * @author Adrián González Sieira
 * @since 26-03-2013
 * @version 1.0
 */
public class AStarIteratorFromMazeCreator {

    public static AstarIterator<Point> create(final StringMaze maze, boolean useHeuristic){
            HeuristicFunction<Point, Double> heuristic = new HeuristicFunction<Point, Double>() {
            public Double estimate(Point from) {
                Point to = maze.getGoalLoc();
                return Math.sqrt((from.x - to.x) * (from.x - to.x)
                        + (from.y - to.y) * (from.y - to.y));
            }
        };

        CostFunction<Point, Double> cost = new CostFunction<Point, Double>() {
            public Double evaluate(Transition<Point> successor) {
                Point from = successor.from();
                Point to = successor.state();
                if (from != null && to != null) {
                    return Math.sqrt((from.x - to.x) * (from.x - to.x)
                            + (from.y - to.y) * (from.y - to.y));
                }
                return 0.0;
            }
        };

        TransitionFunction<Point> transition = new TransitionFunction<Point>() {
            public Iterable<Transition<Point>> from(Point fromState) {
                return Transition.map(fromState,
                        maze.validLocationsFrom(fromState));
            }
        };
        
        AstarIterator<Point> it;
        if(useHeuristic){
            it = new AstarIterator<Point>(maze.getInitialLoc(), transition, new NumericNodeBuilder<Point>(cost, heuristic));
        }
        else{
            it = new AstarIterator<Point>(maze.getInitialLoc(), transition, new NumericNodeBuilder<Point>(cost));
        }
        return it;
    }
    
}
