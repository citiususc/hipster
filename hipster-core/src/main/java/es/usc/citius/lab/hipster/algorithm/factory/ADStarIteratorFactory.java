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

package es.usc.citius.lab.hipster.algorithm.factory;

import es.usc.citius.lab.hipster.algorithm.ADStarForward;
import es.usc.citius.lab.hipster.algorithm.problem.HeuristicSearchProblem;
import es.usc.citius.lab.hipster.function.ScalarFunction;
import es.usc.citius.lab.hipster.function.impl.BinaryOperation;
import es.usc.citius.lab.hipster.node.NodeFactory;
import es.usc.citius.lab.hipster.node.adstar.ADStarNode;
import es.usc.citius.lab.hipster.node.adstar.ADStarNodeBuilder;
import es.usc.citius.lab.hipster.node.adstar.ADStarNodeUpdater;

import java.util.Iterator;

/**
 * This class is an implementation of {@link es.usc.citius.lab.hipster.algorithm.factory.AlgorithmIteratorFactory}
 * to obtain the the {@link es.usc.citius.lab.hipster.algorithm.ADStarForward} iterator to solve a search problem
 * given its definition.
 *
 * @param <S> class defining the state
 * @param <T> class defining the cost
 * 
 * @author Adrián González Sieira <<a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 * @since 0.1.0
 */
public class ADStarIteratorFactory<S, T extends Comparable<T>> implements
        AlgorithmIteratorFactory<S, ADStarNode<S,T>> {
    private final HeuristicSearchProblem<S, T> f;
    private final BinaryOperation<T> costAccumulator;
    private ScalarFunction<T> scale;
    private T min;
    private T max;
    private double epsilon;

    /**
     * Main constructor for ADStarForward factory that takes the definition of a heuristic problem
     * the accumulation operation and the scaling operations for the cost type and the sub-optimal
     * bounded value to do the search.
     * 
     * @param problem heuristic problem definition
     * @param costAccumulator accumulation operation for the cost type
     * @param scale scaling operation for the cost type
     * @param epsilon sub-optimal bounded value to do the search
     */
    public ADStarIteratorFactory(HeuristicSearchProblem<S, T> problem, BinaryOperation<T> costAccumulator, ScalarFunction<T> scale, double epsilon) {
        this.f = problem;
        this.max = costAccumulator.getMaxElem();
        this.min = costAccumulator.getIdentityElem();
        this.epsilon = epsilon;
        this.scale = scale;
        this.costAccumulator = costAccumulator;
    }

    public Iterator<ADStarNode<S, T>> create() {

        NodeFactory<S, ADStarNode<S, T>> defaultBuilder = new ADStarNodeBuilder<S, T>(
                this.min, this.max);

        ADStarNodeUpdater<S, T> updater = new ADStarNodeUpdater<S, T>(
                f.getCostFunction(), f.getHeuristicFunction(), this.costAccumulator, this.scale,
                this.epsilon);

        return new ADStarForward<S, T>(f.getInitialState(), f.getGoalState(),
                f.getTransitionFunction(), f.getTransitionFunction(),
                defaultBuilder, updater);
    }

}
