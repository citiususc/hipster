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

package es.usc.citius.lab.hipster.node;

import es.usc.citius.lab.hipster.function.CostFunction;
import es.usc.citius.lab.hipster.function.TransitionFunction;
import java.util.Iterator;

/**
 * Builder to create instances of {@link ADStarNode} of a inconsistent state.
 *
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @since 01-04-2013
 * @version 1.0
 */
public class InconsistentADStarNodeBuilder<S> implements NodeBuilder<S, ADStarNode<S>>{
    private final TransitionFunction<S> predecessorFunction;
    private final CostFunction<S, Double> costFunction;
    private final NodeBuilder<S, ADStarNode<S>> builder;

    public InconsistentADStarNodeBuilder(TransitionFunction<S> predecessorFunction, CostFunction<S, Double> costFunction, NodeBuilder<S, ADStarNode<S>> defaultBuilder) {
        this.predecessorFunction = predecessorFunction;
        this.costFunction = costFunction;
        this.builder = defaultBuilder;
    }

    public ADStarNode<S> node(ADStarNode<S> from, Transition<S> transition) {
        Double minValue = Double.POSITIVE_INFINITY;
        ADStarNode<S> minPredecessorNode = null;
        for (Iterator<Transition<S>> it = this.predecessorFunction.from(from.transition().to()).iterator(); it.hasNext();) {
            Transition<S> predecessor = it.next();
            /*De un estado obtener su nodo, si existe*/
            ADStarNode<S> predecessorNode = this.builder.node(from, predecessor);
            Double currentValue = predecessorNode.getV() + this.costFunction.evaluate(predecessor);
            if (currentValue < minValue) {
                minValue = currentValue;
                minPredecessorNode = predecessorNode;
            }
        }
        return this.builder.node(minPredecessorNode, transition);
    }

}
