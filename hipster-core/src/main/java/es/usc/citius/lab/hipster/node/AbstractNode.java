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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * AbstractNode is an implementation of {@link es.usc.citius.lab.hipster.node.Node} that
 * adds support for the common operations of search nodes:
 * <ul>
 * 		<li> obtains the path between the beginning state and the current node
 * 		<li> obtains the previous node to the current
 * 		<li> obtains the incoming {@link es.usc.citius.lab.hipster.node.Transition} between
 *			the parent node and the current one
 * </ul>
 *
 * @param <S> class defining the state
 * 
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 * @author Adrián González Sieira <<a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 * @since 0.1.0
 */
public abstract class AbstractNode<S> implements Node<S> {

    protected Transition<S> transition;
    protected Node<S> previousNode;

    /**
     * Default constructor: Requires the previous {@link Node} and the
     * transition done to access this new one.
     *
     * @param transition   incoming transition
     * @param previousNode previous node
     */
    protected AbstractNode(Transition<S> transition, Node<S> previousNode) {
        this.previousNode = previousNode;
        this.transition = transition;
    }

    public List<Node<S>> path() {
        List<Node<S>> path = new ArrayList<Node<S>>();
        Node<S> current = this;
        //reverse iteration until the head is NULL
        while (current != null) {
            path.add(current);
            current = current.previousNode();
        }
        //reverse the nodes to have the correct order
        Collections.reverse(path);
        return path;
    }

    public Node<S> previousNode() {
        return this.previousNode;
    }

    public Transition<S> transition() {
        return this.transition;
    }

    /**
     * Same as {@link AbstractNode#transition()}.
     * 
     * @return incoming {@link es.usc.citius.lab.hipster.node.Transition} between the parent node and the current one
     * @deprecated will be removed in the next version
     */
    public Transition<S> getState() {
    	//TODO: remove in next version
        return transition;
    }

    /**
     * Updates the incoming transition to the current node.
     * 
     * @param state new incoming transition to the node
     */
    public void setState(Transition<S> state) {
    	//TODO: rename as setTransition()
        this.transition = state;
    }

    /**
     * Updates the previous node to the current.
     * 
     * @param previousNode new previous node
     */
    public void setPreviousNode(Node<S> previousNode) {
        this.previousNode = previousNode;
    }

    @Override
    public String toString() {
        return this.transition().to().toString();
    }

    /**
     * From a list of {@link Node} elements obtain the associated states.
     *
     * @author Adrián González Sieira
     * @version 1.0
     * @since 26-03-2013
     */
    public static <S> List<S> statesFrom(List<Node<S>> nodeList) {
    	//TODO: remove this method from AbstractNode and place it in a separate helper
        List<S> stateList = new ArrayList<S>(nodeList.size());
        for (Node<S> node : nodeList) {
            stateList.add(node.transition().to());
        }
        return stateList;
    }
}
