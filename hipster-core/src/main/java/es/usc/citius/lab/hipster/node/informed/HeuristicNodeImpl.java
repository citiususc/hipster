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
package es.usc.citius.lab.hipster.node.informed;


import es.usc.citius.lab.hipster.node.AbstractNode;
import es.usc.citius.lab.hipster.node.Transition;

/**
 * @param <S>
 * @param <T>
 * @author Pablo Rodríguez Mier
 */
public class HeuristicNodeImpl<S, T extends Comparable<T>> extends AbstractNode<S> implements HeuristicNode<S, T>, Comparable<HeuristicNode<S, T>> {

    private T cost;
    private T score;

    public HeuristicNodeImpl(Transition<S> transition, HeuristicNode<S, T> previousNode, T cost, T score) {
        super(transition, previousNode);
        this.cost = cost;
        this.score = score;
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

    public int compareTo(HeuristicNode<S, T> o) {
        return this.score.compareTo(o.getScore());
    }

}
