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

import es.usc.citius.lab.hipster.function.CostFunction;
import es.usc.citius.lab.hipster.function.HeuristicFunction;
import es.usc.citius.lab.hipster.function.impl.BinaryOperation;
import es.usc.citius.lab.hipster.node.CostNode;
import es.usc.citius.lab.hipster.node.HeuristicNode;
import es.usc.citius.lab.hipster.node.NodeFactory;
import es.usc.citius.lab.hipster.node.Transition;

public class HeuristicNodeImplFactory<S, T extends Comparable<T>> implements NodeFactory<S, HeuristicNode<S, T>> {

    private CostFunction<S, T> gf;
    private HeuristicFunction<S, T> hf;
    private BinaryOperation<T> accumulator;


    public HeuristicNodeImplFactory(CostFunction<S, T> costFunction, HeuristicFunction<S, T> heuristicFunction, BinaryOperation<T> accumulator) {
        this.gf = costFunction;
        this.hf = heuristicFunction;
        this.accumulator = accumulator;
    }

    public HeuristicNodeImplFactory(CostFunction<S, T> costFunction, BinaryOperation<T> accumulator) {
        this.gf = costFunction;
        this.hf = new HeuristicFunction<S, T>() {
            public T estimate(S state) {
                return HeuristicNodeImplFactory.this.accumulator.getIdentityElem();
            }
        };
        this.accumulator = accumulator;
    }

    public HeuristicNode<S, T> node(HeuristicNode<S, T> from,
                                    Transition<S> transition) {
        T cost, estimatedDistance;

        if (from == null) {
            cost = estimatedDistance = accumulator.getIdentityElem();
        } else {
            cost = accumulator.apply(from.getCost(), this.gf.evaluate(transition));
            estimatedDistance = this.hf.estimate(transition.to());
        }
        return new HeuristicNodeImpl<S, T>(transition, from, cost, accumulator.apply(cost, estimatedDistance));
    }

    public NodeFactory<S, CostNode<S, T>> toCostNodeFactory() {
        final NodeFactory<S, HeuristicNode<S, T>> factory = this;
        return new NodeFactory<S, CostNode<S, T>>() {
            public CostNode<S, T> node(CostNode<S, T> from,
                                       Transition<S> transition) {
                return factory.node((HeuristicNode<S, T>) from, transition);
            }
        };
    }

}
