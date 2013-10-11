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
package es.usc.citius.lab.hipster.algorithm.factory;

import es.usc.citius.lab.hipster.algorithm.BellmanFord;
import es.usc.citius.lab.hipster.algorithm.problem.SearchProblem;
import es.usc.citius.lab.hipster.node.NodeFactory;
import es.usc.citius.lab.hipster.node.CostNode;
import es.usc.citius.lab.hipster.node.impl.HeuristicNodeImplFactory;

import java.util.Iterator;

public class BellmanFordIteratorFactory<S, T extends Comparable<T>> implements
        AlgorithmIteratorFactory<S, T> {
    private final SearchProblem<S, T> componentFactory;

    public BellmanFordIteratorFactory(
            SearchProblem<S, T> componentFactory) {
        this.componentFactory = componentFactory;
    }

    public Iterator<? extends CostNode<S, T>> create() {
        NodeFactory<S, CostNode<S, T>> factory = new HeuristicNodeImplFactory<S, T>(
                componentFactory.getCostFunction(), componentFactory.getAccumulator())
                .toCostNodeFactory();

        return new BellmanFord<S, T>(
                componentFactory.getInitialState(),
                componentFactory.getTransitionFunction(), factory);

    }

}
