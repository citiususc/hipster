package es.usc.citius.lab.hipster.testutils;

import java.awt.Point;

import es.usc.citius.lab.hipster.function.CostFunction;
import es.usc.citius.lab.hipster.function.HeuristicFunction;
import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.node.Transition;
import es.usc.citius.lab.hipster.util.DoubleOperable;
import es.usc.citius.lab.hipster.util.maze.Maze2D;

public class MazeSearchComponentFactory implements SearchComponentFactory<Point, DoubleOperable> {
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

	public CostFunction<Point, DoubleOperable> getCostFunction() {
		return new CostFunction<Point, DoubleOperable>() {
			public DoubleOperable evaluate(Transition<Point> transition) {
				return new DoubleOperable(transition.from().distance(transition.to()));
			}
		};
	}

	public HeuristicFunction<Point, DoubleOperable> getHeuristicFunction() {
		return new HeuristicFunction<Point, DoubleOperable>() {
			public DoubleOperable estimate(Point state) {
				if (useHeuristic){
				return new DoubleOperable(state.distance(maze.getGoalLoc()));
				} else {
					return DoubleOperable.MIN;
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

	public DoubleOperable getDefaultValue() {
		return DoubleOperable.MIN;
	}

	public DoubleOperable getMaxValue() {
		return DoubleOperable.MAX;
	}


}
