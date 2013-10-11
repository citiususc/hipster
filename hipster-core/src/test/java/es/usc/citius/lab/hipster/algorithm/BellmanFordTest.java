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


import es.usc.citius.lab.hipster.node.CostNode;
import es.usc.citius.lab.hipster.testutils.MazeHeuristicSearchProblem;
import es.usc.citius.lab.hipster.util.maze.MazeSearch;
import es.usc.citius.lab.hipster.util.maze.Mazes;
import org.junit.Test;

import java.awt.*;
import java.util.Iterator;


import static org.junit.Assert.assertEquals;

/**
 * Executes tests over predefined maze strings, comparing the results between
 * Jung and AD* iterator.
 *
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @author Pablo Rodríguez Mier <pablo.rodriguez.mier@usc.es>
 * @version 1.0
 * @since 26/03/2013
 */
public class BellmanFordTest {

    public BellmanFordTest() {
    }

    @Test
    public void BF_Maze1() throws InterruptedException {
        execute(Mazes.Example.MAZE1);
    }

    @Test
    public void BF_Maze2() throws InterruptedException {
        execute(Mazes.Example.MAZE2);
    }

    @Test
    public void BF_Maze3() throws InterruptedException {
        execute(Mazes.Example.MAZE3);
    }

    @Test
    public void BF_Maze4() throws InterruptedException {
        execute(Mazes.Example.MAZE4);
    }

    @Test
    public void BF_Maze5() throws InterruptedException {
        execute(Mazes.Example.MAZE5);
    }

    /*
    @Test
    public void negativeCycles() {
        final DirectedGraph<String, JungEdge<String>> graph = new DirectedSparseMultigraph<String, JungEdge<String>>();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addEdge(new JungEdge<>("A", "B", 1.0), "A", "B");
        graph.addEdge(new JungEdge<>("B", "A", -2.0), "B", "A");
        graph.addEdge(new JungEdge<>("B", "C", 1.0), "B", "C");
    }

    @Test
    public void PositiveWeightedGraph() {
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

        final CostOperator<Double> accumulator = CostOperator.doubleAdditionOp();
        TransitionFunction<String> transition = new TransitionFunction<String>() {
            public Iterable<Transition<String>> from(String current) {
                Collection<Transition<String>> transitions = new ArrayList<Transition<String>>();
                Collection<JungEdge<String>> edges = graph.getOutEdges(current);
                //System.out.println("Edges from " + current + ": " + edges);
                for (JungEdge<String> edge : edges) {
                    transitions.add(new Transition<String>(current, edge.getDest()));
                }
                return transitions;
            }
        };

        NodeFactory<String, CostNode<String, Double>> factory = new HeuristicNodeImplFactory<String, Double>(new CostFunction<String, Double>() {
            public Double evaluate(Transition<String> transition) {
                if (transition.from() == null) {
                    return accumulator.getIdentityElem();
                }
                return graph.findEdge(transition.from(), transition.to()).getCost();
            }


        }, accumulator).toCostNodeFactory();


        BellmanFord<String, Double> it = new BellmanFord<String, Double>("A", transition, factory);
        double result = Double.POSITIVE_INFINITY;
        while (it.hasNext()) {
            Node<String> edgeNode = it.next();
            //System.out.println("Exploring " + edgeNode.transition().from() + "->" + edgeNode.transition().to());
            String vertex = edgeNode.transition().to();
            if (vertex.equals("E")) {
                // Evaluate cost:
                Double cost = 0d;
                for (Node<String> node : edgeNode.path()) {
                    if (node.transition().from() == null) {
                        continue;
                    }
                    JungEdge<String> edge = graph.findEdge(node.transition().from(), node.transition().to());
                    //System.out.println(edge);
                    cost += edge.getCost();
                }
                if (cost < result) {
                    result = cost;
                }
            }
        }
        assertEquals(7.0, result, 0.0);
    } */

    private void execute(Mazes.Example example) {
        Iterator<? extends CostNode<Point, Double>> it = SearchIterators.bellmanFord(new MazeHeuristicSearchProblem(example.getMaze(), false));
        MazeSearch.Result resultIterator = MazeSearch.executeIteratorSearch(it, example.getMaze().getGoalLoc());
        assertEquals(example.getMinimalPathCost(), resultIterator.getCost(), 0.00000001);
    }

}
