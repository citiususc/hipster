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

package es.usc.citius.lab.hipster.node.adstar;

import es.usc.citius.lab.hipster.function.CostFunction;
import es.usc.citius.lab.hipster.function.HeuristicFunction;
import es.usc.citius.lab.hipster.function.impl.BinaryOperation;
import es.usc.citius.lab.hipster.function.ScalarFunction;
import es.usc.citius.lab.hipster.node.Transition;
import es.usc.citius.lab.hipster.node.adstar.ADStarNode.Key;

import java.util.Map;
import java.util.Map.Entry;

/**
 * The ADStarNodeUpdater is used by the {@link es.usc.citius.lab.hipster.algorithm.ADStar}
 * algorithm to update the G and V values of the {@link es.usc.citius.lab.hipster.node.adstar.ADStarNode} 
 * explored by the algorithm. Different operations are executed depending on its consistent or inconsistent state:
 * <ul>
 * 		<li>
 * 			For nodes in consistent state, if the cost of the parent added to the cost of the transition
 * 			improves the current value of G, the path changes to include the new transition and the {@link Key}
 * 			is updated taking into account the new cost.
 * 		</li>
 * 		<li>
 * 			For nodes in inconsistent state, all their predecessors are explored to select the one with minimum
 * 			G and transition cost. The path changes according to the new minimum cost and the {@link Key} is updated.
 * 		</li>
 * <ul>
 * 
 * In both cases the updater returns true if the {@link es.usc.citius.lab.hipster.node.adstar.ADStarNode.Key} values 
 * change to reinsert it in the Open queue with a new priority.
 *
 * @param <S> class defining the state
 * @param <T> class defining the cost 
 * 
 * @author Adrián González Sieira <<a href="mailto:adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 * @since 0.1.0
 */
public class ADStarNodeUpdater<S, T extends Comparable<T>> {

    private final CostFunction<S, T> costFunction;
    private final HeuristicFunction<S, T> heuristicFunction;
    private final BinaryOperation<T> add;
    private final ScalarFunction<T> scale;
    private double epsilon;

    /**
     * The constructor for ADStarNodeUpdater takes the elements that involve the cost
     * definition to update the G and V values of the {@link es.usc.citius.lab.hipster.node.adstar.ADStarNode} 
     * as the {@link es.usc.citius.lab.hipster.algorithm.ADStar} algorithm is being executed.
     * 
     * @param costFunction function to evaluate instances of {@link es.usc.citius.lab.hipster.node.Transition}
     * @param heuristicFunction function to estimate the cost of to the goal
     * @param add operation to accumulate the cost
     * @param scale operation to scale the cost by a factor
     * @param epsilon inflation factor of the {@link es.usc.citius.lab.hipster.algorithm.ADStar} algorithm
     */
    public ADStarNodeUpdater(CostFunction<S, T> costFunction,
                             HeuristicFunction<S, T> heuristicFunction, BinaryOperation<T> add,
                             ScalarFunction<T> scale, double epsilon) {
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
     * @param node {@link es.usc.citius.lab.hipster.algorithm.ADStar} node to update, in consistent state
     * @param parent previous {@link es.usc.citius.lab.hipster.algorithm.ADStar} of the node
     * @param transition {@link es.usc.citius.lab.hipster.node.Transition} between the parent and the node
     * @return true if the node has changed its {@link es.usc.citius.lab.hipster.node.adstar.ADStarNode.Key}
     */
    public boolean updateConsistent(ADStarNode<S, T> node,
                                    ADStarNode<S, T> parent, Transition<S> transition) {
        T accumulatedCost = this.add.apply(parent.getG(),
                this.costFunction.evaluate(transition)); // parent.getG().add(this.costFunction.evaluate(transition));
        if (node.g.compareTo(accumulatedCost) > 0) {
            node.setPreviousNode(parent);
            // node.previousNode = parent;
            node.g = accumulatedCost;
            node.setState(transition);
            // node.state = transition;
            node.key = new ADStarNode.Key<T>(node.g, node.v,
                    this.heuristicFunction.estimate(transition.to()),
                    this.epsilon, this.add, this.scale);
            return true;
        }
        return false;
    }

    /**
     * Updates a node in inconsistent state (V <= G), evaluating all the predecessors of the current node
     * and updating the parent to the node which combination of cost and transition is minimal.
     * 
     * @param node inconsistent {@link es.usc.citius.lab.hipster.algorithm.ADStar} node to update
     * @param predecessorMap map containing the the predecessor nodes and 
     * @return true if the node has changed its {@link es.usc.citius.lab.hipster.node.adstar.ADStarNode.Key}
     */
    public boolean updateInconsistent(ADStarNode<S, T> node,
                                      Map<Transition<S>, ADStarNode<S, T>> predecessorMap) {
        T minValue = this.add.getIdentityElem();
        ADStarNode<S, T> minParent = null;
        Transition<S> minTransition = null;
        for (Entry<Transition<S>, ADStarNode<S, T>> current : predecessorMap
                .entrySet()) {
            T value = this.add.apply(current.getValue().v, this.costFunction.evaluate(current.getKey()));
            //T value = current.getValue().v.add(this.costFunction.evaluate(current.getKey()));
            if (value.compareTo(minValue) < 0) {
                minValue = value;
                minParent = current.getValue();
                minTransition = current.getKey();
            }
        }
        node.setPreviousNode(minParent);
        // node.previousNode = minParent;
        node.g = minValue;
        node.setState(minTransition);
        // node.state = minTransition;
        node.key = new ADStarNode.Key<T>(node.g, node.v,
                this.heuristicFunction.estimate(minTransition.to()),
                this.epsilon, this.add, this.scale);
        return true;
    }

    /**
     * Assigns the maximum value to V in the current node.
     * 
     * @param node {@link es.usc.citius.lab.hipster.node.adstar.ADStarNode} to modify the value of V
     */
    public void setMaxV(ADStarNode<S, T> node) {
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
