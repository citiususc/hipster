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
import es.usc.citius.hipster.model.impl.ADStarNode;
import es.usc.citius.hipster.model.impl.UnweightedNode;
import es.usc.citius.hipster.model.impl.WeightedNode;
import es.usc.citius.hipster.model.problem.SearchProblem;

public final class Hipster {

    private Hipster(){

    }

    public static <A,S,C extends Comparable<C>> AStar<A,S,C,WeightedNode<A,S,C>> createAStar(SearchProblem<A,S,WeightedNode<A,S,C>> components){
        return new AStar<A, S, C, WeightedNode<A,S,C>>(components.getInitialNode(), components.getExpander());
    }

    public static <A,S,C extends Comparable<C>> AStar<A,S,C,WeightedNode<A,S,C>> createDijkstra(SearchProblem<A,S,WeightedNode<A,S,C>> components){
        //TODO: There is no difference with AStar. Actually if the NodeExpander uses heuristics, this "Dijkstra" impl works as the AStar. This should be changed!
        return new AStar<A, S, C, WeightedNode<A,S,C>>(components.getInitialNode(), components.getExpander());
    }


    public static <A,S,C extends Comparable<C>> BellmanFord<A,S,C,WeightedNode<A,S,C>> createBellmanFord(SearchProblem<A,S,WeightedNode<A,S,C>> components){
        return new BellmanFord<A, S, C, WeightedNode<A,S,C>>(components.getInitialNode(), components.getExpander());
    }

    public static <A,S> BreadthFirstSearch<A,S,UnweightedNode<A,S>> createBreadthFirstSearch(SearchProblem<A,S,UnweightedNode<A,S>> components){
        return new BreadthFirstSearch<A, S, UnweightedNode<A, S>>(components.getInitialNode(), components.getExpander());
    }

    public static <A,S> DepthFirstSearch<A,S,UnweightedNode<A,S>> createDepthFirstSearch(SearchProblem<A,S,UnweightedNode<A,S>> components){
        return new DepthFirstSearch<A, S, UnweightedNode<A, S>>(components.getInitialNode(), components.getExpander());
    }

    public static <A,S,C extends Comparable<C>> IDAStar<A,S,C,WeightedNode<A,S,C>> createIDAStar(SearchProblem<A,S,WeightedNode<A,S,C>> components){
        return new IDAStar<A, S, C, WeightedNode<A, S, C>>(components.getInitialNode(), components.getExpander());
    }

    public static <A,S,C extends Comparable<C>> HillClimbing<A,S,C,WeightedNode<A,S,C>> createHillClimbing(SearchProblem<A,S,WeightedNode<A,S,C>> components, boolean enforced){
        return new HillClimbing<A,S,C,WeightedNode<A,S,C>>(components.getInitialNode(), components.getExpander(), enforced);
    }

    public static <A,S,C extends Comparable<C>> MultiobjectiveLS<A,S,C,WeightedNode<A,S,C>> createMultiobjectiveLS(SearchProblem<A,S,WeightedNode<A,S,C>> components){
        return new MultiobjectiveLS<A, S, C, WeightedNode<A, S, C>>(components.getInitialNode(), components.getExpander());
    }

    public static <A,S,C extends Comparable<C>> ADStarForward<A,S,C> createADStar(SearchProblem<A,S,ADStarNode<A,S,C>> components){
        return null;//return new ADStarForward<A, S, C>(components.getInitialNode(), components.getExpander())
    }


}
