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

import es.usc.citius.lab.hipster.algorithm.factory.ADStarIteratorFactory;
import es.usc.citius.lab.hipster.algorithm.factory.AStarIteratorFactory;
import es.usc.citius.lab.hipster.algorithm.factory.BellmanFordIteratorFactory;
import es.usc.citius.lab.hipster.algorithm.problem.HeuristicSearchProblem;
import es.usc.citius.lab.hipster.algorithm.problem.SearchProblem;
import es.usc.citius.lab.hipster.function.ScalarFunction;
import es.usc.citius.lab.hipster.node.CostNode;
import es.usc.citius.lab.hipster.node.HeuristicNode;
import es.usc.citius.lab.hipster.node.adstar.ADStarNode;

import java.util.Iterator;

/**
 * Methods to obtain a search algorithm given the definition of the problem
 * to solve.
 * @see HeuristicSearchProblem
 */
public final class SearchIterators {

    private SearchIterators() {
    }

    /**
     * Creates a {@literal A*} search algorithm.
     *
     * @param problem description of the heuristic problem (see {@link HeuristicSearchProblem})
     * @param <S> state class
     * @param <T> cost type (for example, {@link Double}).
     * @return new A-Star iterator that iterates over the {@link CostNode}
     * @see AStar
     */
    public static <S, T extends Comparable<T>> Iterator<HeuristicNode<S, T>> aStar(HeuristicSearchProblem<S, T> problem) {
        return new AStarIteratorFactory<S, T>(problem).create();
    }

    /**
     * Creates a <a href="en.wikipedia.org/wiki/Bellman–Ford_algorithm">Bellman-Ford</a> algorithm
     *
     * @param problem description of the heuristic problem (see {@link HeuristicSearchProblem})
     * @param <S> state class
     * @param <T> cost type (for example, {@link Double}).
     * @return new BellmanFord iterator that iterates over the {@link CostNode}
     * @see BellmanFord
     */
    public static <S, T extends Comparable<T>> Iterator<CostNode<S, T>> bellmanFord(SearchProblem<S, T> problem) {
        return new BellmanFordIteratorFactory<S, T>(problem).create();
    }

    /**
     * Creates an AD-Star algorithm.
     * @param problem description of the heuristic problem (see {@link HeuristicSearchProblem})
     * @param scale {@link ScalarFunction} used to perform internal scale operations to scale the cost type used
     * @param epsilon
     * @param min
     * @param max
     * @param <S> state class
     * @param <T> cost type (for example, {@link Double}).
     * @return
     */
    public static <S, T extends Comparable<T>> Iterator<ADStarNode<S, T>> adStar(HeuristicSearchProblem<S, T> problem, ScalarFunction<T> scale, double epsilon, T min, T max) {
        return new ADStarIteratorFactory<S, T>(problem, scale, epsilon, min, max).create();
    }

}
