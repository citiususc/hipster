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


import es.usc.citius.hipster.algorithm.localsearch.HillClimbing;
import es.usc.citius.hipster.model.Node;
import es.usc.citius.hipster.model.Transition;
import es.usc.citius.hipster.model.function.*;
import es.usc.citius.hipster.model.function.impl.BinaryOperation;
import es.usc.citius.hipster.model.function.impl.WeightedNodeFactory;
import es.usc.citius.hipster.model.function.impl.LazyNodeExpander;
import es.usc.citius.hipster.model.impl.WeightedNode;
import es.usc.citius.hipster.model.impl.UnweightedNode;
import es.usc.citius.hipster.model.problem.HeuristicSearchProblem;
import es.usc.citius.hipster.model.problem.InformedSearchProblem;
import es.usc.citius.hipster.model.problem.SearchProblem;

public final class Hipster {

    //TODO; Refactor - Remove redundant code

    public static <A,S,C extends Comparable<C>> AStar<A,S,C,WeightedNode<A,S,C>> createAStar(SearchComponents<A,S,WeightedNode<A,S,C>> components){
        AStar<A, S, C, WeightedNode<A, S, C>> algorithm = new AStar<A, S, C, WeightedNode<A,S,C>>(components.initialNode, components.expander);
        return algorithm;
    }


    public static <A,S,C extends Comparable<C>> BellmanFord<A,S,C,WeightedNode<A,S,C>> createBellmanFord(SearchComponents<A,S,WeightedNode<A,S,C>> components){
        BellmanFord<A, S, C, WeightedNode<A,S,C>> algorithm = new BellmanFord<A, S, C, WeightedNode<A,S,C>>(components.initialNode, components.expander);
        return algorithm;
    }

    public static <A,S> BreadthFirstSearch<A,S,UnweightedNode<A,S>> createBreadthFirstSearch(SearchComponents<A,S,UnweightedNode<A,S>> components){
        BreadthFirstSearch<A, S, UnweightedNode<A, S>> algorithm = new BreadthFirstSearch<A, S, UnweightedNode<A, S>>(components.initialNode, components.expander);
        return algorithm;
    }

    public static <A,S> DepthFirstSearch<A,S,UnweightedNode<A,S>> createDepthFirstSearch(SearchComponents<A,S,UnweightedNode<A,S>> components){
        DepthFirstSearch<A, S, UnweightedNode<A, S>> algorithm = new DepthFirstSearch<A, S, UnweightedNode<A, S>>(components.initialNode, components.expander);
        return algorithm;
    }

    public static <A,S,C extends Comparable<C>> IDAStar<A,S,C,WeightedNode<A,S,C>> createIDAStar(SearchComponents<A,S,WeightedNode<A,S,C>> components){
        IDAStar<A, S, C, WeightedNode<A, S, C>> algorithm = new IDAStar<A, S, C, WeightedNode<A, S, C>>(components.initialNode, components.expander);
        return algorithm;
    }

    public static <A,S,C extends Comparable<C>> HillClimbing<A,S,C,WeightedNode<A,S,C>> createHillClimbing(SearchComponents<A,S,WeightedNode<A,S,C>> components, boolean enforced){
        HillClimbing<A,S,C, WeightedNode<A,S,C>> algorithm = new HillClimbing<A,S,C,WeightedNode<A,S,C>>(components.initialNode, components.expander, enforced);
        return algorithm;
    }

    public static <A,S,C extends Comparable<C>> MultiobjectiveLS<A,S,C,WeightedNode<A,S,C>> createMultiobjectiveLS(SearchComponents<A,S,WeightedNode<A,S,C>> components){
        MultiobjectiveLS<A,S,C, WeightedNode<A,S,C>> algorithm = new MultiobjectiveLS<A, S, C, WeightedNode<A, S, C>>(components.initialNode, components.expander);
        return algorithm;
    }

    public static class SearchComponents<A,S,N extends Node<A,S,N>> {
        private N initialNode;
        private NodeExpander<A,S,N> expander;

        public SearchComponents(N initialNode, NodeExpander<A, S, N> expander) {
            this.initialNode = initialNode;
            this.expander = expander;
        }
    }


}
