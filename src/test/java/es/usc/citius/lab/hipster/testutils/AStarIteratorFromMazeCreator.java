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

    public static AstarIterator<Point> create(final StringMaze maze, boolean useHeuristic) {
        HeuristicFunction<Point, Double> heuristic = defaultHeuristicFunction(maze);

        CostFunction<Point, Double> cost = defaultCostFunction();

        TransitionFunction<Point> transition = defaultTransitionFunction(maze);

        AstarIterator<Point> it;
        if (useHeuristic) {
            //it = new AstarIterator<Point>(maze.getInitialLoc(), transition, new NumericNodeBuilder<Point>(cost, heuristic));
            it = AstarIterator.iterator(maze.getInitialLoc(), transition).cost(cost).heuristic(heuristic).build();
        } else {
            //it = new AstarIterator<Point>(maze.getInitialLoc(), transition, new NumericNodeBuilder<Point>(cost));
        	it = AstarIterator.iterator(maze.getInitialLoc(), transition).cost(cost).build();
        }
        return it;
    }
    
    public static HeuristicFunction<Point, Double> defaultHeuristicFunction(final StringMaze maze){
        return new HeuristicFunction<Point, Double>() {
            public Double estimate(Point from) {
                Point goal = maze.getGoalLoc();
                return Math.sqrt((from.x - goal.x) * (from.x - goal.x)
                        + (from.y - goal.y) * (from.y - goal.y));
            }
        };
    }
    
    public static TransitionFunction<Point> defaultTransitionFunction(final StringMaze maze){
        return new TransitionFunction<Point>() {
            public Iterable<Transition<Point>> from(Point fromState) {
                return Transition.map(fromState,
                        maze.validLocationsFrom(fromState));
            }
        };
    }
    
    public static CostFunction<Point, Double> defaultCostFunction(){
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
