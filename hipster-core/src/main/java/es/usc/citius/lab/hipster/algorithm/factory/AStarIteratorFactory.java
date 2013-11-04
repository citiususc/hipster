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

import es.usc.citius.lab.hipster.algorithm.AStar;
import es.usc.citius.lab.hipster.algorithm.problem.HeuristicSearchProblem;
import es.usc.citius.lab.hipster.algorithm.problem.InformedSearchProblem;
import es.usc.citius.lab.hipster.function.CostFunction;
import es.usc.citius.lab.hipster.function.HeuristicFunction;
import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.function.impl.BinaryOperation;
import es.usc.citius.lab.hipster.node.NodeFactory;
import es.usc.citius.lab.hipster.node.HeuristicNode;
import es.usc.citius.lab.hipster.node.impl.HeuristicNodeImplFactory;

import java.util.Iterator;

/**
 * This class is an implementation of {@link es.usc.citius.lab.hipster.algorithm.factory.AlgorithmIteratorFactory}
 * to obtain an {@link es.usc.citius.lab.hipster.algorithm.AStar} iterator to solve a search problem given
 * its definition. 
 * 
 * The way of using the factory to obtain instances of the iterator is using an
 * {@link es.usc.citius.lab.hipster.algorithm.problem.InformedSearchProblem} or a
 * {@link es.usc.citius.lab.hipster.algorithm.problem.HeuristicSearchProblem} definition.
 * 
 * @param <S> class defining the state
 * @param <T> class defining the cost
 * 
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 * @since 0.1.0
 */
public class AStarIteratorFactory<S, T extends Comparable<T>> implements
        AlgorithmIteratorFactory<S, HeuristicNode<S,T>> {
    private final HeuristicSearchProblem<S, T> f;
    private final BinaryOperation<T> costAccumulator;

    /**
     * Constructor for the AStar factory that takes the definition of a heuristic problem and the 
     * accumulation operation for the cost type.
     * 
     * @param problem heuristic problem definition
     * @param costAccumulator binary operation to accumulate the cost
     */
    public AStarIteratorFactory(HeuristicSearchProblem<S, T> problem, BinaryOperation<T> costAccumulator) {
        this.f = problem;
        this.costAccumulator = costAccumulator;
    }

    /**
     * Constructor for the AStar factory that takes the definition of an informed search problem (without heuristic)
     * and the accumulator operation for the cost type.
     * 
     * @param problem informed search problem definition
     * @param costAccumulator binary operation to accumulate the cost
     */
    public AStarIteratorFactory(final InformedSearchProblem<S, T> problem, final BinaryOperation<T> costAccumulator) {
        this.f = new HeuristicSearchProblem<S, T>() {
            @Override
            public HeuristicFunction<S, T> getHeuristicFunction() {
                return new HeuristicFunction<S, T>() {
                    @Override
                    public T estimate(S state) {
                        return costAccumulator.getIdentityElem();
                    }
                };
            }

            @Override
            public S getInitialState() {
                return problem.getInitialState();
            }

            @Override
            public S getGoalState() {
                return problem.getGoalState();
            }

            @Override
            public TransitionFunction<S> getTransitionFunction() {
                return problem.getTransitionFunction();
            }

            @Override
            public CostFunction<S, T> getCostFunction() {
                return problem.getCostFunction();
            }
        };
        this.costAccumulator = costAccumulator;
    }

    public Iterator<HeuristicNode<S, T>> create() {
        NodeFactory<S, HeuristicNode<S, T>> factory = new HeuristicNodeImplFactory<S, T>(
                f.getCostFunction(), f.getHeuristicFunction(), this.costAccumulator);

        AStar<S, T> astar = new AStar<S, T>(f.getInitialState(), f.getTransitionFunction(),
                factory);
        // Create custom queue
        //PriorityFibonacciQueue<HeuristicNode<S,T>> queue = new PriorityFibonacciQueue<HeuristicNode<S, T>>(new HeuristicNodePriorityEvaluator<S,T>());
        //queue.offer(astar.getQueue().poll());
        //astar.setQueue(queue);
        return astar;
    }

}
