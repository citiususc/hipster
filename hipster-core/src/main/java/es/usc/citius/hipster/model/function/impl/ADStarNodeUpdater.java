/*
* Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS).
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package es.usc.citius.hipster.model.function.impl;

import es.usc.citius.hipster.model.Transition;
import es.usc.citius.hipster.model.function.CostFunction;
import es.usc.citius.hipster.model.function.HeuristicFunction;
import es.usc.citius.hipster.model.function.ScalarFunction;
import es.usc.citius.hipster.model.impl.ADStarNode;

import java.util.Map;

/**
 * The ADStarNodeUpdater is used by the {@link es.usc.citius.hipster.algorithm.ADStarForward}
 * algorithm to update the G and V values of the {@link es.usc.citius.hipster.model.impl.ADStarNode}
 * explored by the algorithm. Different operations are executed depending on its consistent or inconsistent state:
 * <ul>
 * <li>
 * For nodes in consistent state, if the cost of the parent added to the cost of the transition
 * improves the current value of G, the path changes to include the new transition and the {@link es.usc.citius.hipster.model.impl.ADStarNode.Key}
 * is updated taking into account the new cost.
 * </li>
 * <li>
 * For nodes in inconsistent state, all their predecessors are explored to select the one with minimum
 * G and transition cost. The path changes according to the new minimum cost and the {@link es.usc.citius.hipster.model.impl.ADStarNode.Key} is updated.
 * </li>
 * <ul>
 *
 * In both cases the updater returns true if the {@link es.usc.citius.hipster.model.impl.ADStarNode.Key} values
 * change to reinsert it in the Open queue with a new priority.
 *
 * @param <A> class defining the action
 * @param <S> class defining the state
 * @param <C> class defining the cost, must implement {@link java.lang.Comparable}
 *
 * @author Adrián González Sieira <<a href="mailto:adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 */
public class ADStarNodeUpdater<A, S, C extends Comparable<C>> {

    private final CostFunction<A, S, C> costFunction;
    private final HeuristicFunction<S, C> heuristicFunction;
    private final BinaryOperation<C> add;
    private final ScalarFunction<C> scale;
    private double epsilon;

    /**
     * The constructor for ADStarNodeUpdater takes the elements that involve the cost
     * definition to update the G and V values of the {@link es.usc.citius.hipster.model.impl.ADStarNode}
     * as the {@link es.usc.citius.hipster.algorithm.ADStarForward} algorithm is being executed.
     *
     * @param costFunction function to evaluate instances of {@link es.usc.citius.hipster.model.Transition}
     * @param heuristicFunction function to estimate the cost of to the goal
     * @param add operation to accumulate the cost
     * @param scale operation to scale the cost by a factor
     * @param epsilon inflation factor of the {@link es.usc.citius.hipster.algorithm.ADStarForward} algorithm
     */
    public ADStarNodeUpdater(CostFunction<A, S, C> costFunction,
                             HeuristicFunction<S, C> heuristicFunction, BinaryOperation<C> add,
                             ScalarFunction<C> scale, double epsilon) {
        this.costFunction = costFunction;
        this.heuristicFunction = heuristicFunction;
        this.epsilon = epsilon;
        this.add = add;
        this.scale = scale;
    }

    /**
     * Updates a node in consistent state (V > G) updating the path
     * and the cost if the parent node and the transition improves the current cost of the node.
     *
     * @param node {@link es.usc.citius.hipster.algorithm.ADStarForward} node to update, in consistent state
     * @param parent previous {@link es.usc.citius.hipster.algorithm.ADStarForward} of the node
     * @param transition {@link es.usc.citius.hipster.model.Transition} between the parent and the node
     * @return true if the node has changed its {@link es.usc.citius.hipster.model.impl.ADStarNode.Key}
     */
    public boolean updateConsistent(ADStarNode<A, S, C> node,
                                    ADStarNode<A, S, C> parent, Transition<A, S> transition) {
        // parent.getG().add(this.costFunction.evaluate(transition));
        C accumulatedCost = add.apply(parent.getG(), costFunction.evaluate(transition));
        if (node.getG().compareTo(accumulatedCost) > 0) {
            node.setPreviousNode(parent);
            // node.previousNode = parent;
            node.setG(accumulatedCost);
            node.setState(transition.getState());
            node.setAction(transition.getAction());
            // node.state = transition;
            node.setKey(new ADStarNode.Key<C>(node.getG(), node.getV(),
                    heuristicFunction.estimate(transition.getState()), epsilon, add, scale));
            return true;
        }
        return false;
    }

    /**
     * Updates a node in inconsistent state (V <= G), evaluating all the predecessors of the current node
     * and updating the parent to the node which combination of cost and transition is minimal.
     *
     * @param node inconsistent {@link es.usc.citius.hipster.algorithm.ADStarForward} node to update
     * @param predecessorMap map containing the the predecessor nodes and
     * @return true if the node has changed its {@link es.usc.citius.hipster.model.impl.ADStarNode.Key}
     */
    public boolean updateInconsistent(ADStarNode<A, S, C> node,
                                      Map<Transition<A, S>, ADStarNode<A, S, C>> predecessorMap) {
        C minValue = add.getIdentityElem();
        ADStarNode<A, S, C> minParent = null;
        Transition<A, S> minTransition = null;
        for (Map.Entry<Transition<A, S>, ADStarNode<A, S, C>> current : predecessorMap
                .entrySet()) {
            C value = add.apply(current.getValue().getV(), costFunction.evaluate(current.getKey()));
            //T value = current.getValue().v.add(this.costFunction.evaluate(current.getKey()));
            if (value.compareTo(minValue) < 0) {
                minValue = value;
                minParent = current.getValue();
                minTransition = current.getKey();
            }
        }
        node.setPreviousNode(minParent);
        // node.previousNode = minParent;
        node.setG(minValue);
        node.setState(minTransition.getState());
        node.setAction(minTransition.getAction());
        // node.state = minTransition;
        node.setKey(
                new ADStarNode.Key<C>(node.getG(), node.getV(),
                heuristicFunction.estimate(minTransition.getState()), epsilon, add, scale)
        );
        return true;
    }

    /**
     * Assigns the maximum value to V in the current node.
     *
     * @param node {@link es.usc.citius.hipster.model.impl.ADStarNode} to modify the value of V
     */
    public void setMaxV(ADStarNode<A, S, C> node) {
        node.setV(this.add.getMaxElem());
    }

    /**
     * Assign a value to the inflation parameter of the heuristic.
     *
     * @param epsilon new value
     */
    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }
}
