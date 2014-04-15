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

package es.usc.citius.hipster.model.impl;


import es.usc.citius.hipster.model.AbstractNode;
import es.usc.citius.hipster.model.HeuristicNode;

public class WeightedNode<A,S,C extends Comparable<C>>
        extends AbstractNode<A,S,WeightedNode<A,S,C>>
        implements HeuristicNode<A,S,C, WeightedNode<A,S,C>> {


    private C cost;
    private C estimation;
    private C score;

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
