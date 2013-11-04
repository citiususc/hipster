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

import es.usc.citius.lab.hipster.algorithm.BellmanFord;
import es.usc.citius.lab.hipster.algorithm.problem.InformedSearchProblem;
import es.usc.citius.lab.hipster.function.impl.BinaryOperation;
import es.usc.citius.lab.hipster.node.NodeFactory;
import es.usc.citius.lab.hipster.node.CostNode;
import es.usc.citius.lab.hipster.node.impl.HeuristicNodeImplFactory;

import java.util.Iterator;

/**
 * Implementation of {@link es.usc.citius.lab.hipster.algorithm.factory.AlgorithmIteratorFactory}
 * to obtain {@link es.usc.citius.lab.hipster.algorithm.BellmanFord} iterator instances. 
 * 
 * @param <S> class defining the state
 * @param <T> class defining the cost
 * 
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 * @since 0.1.0
 */
public class BellmanFordIteratorFactory<S, T extends Comparable<T>> implements
        AlgorithmIteratorFactory<S, CostNode<S,T>> {
    private final InformedSearchProblem<S, T> searchProblem;
    private final BinaryOperation<T> costAccumulator;

    /**
     * Default constructor for the Bellman-Ford factory that takes the definition of a informed
     * search problem and the accumulation operation for the cost type.
     * 
     * @param searchProblem informed search problem definition
     * @param costAccumulator accumulation operation for the cost type
     */
    public BellmanFordIteratorFactory(
            InformedSearchProblem<S, T> searchProblem, BinaryOperation<T> costAccumulator) {
        this.searchProblem = searchProblem;
        this.costAccumulator = costAccumulator;
    }

    public Iterator<CostNode<S, T>> create() {
        NodeFactory<S, CostNode<S, T>> factory = new HeuristicNodeImplFactory<S, T>(
                searchProblem.getCostFunction(), this.costAccumulator)
                .toCostNodeFactory();

        return new BellmanFord<S, T>(
                searchProblem.getInitialState(),
                searchProblem.getTransitionFunction(), factory);

    }

}
