package es.usc.citius.composit.search.algorithm;

import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Test;

import com.google.common.base.Stopwatch;

import es.usc.citius.composit.search.function.CostFunction;
import es.usc.citius.composit.search.function.HeuristicFunction;
import es.usc.citius.composit.search.function.SuccessorFunction;
import es.usc.citius.composit.search.node.HeuristicNode;
import es.usc.citius.composit.search.node.Successor;
import es.usc.citius.composit.search.util.maze.StringMaze;

public class AstarIteratorTest {

	@Test
	public void test() {
		final StringMaze maze = StringMaze.getDefaultMaze2();

		// Successor function used to calculate available movements
		SuccessorFunction<Point> successor = new SuccessorFunction<Point>() {
			public Iterable<Successor<Point>> from(Point fromState) {
				return Successor.map(fromState,
						maze.validMovesFromCell(fromState));
			}
		};
		
		// Cost function used to evaluate a movement
		CostFunction<Point> g = new CostFunction<Point>() {
			public double evaluate(Successor<Point> successor) {
				Point from = successor.from();
				Point to = successor.state();
				if (from != null && to != null) {
					return Math.sqrt((from.x - to.x) * (from.x - to.x)
							+ (from.y - to.y) * (from.y - to.y));
				}
				return 0;
			}
		};
		// Heuristic function used to estimate the distance to the goal
		HeuristicFunction<Point> h = new HeuristicFunction<Point>() {
			public double estimate(Point from) {
				Point to = maze.getGoalLoc();
				return Math.sqrt((from.x - to.x) * (from.x - to.x)
						+ (from.y - to.y) * (from.y - to.y));
			}
		};
		// Create the iterator
		AstarIterator<Point> iterator = new AstarIterator.Builder<Point>(
				maze.getInitialLoc(), successor).cost(g).heuristic(h).build();
		
		int step = 0;
		Stopwatch timer = new Stopwatch().start();
		while(iterator.hasNext()){
			HeuristicNode<Point> location = iterator.next();
			if (location.successor().state().equals(maze.getGoalLoc())){
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
