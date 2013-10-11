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

package es.usc.citius.lab.hipster.algorithm;

import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.node.Node;
import es.usc.citius.lab.hipster.node.Transition;
import es.usc.citius.lab.hipster.node.impl.SimpleNode;

import java.util.Iterator;
import java.util.Stack;


/**
 * Very basic implementation of the Depth-First-Search (DFS) algorithm using
 * a {@link Stack} as the basic data structure.
 *
 * @param <S> state type.
 */
public class DepthFirstSearch<S> implements Iterator<Node<S>> {

    private Stack<Node<S>> stack = new Stack<Node<S>>();
    private TransitionFunction<S> successors;

    // required: Successors function
    public DepthFirstSearch(final S initialState, TransitionFunction<S> successors) {
        stack.add(new SimpleNode<S>(new Transition<S>(null, initialState), null));
        this.successors = successors;
    }

    public boolean hasNext() {
        return !stack.isEmpty();
    }

    public Node<S> next() {
        Node<S> current = stack.pop();
        for (Transition<S> successor : this.successors.from(current.transition().to())) {
            stack.add(new SimpleNode<S>(successor, current));
        }
        return current;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }


}
