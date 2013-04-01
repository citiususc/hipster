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

import java.util.HashMap;
import java.util.Map;

/**
 * Generates instances of {@link ADStarNode} with the default parameters:
 * <ul>
 * <li>g = Infinity
 * <li/>v = Infinity
 * <li/>previousNode = Infinity
 * </ul>
 * It also caches nodes to recover the instances from states.
 *
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @since 01-04-2013
 * @version 1.0
 */
public class DefaultADStarNodeBuilder<S> implements NodeBuilder<S, ADStarNode<S>> {

    private final Map<S, ADStarNode<S>> cache;

    public DefaultADStarNodeBuilder() {
        this.cache = new HashMap<S, ADStarNode<S>>();
    }

    /**
     * Default node generation for AD*.
     *
     * @param from
     * @param transition
     * @return
     */
    public ADStarNode<S> node(ADStarNode<S> from, Transition<S> transition) {
        ADStarNode<S> node = cache.get(transition.to());
        if (node == null) {
            /*Node instantiation and default parameter generation.*/
            node = new ADStarNode<S>(transition, from);
            node.setG(Double.POSITIVE_INFINITY);
            node.setV(Double.POSITIVE_INFINITY);
            node.previousNode = null;
            /*Cache node to prevent generate duplicates and recover the information later.*/
            cache.put(transition.to(), node);
        }
        return node;
    }
}
