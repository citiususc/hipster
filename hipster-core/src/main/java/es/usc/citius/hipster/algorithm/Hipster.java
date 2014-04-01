/*
 * Copyright 2014 CITIUS <http://citius.usc.es>, University of Santiago de Compostela.
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

package es.usc.citius.hipster.algorithm;


import es.usc.citius.hipster.model.ActionState;
import es.usc.citius.hipster.model.UnweightedNode;
import es.usc.citius.hipster.model.function.NodeFactory;
import es.usc.citius.hipster.model.problem.SearchProblem;

public final class Hipster {

    public static <A,S> BreadthFirstSearch<A,S,UnweightedNode<A,S>> createBreadthFirstSearch(SearchProblem<A,S> problem){
        return new BreadthFirstSearch<A, S, UnweightedNode<A,S>>(problem.getInitialState(), problem.getTransitionFunction(),
                new NodeFactory<A, S, UnweightedNode<A,S>>() {
                    @Override
                    public UnweightedNode<A,S> makeNode(UnweightedNode<A,S> fromNode, ActionState<A, S> actionState) {
                        return new UnweightedNode<A,S>(fromNode, actionState);
                    }
                });
    }
}
