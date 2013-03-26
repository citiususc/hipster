package es.usc.citius.lab.hipster.algorithm;

import com.google.common.base.Stopwatch;
import edu.uci.ics.jung.graph.DirectedGraph;
import es.usc.citius.lab.hipster.function.CostFunction;
import es.usc.citius.lab.hipster.function.HeuristicFunction;
import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.node.NumericNodeBuilder;
import es.usc.citius.lab.hipster.node.Transition;
import es.usc.citius.lab.hipster.testutils.JungDirectedGraphFromMazeCreator;
import es.usc.citius.lab.hipster.testutils.JungEdge;
import es.usc.citius.lab.hipster.testutils.MazeSearch;
import es.usc.citius.lab.hipster.util.maze.StringMaze;
import java.awt.Point;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 *
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @author Pablo Rodríguez Mier <pablo.rodriguez.mier@usc.es>
 * @since 26/03/2013
 * @version 1.0
 */
public class BenchmarkTest {

    private static HeuristicFunction<Point, Double> heuristic;
    private static CostFunction<Point, Double> cost;
    private static TransitionFunction<Point> transition;

    public BenchmarkTest() {
    }

    @BeforeClass
    public static void tearUp() {

        heuristic = new HeuristicFunction<Point, Double>() {
            public Double estimate(Point state) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };

        cost = new CostFunction<Point, Double>() {
            public Double evaluate(Transition<Point> transition) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };

        transition = new TransitionFunction<Point>() {
            public Iterable<Transition<Point>> from(Point current) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
    }

    @Test
    public void benchmark() throws InterruptedException {
        System.out.println("Maze | JUNG (ms) | Composit (ms)");
        System.out.println("-------------------------------------");
        final int times = 5;
        for (int i = 10; i < 500; i += 10) {
            StringMaze maze = StringMaze.random(i, 0.9);
            // Repeat 10 times
            //Double mean1 = 0d, mean2 = 0d;
            double min1 = Double.MAX_VALUE, min2 = Double.MAX_VALUE;
            DirectedGraph<Point, JungEdge> graph = JungDirectedGraphFromMazeCreator.create(maze);
            for (int j = 0; j < times; j++) {
                AstarIterator<Point> it = new AstarIterator<Point>(maze.getInitialLoc(), transition, new NumericNodeBuilder<Point>(cost, heuristic));
                Stopwatch w = new Stopwatch().start();
                List<Point> resultIterator = MazeSearch.executeIteratorSearch(it, maze);
                long result1 = w.stop().elapsed(TimeUnit.MILLISECONDS);
                if (result1 < min1) {
                    min1 = result1;
                }
                Stopwatch w2 = new Stopwatch().start();
                List<Point> resultJung = MazeSearch.executeJungSearch(graph, maze);
                long result2 = w2.stop().elapsed(TimeUnit.MILLISECONDS);
                if (result2 < min2) {
                    min2 = result2;
                }
                assertEquals(resultIterator, resultJung);
            }
            System.out.println(String.format("%d \t %.5g \t %.5g", i, min1, min2));
        }
    }
}