package es.usc.citius.lab.hipster.algorithm;

import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Test;

import com.google.common.base.Stopwatch;

import es.usc.citius.lab.hipster.function.CostFunction;
import es.usc.citius.lab.hipster.function.HeuristicFunction;
import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.node.ComparableNode;
import es.usc.citius.lab.hipster.node.NumericNodeBuilder;
import es.usc.citius.lab.hipster.node.Transition;
import es.usc.citius.lab.hipster.util.maze.StringMaze;

public class AstarIteratorTest {

    @Test
    public void test() {
        final StringMaze maze = StringMaze.getDefaultMaze2();

        // Successor function used to calculate available movements
        TransitionFunction<Point> successor = new TransitionFunction<Point>() {
            public Iterable<Transition<Point>> from(Point fromState) {
                return Transition.map(fromState,
                        maze.validMovesFromCell(fromState));
            }
        };

        // Cost function used to evaluate a movement
        CostFunction<Point, Double> g = new CostFunction<Point, Double>() {
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
        // Heuristic function used to estimate the distance to the goal
        HeuristicFunction<Point, Double> h = new HeuristicFunction<Point, Double>() {
            public Double estimate(Point from) {
                Point to = maze.getGoalLoc();
                return Math.sqrt((from.x - to.x) * (from.x - to.x)
                        + (from.y - to.y) * (from.y - to.y));
            }
        };
        // Create the iterator
        AstarIterator<Point> iterator = new AstarIterator<Point>(maze.getInitialLoc(), successor, new NumericNodeBuilder<Point>(g, h));

        int step = 0;
        Stopwatch timer = new Stopwatch().start();
        while (iterator.hasNext()) {
            ComparableNode<Point> location = iterator.next();
            if (location.transition().state().equals(maze.getGoalLoc())) {
                long msec = timer.stop().elapsedMillis();
                assertEquals(step, 128);
                assertEquals(location.cost(), 27.07, 0.01);
                System.out.println("[AstarIteratorTest]: A* algorithm test executed. Goal reached in " + step + " steps with a cost of " + location.cost() + " in " + msec + " ms");
                return;
            }
            step++;
        }
        fail("No solution found");

    }
}
