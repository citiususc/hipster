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
import es.usc.citius.hipster.model.function.impl.BinaryOperation;
import es.usc.citius.hipster.model.function.impl.HeuristicNodeFactoryImpl;
import es.usc.citius.hipster.model.impl.HeuristicNodeImpl;
import es.usc.citius.hipster.model.impl.UnweightedNode;
import es.usc.citius.hipster.model.function.NodeFactory;
import es.usc.citius.hipster.model.problem.HeuristicSearchProblem;
import es.usc.citius.hipster.model.problem.InformedSearchProblem;
import es.usc.citius.hipster.model.problem.SearchProblem;

public final class Hipster {

    public static <A,S> AStar<A,S,Double,HeuristicNodeImpl<A,S,Double>> createAStar(HeuristicSearchProblem<A,S,Double> problem){
        HeuristicNodeFactoryImpl<A,S,Double> factory = new HeuristicNodeFactoryImpl<A,S,Double>(
                problem.getCostFunction(),
                problem.getHeuristicFunction(),
                BinaryOperation.doubleAdditionOp());
        AStar<A, S, Double, HeuristicNodeImpl<A, S, Double>> algorithm =
            new AStar<A, S, Double, HeuristicNodeImpl<A,S,Double>>(
                problem.getInitialState(),
                problem.getTransitionFunction(),
                factory);
        algorithm.setGoalState(problem.getGoalState());
        return algorithm;
    }

    public static <A,S> AStar<A,S,Double,HeuristicNodeImpl<A,S,Double>> createDijkstra(InformedSearchProblem<A,S,Double> problem){
        HeuristicNodeFactoryImpl<A,S,Double> factory = new HeuristicNodeFactoryImpl<A,S,Double>(
                problem.getCostFunction(),
                new HeuristicFunction<S, Double>() {
                    @Override
                    public Double estimate(S state) {
                        return BinaryOperation.doubleAdditionOp().getIdentityElem();
                    }
                },
                BinaryOperation.doubleAdditionOp());

        AStar<A, S, Double, HeuristicNodeImpl<A, S, Double>> algorithm =
            new AStar<A, S, Double, HeuristicNodeImpl<A, S, Double>>(
                problem.getInitialState(),
                problem.getTransitionFunction(),
                factory);
        // TODO: Improve the way to set an optional goal
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
        HeuristicNodeFactoryImpl<A,S,Double> factory = new HeuristicNodeFactoryImpl<A,S,Double>(
                problem.getCostFunction(),
                problem.getHeuristicFunction(),
                BinaryOperation.doubleAdditionOp());
        IDAStar<A, S, Double, HeuristicNodeImpl<A, S, Double>> algorithm =
                new IDAStar<A, S, Double, HeuristicNodeImpl<A, S, Double>>(
                        problem.getInitialState(),
                        problem.getTransitionFunction(),
                        factory);
        algorithm.setGoalState(problem.getGoalState());
        return algorithm;
    }
}
