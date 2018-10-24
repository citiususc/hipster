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
package es.usc.citius.hipster.model.node;


import java.util.LinkedList;
import java.util.List;

/**
 * Basic implementation of the interface {@link Node}. All implementations of
 * the interface may extend this class to reuse the implemented {@link #path()} method and
 * the getters.
 *
 * @param <A> type of the actions
 * @param <S> type of the state
 * @param <N> type of the node
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 * @author Adrián González Sieira <<a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 */
public class AbstractNode<A,S,N extends AbstractNode<A,S,N>> implements Node<A,S,N> {
    protected N previousNode;
    protected S state;
    protected A action;
    protected int pathSize;

    /**
     * Generic constructor of nodes.
     *
     * @param previousNode parent node
     * @param state current state
     * @param action action between the previous node and the current state
     */
    public AbstractNode(N previousNode, S state, A action) {
        this.previousNode = previousNode;
        this.state = state;
        this.action = action;
        this.pathSize =  (previousNode != null) ? previousNode.pathSize + 1 : 1;
    }

    @Override
    public List<N> path() {
        LinkedList<N> path = new LinkedList<N>();
        N currentNode = (N) this;
        while(currentNode != null){
            path.addFirst(currentNode);
            currentNode = currentNode.previousNode;
        }
        return path;
    }

    @Override
    public int pathSize() {
        return pathSize;
    }

    @Override
    public N previousNode() {
        return this.previousNode;
    }

    @Override
    public S state() {
        return state;
    }

    @Override
    public A action() {
        return action;
    }

    @Override
    public String toString() {
        return "Node{" +
                "action=" + action +
                ", state=" + this.state() +
                '}';
    }
}
