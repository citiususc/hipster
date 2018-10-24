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

package es.usc.citius.hipster.model.node.impl;


import es.usc.citius.hipster.model.Transition;
import es.usc.citius.hipster.model.function.CostFunction;
import es.usc.citius.hipster.model.function.HeuristicFunction;
import es.usc.citius.hipster.model.function.impl.BinaryOperation;
import es.usc.citius.hipster.model.node.factory.NodeFactory;

/**
 * Implementation of {@link NodeFactory} for nodes of type
 * {@link WeightedNode}. The new nodes have a cost which
 * is the sum of the cost of the parent and the cost of the {@link es.usc.citius.hipster.model.Transition}
 * between the parent node and the current one, calculated by a {@link es.usc.citius.hipster.model.function.CostFunction}.
 *
 * Also, the score is the sum of the cost of the node and the estimated cost to the goal,
 * according to a {@link es.usc.citius.hipster.model.function.HeuristicFunction}.
 *
 * @param <A> type of the actions
 * @param <S> type of the states
 * @param <C> type of the cost
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 * @author Adrián González Sieira <<a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 */
public class WeightedNodeFactory<A,S,C extends Comparable<C>> implements NodeFactory<A,S, WeightedNode<A,S,C>>{

    protected CostFunction<A,S,C> costFunction;
    protected HeuristicFunction<S,C> heuristicFunction;
    protected BinaryOperation<C> costAccumulator;


    /**
     * Instantiates a node factory using a cost function, a heuristic function and a cost accumulator.
     *
     * @param costFunction cost function
     * @param heuristicFunction heuristic function
     * @param costAccumulator function to accumulate the cost elements
     */
    public WeightedNodeFactory(CostFunction<A, S, C> costFunction, HeuristicFunction<S, C> heuristicFunction, BinaryOperation<C> costAccumulator) {
        this.costFunction = costFunction;
        this.heuristicFunction = heuristicFunction;
        this.costAccumulator = costAccumulator;
    }

    /**
     * Instantiates a node factory using a cost function and a cost accumulator. The
     * heuristic function is considered to return always the identity element of the cost.
     *
     * @param costFunction cost function
     * @param costAccumulator function to accumulate the cost elements
     */
    public WeightedNodeFactory(CostFunction<A, S, C> costFunction, BinaryOperation<C> costAccumulator) {
        this.costFunction = costFunction;
        this.heuristicFunction = new HeuristicFunction<S, C>() {
            public C estimate(S state) {
                return WeightedNodeFactory.this.costAccumulator.getIdentityElem();
            }
        };
        this.costAccumulator = costAccumulator;
    }

    @Override
    public WeightedNode<A, S, C> makeNode(WeightedNode<A, S, C> fromNode, Transition<A, S> transition) {
        C cost, estimatedDistance, score;

        if (fromNode == null){
            cost = costAccumulator.getIdentityElem();
        } else {
            cost = costAccumulator.apply(fromNode.getCost(), this.costFunction.evaluate(transition));
        }
        estimatedDistance = this.heuristicFunction.estimate(transition.getState());
        score = costAccumulator.apply(cost, estimatedDistance);

        return new WeightedNode<A,S,C>(fromNode, transition.getState(), transition.getAction(), cost, estimatedDistance, score);
    }

    public CostFunction<A, S, C> getCostFunction() {
        return costFunction;
    }

    public HeuristicFunction<S, C> getHeuristicFunction() {
        return heuristicFunction;
    }

    public BinaryOperation<C> getCostAccumulator() {
        return costAccumulator;
    }
}
