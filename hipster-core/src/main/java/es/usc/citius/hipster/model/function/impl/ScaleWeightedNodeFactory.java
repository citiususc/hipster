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

package es.usc.citius.hipster.model.function.impl;


import es.usc.citius.hipster.model.Transition;
import es.usc.citius.hipster.model.function.CostFunction;
import es.usc.citius.hipster.model.function.HeuristicFunction;
import es.usc.citius.hipster.model.function.NodeFactory;
import es.usc.citius.hipster.model.impl.WeightedNode;

/**
 * Implementation of {@link NodeFactory} for nodes of type
 * {@link WeightedNode}. The new nodes have a cost which
 * is the sum of the cost of the parent and the cost of the {@link Transition}
 * between the parent node and the current one, calculated by a {@link CostFunction}.
 *
 * Also, the score is the sum of the cost of the node and the estimated cost to the goal,
 * according to a {@link HeuristicFunction} (scaled by a factor >= 1.0).
 *
 * @param <A> type of the actions
 * @param <S> type of the states
 * @param <C> type of the cost
 *
 * @author Adrián González Sieira <<a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 */
public class ScaleWeightedNodeFactory<A,S,C extends Comparable<C>> implements NodeFactory<A,S,WeightedNode<A,S,C>>{

    protected CostFunction<A,S,C> costFunction;
    protected HeuristicFunction<S,C> heuristicFunction;
    protected BinaryOperation<C> costAccumulator;
    protected ScalarOperation<C> scalarOperation;
    protected double scaleFactor;


    /**
     * Instantiates a node factory using a cost function, a heuristic function and a cost accumulator.
     *
     * @param costFunction cost function
     * @param heuristicFunction heuristic function
     * @param scaleFactor scalar factor for the heuristic
     * @param costAccumulator function to accumulate the cost elements
     * @param scaleOperation scaling operation for the cost type
     */
    public ScaleWeightedNodeFactory(CostFunction<A, S, C> costFunction, HeuristicFunction<S, C> heuristicFunction, double scaleFactor, BinaryOperation<C> costAccumulator, ScalarOperation<C> scaleOperation) {
        this.costFunction = costFunction;
        this.heuristicFunction = heuristicFunction;
        this.costAccumulator = costAccumulator;
        this.scalarOperation = scaleOperation;
        this.scaleFactor = scaleFactor;
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
        score = costAccumulator.apply(cost, scalarOperation.scale(estimatedDistance, scaleFactor));

        return new WeightedNode<A,S,C>(fromNode, transition.getState(), transition.getAction(), cost, estimatedDistance, score);
    }

    public CostFunction<A, S, C> getCostFunction() {
        return costFunction;
    }

    public HeuristicFunction<S, C> getHeuristicFunction() {
        return heuristicFunction;
    }

    public double getScaleFactor() {
        return scaleFactor;
    }

    public BinaryOperation<C> getCostAccumulator() {
        return costAccumulator;
    }

    public ScalarOperation<C> getScalarOperation() {
        return scalarOperation;
    }

    public void setScaleFactor(double scaleFactor) {
        this.scaleFactor = scaleFactor;
    }
}
