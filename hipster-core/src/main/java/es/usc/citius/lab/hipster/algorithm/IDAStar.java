/*
 * Copyright 2013 CITIUS <http://citius.usc.es>, University of Santiago de Compostela.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package es.usc.citius.lab.hipster.algorithm;


import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.node.HeuristicNode;
import es.usc.citius.lab.hipster.node.Node;
import es.usc.citius.lab.hipster.node.NodeFactory;
import es.usc.citius.lab.hipster.node.Transition;

import java.util.*;

/**
 *
 * @param <S>
 * @param <T>
 */
public class IDAStar<S, T extends Comparable<T>> implements Iterator<HeuristicNode<S, T>> {

    private final S initialState;
    private TransitionFunction<S> successors;
    private Stack<HeuristicNode<S,T>> stack;
    private NodeFactory<S, HeuristicNode<S, T>> factory;

    private T fLimit;

    public IDAStar(S initialState, TransitionFunction<S> transitionFunction, NodeFactory<S, HeuristicNode<S, T>> factory) {
        this.initialState = initialState;
        this.successors = transitionFunction;
        this.factory = factory;
        HeuristicNode<S, T> initialNode = this.factory.node(null, new Transition<S>(initialState));
        // Set initial bound
        fLimit = initialNode.getEstimation();

    }

    private void iterativeDfsContour(){
        // Perform a DFS with cutoff using fLimit bound
        NodeFactory<S, Node<S>> f = new NodeFactory<S, Node<S>>() {
            @Override
            public Node<S> node(Node<S> from, Transition<S> transition) {
                return factory.node((HeuristicNode<S, T>) from, transition);
            }
        };
        DepthFirstSearch<S> dfs = new DepthFirstSearch<S>(this.initialState, this.successors, f);
        while(dfs.hasNext()){
            HeuristicNode<S,T> node = (HeuristicNode<S, T>) dfs.next();

        }

    }

    @Override
    public boolean hasNext() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public HeuristicNode<S, T> next() {
        // Compute n
        return null;
    }

    @Override
    public void remove() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
