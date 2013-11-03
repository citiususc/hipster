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

import es.usc.citius.lab.hipster.algorithm.factory.*;
import es.usc.citius.lab.hipster.algorithm.problem.HeuristicSearchProblem;
import es.usc.citius.lab.hipster.algorithm.problem.InformedSearchProblem;
import es.usc.citius.lab.hipster.algorithm.problem.SearchProblem;
import es.usc.citius.lab.hipster.function.ScalarFunction;
import es.usc.citius.lab.hipster.function.impl.BinaryOperation;
import es.usc.citius.lab.hipster.function.impl.ScalarOperation;
import es.usc.citius.lab.hipster.node.AbstractNode;
import es.usc.citius.lab.hipster.node.CostNode;
import es.usc.citius.lab.hipster.node.HeuristicNode;
import es.usc.citius.lab.hipster.node.Node;
import es.usc.citius.lab.hipster.node.adstar.ADStarNode;

import java.util.Iterator;
import java.util.List;

/**
 * Methods to obtain a search algorithm given the definition of the problem
 * to solve.
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 * @since 0.1.0
 * @see SearchProblem
 */
public final class Algorithms {

    private Algorithms() {
        throw new RuntimeException("Use the static methods of this class to create search algorithms instead");
    }

    public static final class Search<S, N extends Node<S>> implements Iterable<N>{
        private AlgorithmIteratorFactory<S, N> factory;
        private S goal;

        public interface SearchListener<N> {
            void handle(N node);
        }

        public Search(AlgorithmIteratorFactory<S, N> factory, S goal) {
            this.factory = factory;
            this.goal = goal;
        }

        private N search(Iterator<N> it){
            while(it.hasNext()){
                N node = it.next();
                if (node.transition().to().equals(goal)){
                    return node;
                }
            }
            return null;
        }

        public List<S> getOptimalPath(){
            Iterator<N> it = factory.create();
            return AbstractNode.statesFrom(search(it).path());
        }

        public N search(){
            Iterator<N> it = factory.create();
            return search(it);
        }

        public void search(SearchListener<N> listener){
            Iterator<N> it = factory.create();
            while(it.hasNext()){
                listener.handle(it.next());
            }
        }

        @Override
        public Iterator<N> iterator() {
            return factory.create();
        }
    }


    /**
     * Creates a {@literal A*} search algorithm.
     *
     * @param problem description of the heuristic problem (see {@link HeuristicSearchProblem})
     * @param <S> state type
     * @param <T> cost type (for example, {@link Double}).
     * @return new A-Star iterator that iterates over the {@link HeuristicNode}
     * @see AStar
     */
    public static <S, T extends Comparable<T>> Search<S, HeuristicNode<S,T>> createAStar(HeuristicSearchProblem<S, T> problem, BinaryOperation<T> costAccumulator) {
        return new Search<S, HeuristicNode<S,T>>(new AStarIteratorFactory<S, T>(problem, costAccumulator), problem.getGoalState());
    }

    /**
     * Creates a {@literal A*} search algorithm using Doubles as the default cost type. This method is recommended
     * for most cases.
     *
     * @param problem description of the heuristic problem (see {@link HeuristicSearchProblem})
     * @param <S> state type
     * @return new A-Star search {@link Search} that iterates over the {@link HeuristicNode}.
     * @see AStar
     */
    public static <S> Search<S, HeuristicNode<S,Double>> createAStar(HeuristicSearchProblem<S, Double> problem) {
        return new Search<S, HeuristicNode<S,Double>>(new AStarIteratorFactory<S, Double>(problem, BinaryOperation.doubleAdditionOp()), problem.getGoalState());
    }

    /**
     * Creates a Dijkstra algorithm using the same implementation of the {@literal A*} without heuristics.
     * @param problem {@link SearchProblem} describing the elements of the search problem.
     * @param <S> state type
     * @param <T> cost type (for example, {@link Double}).
     * @return new A-Star search {@link Search} that iterates over the {@link HeuristicNode}.
     */
    public static <S, T extends Comparable<T>> Search<S, HeuristicNode<S,T>> createDijkstra(InformedSearchProblem<S, T> problem, BinaryOperation<T> costAccumulator) {
        return new Search<S, HeuristicNode<S,T>>(new AStarIteratorFactory<S, T>(problem, costAccumulator), problem.getGoalState());
    }

    /**
     * Creates a Dijkstra algorithm using the same implementation of the {@literal A*} without heuristics, using
     * double cost types.
     * @param problem {@link SearchProblem} describing the elements of the search problem.
     * @param <S> state type
     * @return new Dijkstra search {@link Search} that iterates over the {@link HeuristicNode}.
     */
    public static <S> Search<S, HeuristicNode<S,Double>> createDijkstra(InformedSearchProblem<S, Double> problem) {
        return new Search<S, HeuristicNode<S,Double>>(new AStarIteratorFactory<S, Double>(problem, BinaryOperation.doubleAdditionOp()), problem.getGoalState());
    }

    /**
     * Creates a <a href="en.wikipedia.org/wiki/Bellman–Ford_algorithm">Bellman-Ford</a> algorithm
     *
     * @param problem description of the heuristic problem (see {@link HeuristicSearchProblem})
     * @param <S> state class
     * @param <T> cost type (for example, {@link Double}).
     * @return new BellmanFord {@link Search} that iterates over the {@link CostNode}
     * @see BellmanFord
     */
    public static <S, T extends Comparable<T>> Search<S, CostNode<S,T>> createBellmanFord(InformedSearchProblem<S, T> problem, BinaryOperation<T> costAccumulator) {
        return new Search<S, CostNode<S,T>>(new BellmanFordIteratorFactory<S, T>(problem, costAccumulator), problem.getGoalState());
    }

    /**
     * Creates a <a href="en.wikipedia.org/wiki/Bellman–Ford_algorithm">Bellman-Ford</a> algorithm
     * using double cost types.
     *
     * @param problem description of the heuristic problem (see {@link HeuristicSearchProblem})
     * @param <S> state type
     * @return new BellmanFord {@link Search} that iterates over the {@link CostNode}
     * @see BellmanFord
     */
    public static <S> Search<S, CostNode<S,Double>> createBellmanFord(InformedSearchProblem<S, Double> problem) {
        return new Search<S, CostNode<S,Double>>(new BellmanFordIteratorFactory<S, Double>(problem, BinaryOperation.doubleAdditionOp()), problem.getGoalState());
    }

    /**
     * Creates an AD-Star algorithm.
     * @param problem description of the heuristic problem (see {@link HeuristicSearchProblem})
     * @param scale {@link ScalarFunction} used to perform internal scale operations to scale the cost type used
     * @param epsilon
     * @param min
     * @param max
     * @param <S> state type
     * @param <T> cost type (for example, {@link Double}).
     * @return
     */
    public static <S, T extends Comparable<T>> Search<S, ADStarNode<S, T>> createADStar(HeuristicSearchProblem<S, T> problem, BinaryOperation<T> costAccumulator, ScalarFunction<T> scale, double epsilon, T min, T max) {
        return new Search<S, ADStarNode<S, T>>(new ADStarIteratorFactory<S, T>(problem, costAccumulator, scale, epsilon, min, max), problem.getGoalState());
    }

    /**
     * Creates an AD-Star algorithm using double cost types.
     * @param problem description of the heuristic problem (see {@link HeuristicSearchProblem})
     * @param epsilon
     * @param min
     * @param max
     * @param <S> state type
     * @return
     */
    public static <S> Search<S, ADStarNode<S, Double>> createADStar(HeuristicSearchProblem<S, Double> problem, double epsilon, Double min, Double max) {
        return new Search<S, ADStarNode<S, Double>>(new ADStarIteratorFactory<S, Double>(problem, BinaryOperation.doubleAdditionOp(), ScalarOperation.doubleMultiplicationOp(), epsilon, min, max), problem.getGoalState());
    }

    /**
     * Creates a <a href="http://en.wikipedia.org/wiki/Depth-first_search">Depth First Search algorithm (DFS)</a>. The DFS is an uninformed algorithm.
     * DFS is complete (it always find a solution if exists)
     * @param problem simple problem description using the initial state and the transition function.
     * @param <S> state type.
     * @return
     */
    public static <S> Search<S, Node<S>> createDepthFirstSearch(SearchProblem<S> problem) {
        return new Search<S, Node<S>>(new DepthFirstSearchIteratorFactory<S>(problem), problem.getGoalState());
    }

}
