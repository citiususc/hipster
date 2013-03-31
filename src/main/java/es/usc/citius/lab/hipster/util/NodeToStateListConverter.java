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

package es.usc.citius.lab.hipster.util;

import es.usc.citius.lab.hipster.node.Node;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * From a list of {@link Node} elements obtain the associated states.
 *
 * @author Adrián González Sieira
 * @since 26-03-2013
 * @version 1.0
 */
public class NodeToStateListConverter<S> {

    /**
     * Obtains a List of states from the list of nodes that forms a path.
     * @param nodeList list of {@link Node}
     * @return list of states
     */
    public List<S> convert(List<Node<S>> nodeList) {
        List<S> stateList = new ArrayList<S>(nodeList.size());
        for (Iterator<Node<S>> it = nodeList.iterator(); it.hasNext();) {
            Node<S> current = it.next();
            stateList.add(current.transition().to());
        }
        return stateList;
    }
}
