package es.usc.citius.lab.hipster.testutils;

import java.awt.Point;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import es.usc.citius.lab.hipster.function.CostFunction;
import es.usc.citius.lab.hipster.function.HeuristicFunction;
import es.usc.citius.lab.hipster.function.Operation;
import es.usc.citius.lab.hipster.function.Operations;
import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.node.Transition;
import es.usc.citius.lab.hipster.util.maze.Maze2D;

public class JungMazeGraphComponentFactory implements SearchComponentFactory<Point, Double> {
	private final DirectedGraph<Point, JungEdge<Point>> graph;
	private final Maze2D maze;
	private boolean useHeuristic;
	
	public JungMazeGraphComponentFactory(Maze2D maze, boolean useHeuristic){
		this.maze = maze;
		this.graph = createGraphFrom(maze);
		this.useHeuristic = useHeuristic;
	}
	
	public TransitionFunction<Point> getTransitionFunction() {
		return new TransitionFunction<Point>() {
			public Iterable<Transition<Point>> from(Point current) {
				return Transition.map(current, graph.getNeighbors(current));
			}
		};
	}

	public CostFunction<Point, Double> getCostFunction() {
		return new CostFunction<Point, Double>() {
			public Double evaluate(Transition<Point> transition) {
				return new Double(graph.findEdge(transition.from(), transition.to()).getCost());
			}
		};
	}

	public HeuristicFunction<Point, Double> getHeuristicFunction() {
		return new HeuristicFunction<Point, Double>() {
			public Double estimate(Point state) {
				if (useHeuristic){
					return state.distance(maze.getGoalLoc());
				} else {
					return Operations.addition().getIdentityValue();
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
	
	public static DirectedGraph<Point, JungEdge<Point>> createGraphFrom(Maze2D maze) {
        // Create a graph from maze
        DirectedGraph<Point, JungEdge<Point>> graph = new DirectedSparseGraph<Point, JungEdge<Point>>();
        // Convert maze to graph. For each cell, add all valid neighbors with
        // their costs
        for (Point source : maze.getMazePoints()) {
            if (!graph.containsVertex(source)) {
                graph.addVertex(source);
            }
            for (Point dest : maze.validLocationsFrom(source)) {
                if (!graph.containsVertex(dest)) {
                    graph.addVertex(dest);
                }
                double edgeCost = Math.sqrt((source.x - dest.x)
                        * (source.x - dest.x) + (source.y - dest.y)
                        * (source.y - dest.y));
                JungEdge<Point> e = new JungEdge<Point>(source, dest, edgeCost);
                if (!graph.containsEdge(e)) {
                    graph.addEdge(e, source, dest);
                }
            }
        }
        return graph;
    }

	public Operation<Double> getAccumulator() {
		return Operations.addition();
	}

	
	

}
