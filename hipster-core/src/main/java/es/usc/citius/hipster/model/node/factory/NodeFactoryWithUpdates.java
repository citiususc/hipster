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
package es.usc.citius.hipster.model.node.factory;

/**
 * Instantiator of nodes with the functionality of updating them as well.
 * New nodes are obtained from the current state, the parent and he transition between them.
 * Existing nodes can be updated within this class.
 *
 * @param <A> action type
 * @param <S> state type
 * @param <N> node type
 *
 * @author Adrián González Sieira <<a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 */
public interface NodeFactoryWithUpdates<A, S, N> extends NodeFactory<A, S, N>{

    /**
     * Allows updating the information of a node without creating a new one.
     *
     * @param node node to be updated
     */
    public void updateNode(N node);

}
