/*
 * Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS), University of Santiago de Compostela (USC).
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

package es.usc.citius.lab.hipster.algorithm.problem;

import es.usc.citius.lab.hipster.function.CostFunction;
import es.usc.citius.lab.hipster.function.HeuristicFunction;
import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.function.impl.BinaryOperation;
import es.usc.citius.lab.hipster.node.NodeFactory;
import es.usc.citius.lab.hipster.node.HeuristicNode;
import es.usc.citius.lab.hipster.node.impl.HeuristicNodeImplFactory;

/**
 * A default search problem that works with doubles. By default this
 * implementation uses a {@link es.usc.citius.lab.hipster.function.impl.BinaryOperation#doubleAdditionOp()}
 * as the default accumulator. The {@link CostFunction} to compute the cost of a node
 * must work with {@link Double}.
 *
 * @author Pablo Rodríguez Mier
 */
public class DefaultSearchProblem<S> implements HeuristicSearchProblem<S, Double> {

    private S initialState;
    private S goalState;
    private TransitionFunction<S> transitionFunction;
    private CostFunction<S, Double> costFunction;
    private NodeFactory<S, HeuristicNode<S, Double>> nodeFactory;
    private HeuristicFunction<S, Double> heuristicFunction = new HeuristicFunction<S, Double>() {
        @Override
        public Double estimate(S state) {
            return 0d;
        }
    };


    public DefaultSearchProblem(S initialState, S goalState, TransitionFunction<S> transitionFunction, CostFunction<S, Double> costFunction) {
        this.initialState = initialState;
        this.goalState = goalState;
        this.transitionFunction = transitionFunction;
        this.costFunction = costFunction;
        this.nodeFactory = new HeuristicNodeImplFactory<S, Double>(this.costFunction,
                this.heuristicFunction,
                BinaryOperation.doubleAdditionOp());
    }

    @Override
    public S getInitialState() {
        return this.initialState;
    }

    @Override
    public S getGoalState() {
        return this.goalState;
    }

    @Override
    public TransitionFunction<S> getTransitionFunction() {
        return this.transitionFunction;
    }

    @Override
    public CostFunction<S, Double> getCostFunction() {
        return this.costFunction;
    }

    @Override
    public HeuristicFunction<S, Double> getHeuristicFunction() {
        return this.heuristicFunction;
    }
}
