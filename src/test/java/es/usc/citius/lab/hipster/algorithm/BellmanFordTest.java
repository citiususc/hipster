/*
 * Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.usc.citius.lab.hipster.algorithm;

import static org.junit.Assert.assertEquals;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import es.usc.citius.lab.hipster.function.CostFunction;
import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.node.HeuristicNode;
import es.usc.citius.lab.hipster.node.Node;
import es.usc.citius.lab.hipster.node.NodeFactory;
import es.usc.citius.lab.hipster.node.Transition;
import es.usc.citius.lab.hipster.node.astar.HeuristicNumericNode;
import es.usc.citius.lab.hipster.node.astar.HeuristicNumericNodeBuilder;
import es.usc.citius.lab.hipster.testutils.AlgorithmIteratorFromMazeCreator;
import es.usc.citius.lab.hipster.testutils.JungDirectedGraphFromMazeCreator;
import es.usc.citius.lab.hipster.testutils.JungEdge;
import es.usc.citius.lab.hipster.testutils.MazeSearch;
import es.usc.citius.lab.hipster.util.maze.Maze2D;

/**
 * Executes tests over predefined maze strings, comparing the results between
 * Jung and AD* iterator.
 * 
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @author Pablo Rodríguez Mier <pablo.rodriguez.mier@usc.es>
 * @since 26/03/2013
 * @version 1.0
 */
public class BellmanFordTest {

	public BellmanFordTest() {
	}

	@Test
	public void BF_Maze1() throws InterruptedException {
		Maze2D maze = new Maze2D(MazeSearch.getTestMaze1());
		execute(maze, false);
	}

	@Test
	public void BF_Maze2() throws InterruptedException {
		Maze2D maze = new Maze2D(MazeSearch.getTestMaze2());
		execute(maze, false);
	}

	@Test
	public void BF_Maze3() throws InterruptedException {
		Maze2D maze = new Maze2D(MazeSearch.getTestMaze3());
		execute(maze, false);
	}

	@Test
	public void BF_Maze4() throws InterruptedException {
		Maze2D maze = new Maze2D(MazeSearch.getTestMaze4());
		execute(maze, false);
	}

	@Test
	public void BF_Maze5() throws InterruptedException {
		Maze2D maze = new Maze2D(MazeSearch.getTestMaze5());
		execute(maze, false);
	}

	@Test
    public void PositiveWeightedGraph(){
    	final DirectedGraph<String, JungEdge<String>> graph = new DirectedSparseMultigraph<String, JungEdge<String>>();
    	graph.addVertex("A");
    	graph.addVertex("B");
    	graph.addVertex("C");
    	graph.addVertex("D");
    	graph.addVertex("E");
    	//JungEdge<String> initial = new JungEdge<String>("A", "B", 1.0);
    	//graph.addEdge(initial, "A", "B");
    	graph.addEdge(new JungEdge<String>("A", "B", 1.0), "A", "B");
    	graph.addEdge(new JungEdge<String>("A", "C", 4.0), "A", "C");
    	graph.addEdge(new JungEdge<String>("A", "E", 10.0), "A", "E");
    	graph.addEdge(new JungEdge<String>("B", "C", 2.0), "B", "C");
    	graph.addEdge(new JungEdge<String>("B", "D", 4.0), "B", "D");
    	graph.addEdge(new JungEdge<String>("C", "D", 3.0), "C", "D");
    	graph.addEdge(new JungEdge<String>("D", "A", 2.0), "D", "A");
    	graph.addEdge(new JungEdge<String>("D", "E", 2.0), "D", "E");
    	
    	TransitionFunction<String> transition = new TransitionFunction<String>() {
			public Iterable<Transition<String>> from(String current) {
				Collection<Transition<String>> transitions = new ArrayList<Transition<String>>();
				Collection<JungEdge<String>> edges = graph.getOutEdges(current);
				//System.out.println("Edges from " + current + ": " + edges);
				for(JungEdge<String> edge : edges){
					transitions.add(new Transition<String>(current, edge.getDest()));
				}
				return transitions;
			}
		};
		
		NodeFactory<String, HeuristicNode<String>> builder = new HeuristicNumericNodeBuilder<String>(
				new CostFunction<String, Double>() {
					public Double evaluate(
							Transition<String> transition) {
						if (transition.from()==null){
							return 0d;
						}
						return graph.findEdge(transition.from(), transition.to()).getCost();
					}
		});
		
		BellmanFord<String> it = new BellmanFord<String>("A", transition, builder, null);
		while(it.hasNext()){
			Node<String> edgeNode = it.next();
			System.out.println("Exploring " + edgeNode.transition().from() + "->" + edgeNode.transition().to());
			String vertex = edgeNode.transition().to();
			if (vertex.equals("E")){
				// Evaluate cost:
				Double cost = 0d;
				for(Node<String> node : edgeNode.path()){
					if (node.transition().from() == null){
						continue;
					}
					JungEdge<String> edge = graph.findEdge(node.transition().from(), node.transition().to());
					System.out.println(edge);
					cost += edge.getCost();
				}
				System.out.println("Cost to goal: " + cost);
			}
		}
    	
    }

	private void execute(Maze2D maze, boolean heuristic)
			throws InterruptedException {
		BellmanFord<Point> it = AlgorithmIteratorFromMazeCreator.bellmanFord(
				maze, heuristic);
		DirectedGraph<Point, JungEdge<Point>> graph = JungDirectedGraphFromMazeCreator
				.create(maze);
		MazeSearch.Result resultJung = MazeSearch
				.executeJungSearch(graph, maze);
		MazeSearch.Result resultIterator = MazeSearch
				.executePrintIteratorSearch(it, maze, false);
		assertEquals(resultIterator.getCost(), resultJung.getCost(), 0.001);
	}

}
