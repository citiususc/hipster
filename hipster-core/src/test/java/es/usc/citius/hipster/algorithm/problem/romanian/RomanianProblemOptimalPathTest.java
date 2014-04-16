/*
 * Copyright 2014 CITIUS <http://citius.usc.es>, University of Santiago de Compostela.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package es.usc.citius.hipster.algorithm.problem.romanian;


import es.usc.citius.hipster.algorithm.AStar;
import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.model.Transition;
import es.usc.citius.hipster.model.function.CostFunction;
import es.usc.citius.hipster.model.function.HeuristicFunction;
import es.usc.citius.hipster.model.function.TransitionFunction;
import es.usc.citius.hipster.model.impl.WeightedNode;
import es.usc.citius.hipster.model.problem.HeuristicSearchProblem;
import es.usc.citius.hipster.model.problem.ProblemBuilder;
import es.usc.citius.hipster.util.examples.RomanianProblem;
import es.usc.citius.hipster.util.graph.GraphEdge;
import es.usc.citius.hipster.util.graph.HipsterGraph;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static es.usc.citius.hipster.util.examples.RomanianProblem.City;
import static org.junit.Assert.assertEquals;

public class RomanianProblemOptimalPathTest {

    @Test
    public void RomanianProblemAStarTest() {

        final HipsterGraph<City, Double> graph = RomanianProblem.graph();

        HeuristicSearchProblem<GraphEdge<City,Double>, City, Double> p = ProblemBuilder.create()
                .initialState(City.Arad)
                .goalState(City.Bucharest)
                // Actions = Edges of the graph
                .defineProblemWithExplicitActions()
                // Create the TransitionFunction<Action,State> where
                // Actions = GraphEdge<City,Double> (edges of the graph)
                // States = City
                .useTransitionFunction(new TransitionFunction<GraphEdge<City, Double>, City>() {
                    @Override
                    public Iterable<Transition<GraphEdge<City, Double>, City>> transitionsFrom(City fromCity) {
                        Set<Transition<GraphEdge<City, Double>, City>> successors = new HashSet<Transition<GraphEdge<City, Double>, City>>();
                        for (GraphEdge<City, Double> edge : graph.edgesWithVertex(fromCity)) {
                            City toCity = graph.vertexConnectedTo(fromCity, edge);
                            successors.add(Transition.create(fromCity, edge, toCity));
                        }
                        return successors;
                    }
                })
                .useCostFunction(new CostFunction<GraphEdge<City,Double>, City, Double>() {
                    @Override
                    public Double evaluate(Transition<GraphEdge<City,Double>, City> transition) {
                        return transition.getAction().getEdgeValue();
                    }
                })
                .useHeuristicFunction(new HeuristicFunction<City, Double>() {
                    @Override
                    public Double estimate(City state) {
                        return RomanianProblem.heuristics().get(state);
                    }
                })
                .build();

        // Create the custom AStar iterator. Generics may be scary, but IDEs can autocomplete signatures pretty well, don't panic!
        // If you don't need type safety, just define AStar.AStarIter it = Hipster.createAStar(p).iterator()
        AStar<GraphEdge<City,Double>, City, Double, WeightedNode<GraphEdge<City,Double>, City, Double>>.AStarIter it
                = Hipster.createAStar(p).iterator();

        List<Double> expectedScore = Arrays.asList(366d, 393d, 413d, 415d, 417d, 418d);
        int i=0;
        while (it.hasNext()) {
            //System.out.println(node.state() + " - " + node);
            WeightedNode<GraphEdge<City,Double>, City, Double> node = it.next();
            // Check expansion order and score
            assertEquals(expectedScore.get(i), node.getScore());
            i++;
            if (node.state().equals(City.Bucharest)){
                break;
            }
        }
    }

}
