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
import es.usc.citius.lab.hipster.node.NodeFactory;
import es.usc.citius.lab.hipster.node.Transition;

import java.util.Stack;

public class RecursiveIDA<S, T extends Comparable<T>> {

    private final S initialState;
    private TransitionFunction<S> successors;
    private NodeFactory<S, HeuristicNode<S, T>> factory;

    private class Result {
        T limit;
        HeuristicNode<S,T> node;

        private Result(HeuristicNode<S, T> node, T limit) {
            this.node = node;
            this.limit = limit;
        }

    }

    public RecursiveIDA(S initialState, TransitionFunction<S> transitionFunction, NodeFactory<S, HeuristicNode<S, T>> factory) {
        this.initialState = initialState;
        this.successors = transitionFunction;
        this.factory = factory;

    }

    public HeuristicNode<S,T> search(S goal){
        HeuristicNode<S, T> initialNode = this.factory.node(null, new Transition<S>(initialState));
        // Set initial bound
        T fLimit = initialNode.getEstimation();

        do {
            System.out.println("Starting at depth " + fLimit);
            Result r = dfs_contour(initialNode, fLimit, goal);
            fLimit = r.limit;
            if (r.node != null){
                return r.node;
            }
            if (fLimit == null){
                return null;
            }
        } while(true);
    }

    private Result dfs_contour(HeuristicNode<S, T> node, T fLimit, S goal) {
        // Compute cost
        T f = node.getScore();
        if (f.compareTo(fLimit)>0) {
            return new Result(null, f);
        }
        if (node.transition().to().equals(goal)){
            return new Result(node, f);
        }
        T min = null;
        for(Transition<S> successor : successors.from(node.transition().to())){
            // Create successor nodes
            HeuristicNode<S,T> successorNode = factory.node(node, successor);
            // Recursive call
            Result r = dfs_contour(successorNode, fLimit, goal);
            if (r.node != null){
                return r;
            }
            T newF = r.limit;
            // Select the min f score
            if (min == null){
                min = newF;
            } else {
                if (newF.compareTo(min)<0){
                    min = newF;
                }
            }
        }
        return new Result(null, min);
    }
}
