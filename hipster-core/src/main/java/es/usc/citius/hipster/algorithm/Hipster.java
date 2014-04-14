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

package es.usc.citius.hipster.algorithm;


import es.usc.citius.hipster.model.Transition;
import es.usc.citius.hipster.model.function.HeuristicFunction;
import es.usc.citius.hipster.model.function.NodeExpander;
import es.usc.citius.hipster.model.function.NodeFactory;
import es.usc.citius.hipster.model.function.impl.BinaryOperation;
import es.usc.citius.hipster.model.function.impl.HeuristicNodeFactoryImpl;
import es.usc.citius.hipster.model.function.impl.LazyNodeExpander;
import es.usc.citius.hipster.model.impl.HeuristicNodeImpl;
import es.usc.citius.hipster.model.impl.UnweightedNode;
import es.usc.citius.hipster.model.problem.HeuristicSearchProblem;
import es.usc.citius.hipster.model.problem.InformedSearchProblem;
import es.usc.citius.hipster.model.problem.SearchProblem;

public final class Hipster {

    //TODO; Refactor - Remove redundant code

    public static <A,S> AStar<A,S,Double,HeuristicNodeImpl<A,S,Double>> createAStar(HeuristicSearchProblem<A,S,Double> problem){
        // Create a Lazy Node Expander by default
        SearchComponents<A,S> components = createDefaultComponents(problem);
        // Create the algorithm with all those components
        AStar<A, S, Double, HeuristicNodeImpl<A, S, Double>> algorithm =
            new AStar<A, S, Double, HeuristicNodeImpl<A,S,Double>>(components.initialNode, components.expander);
        // Put the optional goal state
        algorithm.setGoalState(problem.getGoalState());
        return algorithm;
    }

    public static <A,S> AStar<A,S,Double,HeuristicNodeImpl<A,S,Double>> createDijkstra(InformedSearchProblem<A,S,Double> problem){
        // The Dijkstra impl. is the same as the A* but without using heuristics
        HeuristicNodeFactoryImpl<A,S,Double> factory = new HeuristicNodeFactoryImpl<A,S,Double>(
                problem.getCostFunction(),
                new HeuristicFunction<S, Double>() {
                    @Override
                    public Double estimate(S state) {
                        return BinaryOperation.doubleAdditionOp().getIdentityElem();
                    }
                },
                BinaryOperation.doubleAdditionOp());
        // Make the initial node. The initial node contains the initial state
        // of the problem, and it comes from no previous node (null) and using no action (null)
        HeuristicNodeImpl<A,S,Double> initialNode = factory.makeNode(null, Transition.<A,S>create(null, null, problem.getInitialState()));
        // Create a Lazy Node Expander by default
        NodeExpander<A,S,HeuristicNodeImpl<A,S,Double>> expander = new LazyNodeExpander<A, S, HeuristicNodeImpl<A, S, Double>>(problem.getTransitionFunction(), factory);
        // Create the algorithm with all those components
        AStar<A, S, Double, HeuristicNodeImpl<A, S, Double>> algorithm =
                new AStar<A, S, Double, HeuristicNodeImpl<A,S,Double>>(initialNode, expander);
        // Put the optional goal state
        algorithm.setGoalState(problem.getGoalState());
        return algorithm;
    }

    /**
     * Create a simple BFS algorithm using nodes of type UnweightedNode
     * @param problem search problem components
     * @param <A> action type
     * @param <S> state type
     * @return
     */
    public static <A,S> BreadthFirstSearch<A,S,UnweightedNode<A,S>> createBreadthFirstSearch(SearchProblem<A,S> problem){
        BreadthFirstSearch<A, S, UnweightedNode<A, S>> algorithm = new BreadthFirstSearch<A, S, UnweightedNode<A, S>>(problem.getInitialState(), problem.getTransitionFunction(),
                new NodeFactory<A, S, UnweightedNode<A, S>>() {
                    @Override
                    public UnweightedNode<A, S> makeNode(UnweightedNode<A, S> fromNode, Transition<A, S> transition) {
                        return new UnweightedNode<A, S>(fromNode, transition);
                    }
                }
        );
        algorithm.setGoalState(problem.getGoalState());
        return algorithm;
    }

    public static <A,S> IDAStar<A,S,Double,HeuristicNodeImpl<A,S,Double>> createIDAStar(HeuristicSearchProblem<A,S,Double> problem){
        SearchComponents<A,S> components = createDefaultComponents(problem);

        IDAStar<A, S, Double, HeuristicNodeImpl<A, S, Double>> algorithm =
                new IDAStar<A, S, Double, HeuristicNodeImpl<A, S, Double>>(
                        components.initialNode, components.expander);
        algorithm.setGoalState(problem.getGoalState());
        return algorithm;
    }

    private static class SearchComponents<A,S> {
        private HeuristicNodeImpl<A, S, Double> initialNode;
        private NodeExpander<A,S,HeuristicNodeImpl<A, S, Double>> expander;

        private SearchComponents(HeuristicNodeImpl<A, S, Double> initialNode, NodeExpander<A, S, HeuristicNodeImpl<A, S, Double>> expander) {
            this.initialNode = initialNode;
            this.expander = expander;
        }
    }

    private static <A,S> SearchComponents<A,S> createDefaultComponents(HeuristicSearchProblem<A, S, Double> problem){
        HeuristicNodeFactoryImpl<A, S, Double> factory = createDefaultHeuristicNodeFactory(problem);
        HeuristicNodeImpl<A,S,Double> initialNode = factory.makeNode(null, Transition.<A,S>create(null, null, problem.getInitialState()));
        LazyNodeExpander<A, S, HeuristicNodeImpl<A, S, Double>> nodeExpander = new LazyNodeExpander<A, S, HeuristicNodeImpl<A, S, Double>>(
                problem.getTransitionFunction(),
                factory);

        return new SearchComponents<A, S>(initialNode, nodeExpander);
    }


    private static <A,S> HeuristicNodeFactoryImpl<A, S, Double> createDefaultHeuristicNodeFactory(HeuristicSearchProblem<A, S, Double> problem){
        return new HeuristicNodeFactoryImpl<A,S,Double>(
                problem.getCostFunction(),
                problem.getHeuristicFunction(),
                BinaryOperation.doubleAdditionOp());
    }
}
