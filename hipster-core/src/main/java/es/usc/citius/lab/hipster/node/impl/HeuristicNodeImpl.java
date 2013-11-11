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
package es.usc.citius.lab.hipster.node.impl;


import es.usc.citius.lab.hipster.node.AbstractNode;
import es.usc.citius.lab.hipster.node.HeuristicNode;
import es.usc.citius.lab.hipster.node.Transition;

/**
 * Implementation of a generic HeuristicNode. A Heuristic Node contains a transition, which is the
 * information about the movement used to reach the new state, the previous node, used to connect
 * the nodes for retrieving the full path, the cost, which is used to compare the node with other ones,
 * and the score, which is used to heuristically compare nodes.
 *
 * @param <S> type of the state.
 * @param <T> type of the cost and the score.
 * @author Pablo Rodríguez Mier
 */
public class HeuristicNodeImpl<S, T extends Comparable<T>> extends AbstractNode<S> implements HeuristicNode<S, T>, Comparable<HeuristicNode<S, T>> {

    private T cost;
    private T score;
    private T estimation;

    public HeuristicNodeImpl(Transition<S> transition, HeuristicNode<S, T> previousNode, T cost, T estimation, T score) {
        super(transition, previousNode);
        this.cost = cost;
        this.score = score;
        this.estimation = estimation;
    }

    public HeuristicNodeImpl(Transition<S> transition, HeuristicNode<S, T> previousNode, T cost, T estimation, T score, boolean track) {
        super(transition);
        if (track){
            this.previousNode = previousNode;
        }
        this.cost = cost;
        this.score = score;
        this.estimation = estimation;
    }

    public HeuristicNodeImpl(Transition<S> transition, HeuristicNode<S, T> previousNode, T cost) {
        super(transition, previousNode);
        this.cost = cost;
        this.score = cost;
    }

    public T getCost() {
        return this.cost;
    }

    public T getScore() {
        return this.score;
    }

    @Override
    public T getEstimation() {
        return this.estimation;
    }

    public int compareTo(HeuristicNode<S, T> o) {
        return this.score.compareTo(o.getScore());
    }

    @Override
    public String toString() {
        return this.getState().toString();
    }
}
