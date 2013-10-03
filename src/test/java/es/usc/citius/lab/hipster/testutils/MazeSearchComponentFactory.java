package es.usc.citius.lab.hipster.testutils;

import java.awt.Point;

import es.usc.citius.lab.hipster.function.CostFunction;
import es.usc.citius.lab.hipster.function.HeuristicFunction;
import es.usc.citius.lab.hipster.function.impl.BinaryOperation;
import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.node.Transition;
import es.usc.citius.lab.hipster.algorithm.multiobjective.maze.Maze2D;

public class MazeSearchComponentFactory implements SearchComponentFactory<Point, Double> {
	private final Maze2D maze;
	private boolean useHeuristic;
	
	public MazeSearchComponentFactory(Maze2D maze, boolean useHeuristic){
		this.maze = maze;
		this.useHeuristic = useHeuristic;
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
				return new Double(transition.from().distance(transition.to()));
			}
		};
	}

	public HeuristicFunction<Point, Double> getHeuristicFunction() {
		return new HeuristicFunction<Point, Double>() {
			public Double estimate(Point state) {
				if (useHeuristic){
				return new Double(state.distance(maze.getGoalLoc()));
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

	public BinaryOperation<Double> getAccumulator() {
		return BinaryOperation.doubleAdditionOp();
	}

	


}
