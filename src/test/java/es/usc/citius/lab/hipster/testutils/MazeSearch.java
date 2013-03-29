package es.usc.citius.lab.hipster.testutils;

import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedGraph;
import es.usc.citius.lab.hipster.algorithm.AStar;
import es.usc.citius.lab.hipster.node.ComparableNode;
import es.usc.citius.lab.hipster.node.Node;
import es.usc.citius.lab.hipster.util.NodeToStateListConverter;
import es.usc.citius.lab.hipster.util.NumericCostEvaluator;
import es.usc.citius.lab.hipster.util.maze.StringMaze;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections15.Transformer;
import static org.junit.Assert.fail;

/**
 *
 * @author Adrián González Sieira
 * @since 26-03-2013
 * @version 1.0
 */
public final class MazeSearch {
    
    private MazeSearch(){
    
    }

    public static final class Result{
        private List<Point> path;
        private Double cost;

        public Result(List<Point> path, Double cost) {
            this.path = path;
            this.cost = cost;
        }
        
        public Double getCost() {
            return cost;
        }

        public List<Point> getPath() {
            return path;
        }
    }
    
    public static Result executePrintIteratorSearch(AStar<Point> it, StringMaze maze) throws InterruptedException {
        int steps = 0;
        while (it.hasNext()) {
            ComparableNode<Point> currentNode = it.next();
            steps++;
            List<Node<Point>> nodePath = currentNode.path();
            List<Point> statePath = new NodeToStateListConverter<Point>().convert(nodePath);
            Thread.sleep(20);
            System.out.println(maze.getMazeForPath(statePath));
            if (currentNode.transition().to().equals(maze.getGoalLoc())) {
                Double cost = new NumericCostEvaluator<Point>().evaluate(nodePath, AStarIteratorFromMazeCreator.defaultCostFunction());
                return new Result(statePath, cost);
            }
        }
        fail("Solution not found after " + steps + " steps.");
        return null;
    }
    
    public static Result executeIteratorSearch(AStar<Point> it, StringMaze maze) {
        int steps = 0;
        while (it.hasNext()) {
            ComparableNode<Point> currentNode = it.next();
            steps++;
            if (currentNode.transition().to().equals(maze.getGoalLoc())) {
                List<Node<Point>> nodePath = currentNode.path();
                Double cost = new NumericCostEvaluator<Point>().evaluate(nodePath, AStarIteratorFromMazeCreator.defaultCostFunction());
                List<Point> statePath = new NodeToStateListConverter<Point>().convert(nodePath);
                return new Result(statePath, cost);
            }
        }
        fail("Solution not found after " + steps + " steps.");
        return null;
    }

    public static Result executeJungSearch(DirectedGraph<Point, JungEdge> jungGraph, StringMaze maze) {
        DijkstraShortestPath<Point, JungEdge> dijkstra = new DijkstraShortestPath<Point, JungEdge>(
                jungGraph, new Transformer<JungEdge, Double>() {
            public Double transform(JungEdge input) {
                return input.getCost();
            }
        }, true);
        List<JungEdge> path = dijkstra.getPath(maze.getInitialLoc(),
                maze.getGoalLoc());
        Double cost = 0.0;
        List<Point> statePath = new ArrayList<Point>();
        for (Iterator<JungEdge> it = path.iterator(); it.hasNext();) {
            JungEdge current = it.next();
            statePath.add(current.getSource());
            cost += current.getCost();
        }
        statePath.add(maze.getGoalLoc());
        return new Result(statePath, cost);
    }
}
