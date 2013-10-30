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

import es.usc.citius.lab.hipster.node.NodeFactory;
import es.usc.citius.lab.hipster.node.Transition;

/**
 * The ADStarNodeBuilder is used for instantiate new {@link es.usc.citius.lab.hipster.node.adstar.ADStarNode}.
 *
 * @param <S> class defining the state
 * @param <T> class defining the cost
 * 
 * @author Adrián González Sieira <<a href="mailto:adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 * @since 0.1.0
 */
public class ADStarNodeBuilder<S, T extends Comparable<T>> implements NodeFactory<S, ADStarNode<S, T>> {

    protected T max;
    protected T min;

    /**
     * Constructor for the ADStarNodeBuilder. Takes the minimum and 
     * maximum cost values managed by the algorithm to initialize the 
     * node values properly.
     * 
     * @param min minimum node cost
     * @param max maximum node cost
     */
    public ADStarNodeBuilder(T min, T max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Builds a new node taking the parent and the transition used to access the new node.
     */
    public ADStarNode<S, T> node(ADStarNode<S, T> from, Transition<S> transition) {
        if (from == null) {
            return new ADStarNode<S, T>(transition, null, min, max, new ADStarNode.Key<T>(min, min));
        } else {
            return new ADStarNode<S, T>(transition, null, max, max, new ADStarNode.Key<T>(max, max));
        }
    }
}
