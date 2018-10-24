/**
 * Copyright (C) 2013-2018 Centro de Investigación en Tecnoloxías da Información (CITIUS) (http://citius.usc.es)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package es.usc.citius.hipster.model.node.impl;


import es.usc.citius.hipster.model.node.AbstractNode;
import es.usc.citius.hipster.model.node.HeuristicNode;

/**
 * Basic implementation of a node which does not which keeps information about
 * the cost. For problems which does not use actions, instances of
 * {@code new WeightedNode<Void,S,C>} may be used.
 *
 * @param <A> type of the actions
 * @param <S> type of the states
 * @param <C> type of the cost
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 * @author Adrián González Sieira <<a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 */
public class WeightedNode<A,S,C extends Comparable<C>>
        extends AbstractNode<A,S,WeightedNode<A,S,C>>
        implements HeuristicNode<A,S,C, WeightedNode<A,S,C>> {

    protected C cost;
    protected C estimation;
    protected C score;

    /**
     * Basic constructor for instantiating a new weighted node.
     *
     * @param previousNode parent node
     * @param state state of the node to be created
     * @param action action connecting the parent node and the current one
     * @param cost cost of the new node
     * @param estimation estimated cost between the current node and the goal
     * @param score score of the new node
     */
    public WeightedNode(WeightedNode<A, S, C> previousNode, S state, A action, C cost, C estimation, C score) {
        super(previousNode, state, action);
        this.cost = cost;
        this.estimation = estimation;
        this.score = score;
    }

    @Override
    public C getScore() {
        return score;
    }

    @Override
    public C getEstimation() {
        return estimation;
    }

    @Override
    public C getCost() {
        return cost;
    }

    @Override
    public int compareTo(WeightedNode<A, S, C> o) {
        return score.compareTo(o.score);
    }

    @Override
    public String toString() {
        return "WeightedNode{" +
                "state=" + this.state() +
                ", cost=" + cost +
                ", estimation=" + estimation +
                ", score=" + score +
                '}';
    }
}
