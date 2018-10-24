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

import es.usc.citius.hipster.algorithm.ARAStar;
import es.usc.citius.hipster.model.function.CostFunction;
import es.usc.citius.hipster.model.function.HeuristicFunction;
import es.usc.citius.hipster.model.function.impl.BinaryOperation;
import es.usc.citius.hipster.model.function.impl.ScalarOperation;
import es.usc.citius.hipster.model.node.factory.NodeFactoryWithUpdates;

public class ScaleWeightedNodeFactoryWithUpdates<A, S, C extends Comparable<C>> extends ScaleWeightedNodeFactory<A,S,C> implements NodeFactoryWithUpdates<A, S, WeightedNode<A, S, C>>, ARAStar.Factory<A, S, C, WeightedNode<A, S, C>> {

    public ScaleWeightedNodeFactoryWithUpdates(CostFunction<A, S, C> costFunction, HeuristicFunction<S, C> heuristicFunction, double scaleFactor, BinaryOperation<C> costAccumulator, ScalarOperation<C> scalarOperation) {
        super(costFunction, heuristicFunction, scaleFactor, costAccumulator, scalarOperation);
    }

    public ScaleWeightedNodeFactoryWithUpdates(ScaleWeightedNodeFactory<A, S, C> factory){
        this(factory.costFunction, factory.heuristicFunction, factory.scaleFactor, factory.costAccumulator, factory.scalarOperation);
    }

    @Override
    public void updateNode(WeightedNode<A, S, C> node) {

        // Updates the score with the given cost and estimation
        node.score = costAccumulator.apply(node.cost, scalarOperation.scale(node.estimation, scaleFactor));

    }

}
