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
import es.usc.citius.hipster.model.ADStarNode;
import es.usc.citius.hipster.model.function.NodeExpander;
import es.usc.citius.hipster.model.function.impl.ADStarNodeExpander;
import es.usc.citius.hipster.model.function.impl.ADStarNodeFactory;
import es.usc.citius.hipster.model.impl.ADStarNodeImpl;
import es.usc.citius.hipster.model.impl.UnweightedNode;
import es.usc.citius.hipster.model.impl.WeightedNode;
import es.usc.citius.hipster.model.problem.SearchComponents;
import es.usc.citius.hipster.model.problem.SearchProblem;
import es.usc.citius.hipster.util.examples.RomanianProblem;
import es.usc.citius.hipster.util.graph.GraphSearchProblem;

import java.util.Collections;

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

    public static <A,S,C extends Comparable<C>> ADStarForward<A,S,C,ADStarNodeImpl<A,S,C>> createADStar(SearchComponents<A, S, C> components){
        //node factory instantiation
        ADStarNodeFactory<A, S, C> factory = new ADStarNodeFactory<A, S, C>(components);
        //node expander instantiation
        ADStarNodeExpander<A, S, C, ADStarNodeImpl<A, S, C>> expander =
                new ADStarNodeExpander<A, S, C, ADStarNodeImpl<A, S, C>>(components, factory, 1.0);
        //instantiate algorithm
        return new ADStarForward(
                components.getBegin(),
                Collections.singleton(components.getGoal()),
                expander);
    }

}
