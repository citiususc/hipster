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

/**
 * Builder to create instances of {@link ADStarNode} in a consistent state.
 *
 * @author Adrián González Sieira
 * @since 27-03-2013
 * @version 1.0
 */
public class ConsistentADStarNodeBuilder<S> implements NodeBuilder<S, ADStarNode<S>> {

    private final NodeBuilder<S, ADStarNode<S>> builder;
    private final CostFunction<S, Double> costFunction;

    /**
     * Default constructor for this class: initializes the instances map.
     */
    public ConsistentADStarNodeBuilder(NodeBuilder<S, ADStarNode<S>> defaultBuilder, CostFunction<S, Double> costFunction) {
        this.builder = defaultBuilder;
        this.costFunction = costFunction;
    }

    /**
     * Method to return node instances: if the node is visited before, the
     * previous instance is returned; else, a new one is created.
     *
     * @param from parent node
     * @param transition incoming transition
     * @return instance of {@link ADStarNode}
     */
    public ADStarNode<S> node(ADStarNode<S> from, Transition<S> transition) {
        /**/
        ADStarNode<S> node = this.builder.node(null, transition);
        Double cost = this.costFunction.evaluate(node.transition());
        /*if g(s') > g(s) + c(s, s')*/
        if (node.getG() > from.getG() + cost) {
            /*bp(s') = s*/
            node.setPreviousNode(from);
            /*g(s') = g(bp(s') + c(bp(s'), s')*/
            node.setG(node.previousNode().getG() + cost);
        }
        return node;
    }
}
