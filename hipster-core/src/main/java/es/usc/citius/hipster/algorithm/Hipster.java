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
import es.usc.citius.hipster.model.CostNode;
import es.usc.citius.hipster.model.HeuristicNode;
import es.usc.citius.hipster.model.Node;
import es.usc.citius.hipster.model.problem.SearchProblem;

public final class Hipster {

    private Hipster(){

    }

    public static <A,S,C extends Comparable<C>, N extends HeuristicNode<A,S,C,N>> AStar<A,S,C,N> createAStar(SearchProblem<A,S,N> components){
        return new AStar<A, S, C, N>(components.getInitialNode(), components.getExpander());
    }

    public static <A,S,C extends Comparable<C>, N extends HeuristicNode<A,S,C,N>> AStar<A,S,C,N> createDijkstra(SearchProblem<A,S,N> components){
        //TODO: There is no difference with AStar. Actually if the NodeExpander uses heuristics, this "Dijkstra" impl works as the AStar. This should be changed!
        return new AStar<A, S, C, N>(components.getInitialNode(), components.getExpander());
    }

    public static <A,S,C extends Comparable<C>, N extends CostNode<A,S,C,N>> BellmanFord<A,S,C,N> createBellmanFord(SearchProblem<A,S,N> components){
        return new BellmanFord<A, S, C, N>(components.getInitialNode(), components.getExpander());
    }

    public static <A,S,N extends Node<A,S,N>> BreadthFirstSearch<A,S,N> createBreadthFirstSearch(SearchProblem<A,S,N> components){
        return new BreadthFirstSearch<A,S,N>(components.getInitialNode(), components.getExpander());
    }

    public static <A,S,N extends Node<A,S,N>> DepthFirstSearch<A,S,N> createDepthFirstSearch(SearchProblem<A,S,N> components){
        return new DepthFirstSearch<A, S, N>(components.getInitialNode(), components.getExpander());
    }

    public static <A,S,C extends Comparable<C>, N extends HeuristicNode<A,S,C,N>> IDAStar<A,S,C,N> createIDAStar(SearchProblem<A,S,N> components){
        return new IDAStar<A, S, C, N>(components.getInitialNode(), components.getExpander());
    }

    public static <A,S,C extends Comparable<C>, N extends HeuristicNode<A,S,C,N>> HillClimbing<A,S,C,N> createHillClimbing(SearchProblem<A,S,N> components, boolean enforced){
        return new HillClimbing<A,S,C,N>(components.getInitialNode(), components.getExpander(), enforced);
    }

    public static <A,S,C extends Comparable<C>, N extends HeuristicNode<A,S,C,N>> MultiobjectiveLS<A,S,C,N> createMultiobjectiveLS(SearchProblem<A,S,N> components){
        return new MultiobjectiveLS<A, S, C, N>(components.getInitialNode(), components.getExpander());
    }


}
