package es.usc.citius.lab.hipster.iterator;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections15.Transformer;
import org.junit.Test;

import com.google.common.base.Stopwatch;

import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import es.usc.citius.lab.hipster.algorithm.AstarIterator;
import es.usc.citius.lab.hipster.function.CostFunction;
import es.usc.citius.lab.hipster.function.HeuristicFunction;
import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.node.ComparableNode;
import es.usc.citius.lab.hipster.node.Node;
import es.usc.citius.lab.hipster.node.NumericNodeBuilder;
import es.usc.citius.lab.hipster.node.Transition;
import es.usc.citius.lab.hipster.util.NumericCostEvaluator;
import es.usc.citius.lab.hipster.util.maze.StringMaze;

public class SearchComparativeTest {

    String[] testMaze1 = {
        "        ",
        "    X   ",
        "  @ X O ",
        "    X   ",
        "        ",
        "        ",};
    String[] testMaze2 = {
        "XX@XXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
        "XX XXXXXXXXXXXXX     XXXXXXXXXXX",
        "XX    XXXXXXXXXX XXX XX     XXXX",
        "XXXXX  XXXXXX    XXX XX XXX XXXX",
        "XXX XX XXXXXX XX XXX XX  XX XXXX",
        "XXX     XXXXX XXXXXX XXXXXX XXXX",
        "XXXXXXX       XXXXXX        XXXX",
        "XXXXXXXXXX XXXXX XXXXXXXXXXXXXXX",
        "XXXXXXXXXX XX    XXXXX      XXXX",
        "XXXXXXXXXX    XXXXXXXX XXXX XXXX",
        "XXXXXXXXXXX XXXXXXXXXX XXXX XXXX",
        "XXXXXXXXXXX            XXXX XXXX",
        "XXXXXXXXXXXXXXXXXXXXXXXX XX XXXX",
        "XXXXXX              XXXX XX XXXX",
        "XXXXXX XXXXXXXXXXXX XX      XXXX",
        "XXXXXX XXO   XXXXXX XXXX XXXXXXX",
        "XXXXXX XXXXX   XXX            XX",
        "XXXXXX XXXXXXX XXXXXXXXXXX XXXXX",
        "XXXXXX XXXXXXX XXXXXXXXXXXXXXXXX",
        "XXXXXX            XXXXXXXXXXXXXX"};
    String[] testMaze3 = {
        "                      O          ",
        "                                 ",
        "                                 ",
        "                                 ",
        "                                 ",
        "                                 ",
        "                                 ",
        "                                 ",
        "                                 ",
        "                                 ",
        "                                 ",
        "           @                     ",
        "                                 "};
    String[] testMaze4 = {
        "                      O          ",
        "                                 ",
        "                                 ",
        "                                 ",
        "                                 ",
        "     XXXXXXXXXXXXXXXXXXXXX       ",
        "     XXXXXXXXXXXXXXXXXXXXX       ",
        "                       XXX       ",
        "                       XXX       ",
        "                       XXX       ",
        "                       XXX       ",
        "           @                     ",
        "                                 "};
    String[] testMaze5 = {
        "                  X   O          ",
        "                  X              ",
        "                  XXXXXXXX       ",
        "       XXXXXXXXXX  XXXXX         ",
        "                X    XXXXXXXXXX  ",
        "     XXXXXX  XXXXXXX  XXXX       ",
        "     XXXXXX XXXXXXX  XXXXX       ",
        "                       XXX       ",
        "                       XXX       ",
        "                       XXX       ",
        "                       XXX       ",
        "           @                     ",
        "                                 "};

    private class Edge {

        private Point source, dest;
        private Double cost;

        public Edge(Point source, Point dest, Double cost) {
            this.source = source;
            this.dest = dest;
            this.cost = cost;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((cost == null) ? 0 : cost.hashCode());
            result = prime * result + ((dest == null) ? 0 : dest.hashCode());
            result = prime * result
                    + ((source == null) ? 0 : source.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            Edge other = (Edge) obj;
            if (!getOuterType().equals(other.getOuterType())) {
                return false;
            }
            if (cost == null) {
                if (other.cost != null) {
                    return false;
                }
            } else if (!cost.equals(other.cost)) {
                return false;
            }
            if (dest == null) {
                if (other.dest != null) {
                    return false;
                }
            } else if (!dest.equals(other.dest)) {
                return false;
            }
            if (source == null) {
                if (other.source != null) {
                    return false;
                }
            } else if (!source.equals(other.source)) {
                return false;
            }
            return true;
        }

        private SearchComparativeTest getOuterType() {
            return SearchComparativeTest.this;
        }
    }

    private DirectedGraph<Point, Edge> createMazeGraph(StringMaze maze) {
        // Create a graph from maze
        DirectedGraph<Point, Edge> graph = new DirectedSparseGraph<Point, Edge>();
        // Convert maze to graph. For each cell, add all valid neighbors with
        // their costs
        for (Point source : maze.getMazePoints()) {
            if (!graph.containsVertex(source)) {
                graph.addVertex(source);
            }
            for (Point dest : maze.validMovesFromCell(source)) {
                if (!graph.containsVertex(dest)) {
                    graph.addVertex(dest);
                }
                double cost = Math.sqrt((source.x - dest.x)
                        * (source.x - dest.x) + (source.y - dest.y)
                        * (source.y - dest.y));
                Edge e = new Edge(source, dest, cost);
                if (!graph.containsEdge(e)) {
                    graph.addEdge(e, source, dest);
                }
            }
        }
        return graph;
    }

    // Run tests and compare results
    @Test
    public void benchmark() {
        System.out.println("Maze | JUNG (ms) | Composit (ms)");
        System.out.println("-------------------------------------");
        final int times = 5;
        for (int i = 10; i < 500; i += 10) {
            StringMaze maze = StringMaze.random(i, 0.9);
            // Repeat 10 times
            //Double mean1 = 0d, mean2 = 0d;
            double min1 = Double.MAX_VALUE, min2 = Double.MAX_VALUE;
            for (int j = 0; j < times; j++) {

                double result1 = benchmarkJung(maze);
                if (result1 < min1) {
                    min1 = result1;
                }
                double result2 = benchmarkCompositAstar(maze);
                if (result2 < min2) {
                    min2 = result2;
                }
            }
            //mean1 /= times;
            //mean2 /= times;
            // Print mean values
            System.out.println(String.format("%d \t %.5g \t %.5g", i, min1, min2));
        }

    }

    @Test
    public void iterativeHeuristicDijkstra() throws InterruptedException {
        //compositHeuristicDijkstra(new StringMaze(testMaze4), 129);
        //compositHeuristicDijkstra(new StringMaze(testMaze5), 286);
        dijkstraRealTime(new StringMaze(testMaze4), 129);
        //dijkstraRealTime(new StringMaze(testMaze5), 286);
    }

    public void dijkstraRealTime(StringMaze maze, int real_steps) throws InterruptedException {
        TransitionFunction<Point> transitionFunction = dijkstraTransitionFunction(maze);
        CostFunction<Point, Double> costFunction = dijkstraCostFunction();
        AstarIterator<Point> it = new AstarIterator<Point>(maze.getInitialLoc(), transitionFunction, new NumericNodeBuilder<Point>(costFunction));
        int step = 0;
        Stopwatch w = new Stopwatch().start();
        while (it.hasNext()) {
            ComparableNode<Point> state = it.next();
            step++;

            List<Point> points = new ArrayList<Point>();
            for (Node<Point> node : state.path()) {
                points.add(node.transition().state());
            }

            drawPath(maze, points);

            Thread.sleep(20);

            if (state.transition().state().equals(maze.getGoalLoc())) {
                System.out.println("Path cost: " + new NumericCostEvaluator<Point>().evaluate(state.path(), costFunction));
                System.out.println("Total time: " + w.stop());
                break;
            }

        }
        System.out.println("Steps: " + step);
    }

    public long benchmarkJung(StringMaze maze) {
        DirectedGraph<Point, Edge> graph = createMazeGraph(maze);
        DijkstraShortestPath<Point, Edge> dijkstra = new DijkstraShortestPath<Point, Edge>(
                graph, new Transformer<Edge, Double>() {
            public Double transform(Edge input) {
                return input.cost;
            }
        }, true);
        Stopwatch w = new Stopwatch().start();
        List<Edge> path = dijkstra.getPath(maze.getInitialLoc(),
                maze.getGoalLoc());
        Double total = 0d;
        Collection<Point> points = new HashSet<Point>();
        for (Edge e : path) {
            total += e.cost;
            points.add(e.source);
            points.add(e.dest);
        }
        //System.out.println("JUNG: " + total);
        //System.out.println("Time : " + time + " ms");
        //drawPath(maze, new ArrayList(points));
        return w.stop().elapsedMillis();
    }

    public long benchmarkCompositAstar(final StringMaze maze) {
        Stopwatch w = new Stopwatch().start();
        TransitionFunction<Point> transitionFunction = dijkstraTransitionFunction(maze);
        CostFunction<Point, Double> costFunction = dijkstraCostFunction();
        AstarIterator<Point> it = new AstarIterator<Point>(maze.getInitialLoc(), transitionFunction, new NumericNodeBuilder<Point>(costFunction));
        while (it.hasNext()) {
            ComparableNode<Point> state = it.next();
            if (state.transition().state().equals(maze.getGoalLoc())) {
                //System.out.println("ComposIT: " + state.cost());
                return w.stop().elapsedMillis();
            }

        }
        System.out.println("No solution!");
        return w.stop().elapsedMillis();
    }

    private void drawPath(StringMaze maze, List<Point> points) {
        for (String line : maze.getMazePath(points)) {
            System.out.println(line);
        }
    }

    /**
     * Creates a new transition function (8-connected grid)
     * @param valid instance of StringMaze
     * @return TransitionFunction
     */
    private TransitionFunction<Point> dijkstraTransitionFunction(final StringMaze maze) {
        return new TransitionFunction<Point>() {
            public Iterable<Transition<Point>> from(Point fromState) {
                Collection<Transition<Point>> successors = new LinkedList<Transition<Point>>();
                for (Point p : maze.validMovesFromCell(fromState)) {
                    successors.add(new Transition<Point>(fromState, p));
                }
                return successors;
            }
        };
    }

    /**
     * Creates a new cost function (2D euclidean distance)
     * @return CostFunction
     */
    private CostFunction<Point, Double> dijkstraCostFunction() {
        return new CostFunction<Point, Double>() {
            public Double evaluate(Transition<Point> successor) {
                Point from = successor.from();
                Point to = successor.state();
                if (from != null && to != null) {
                    return Math.sqrt((from.x - to.x)
                            * (from.x - to.x) + (from.y - to.y)
                            * (from.y - to.y));
                }
                return 0.0;
            }
        };
    }
}
