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
package es.usc.citius.lab.hipster.testutils;

import es.usc.citius.lab.hipster.algorithm.AStar;
import es.usc.citius.lab.hipster.node.NodeFactory;
import es.usc.citius.lab.hipster.node.informed.CostNode;
import es.usc.citius.lab.hipster.node.informed.HeuristicNode;
import es.usc.citius.lab.hipster.node.informed.HeuristicNodeImplFactory;

import java.util.Iterator;

public class AStarIteratorFactory<S, T extends Comparable<T>> implements
        AlgorithmIteratorFactory<S, T> {
    private final HeuristicSearchProblem<S, T> f;

    public AStarIteratorFactory(HeuristicSearchProblem<S, T> componentFactory) {
        this.f = componentFactory;
    }

    public Iterator<? extends CostNode<S, T>> create() {
        NodeFactory<S, HeuristicNode<S, T>> factory = new HeuristicNodeImplFactory<S, T>(
                f.getCostFunction(), f.getHeuristicFunction(), f.getAccumulator());

        AStar<S, T> astar = new AStar<S, T>(f.getInitialState(), f.getTransitionFunction(),
                factory);
        // Create custom queue
        //PriorityFibonacciQueue<HeuristicNode<S,T>> queue = new PriorityFibonacciQueue<HeuristicNode<S, T>>(new HeuristicNodePriorityEvaluator<S,T>());
        //queue.offer(astar.getQueue().poll());
        //astar.setQueue(queue);
        return astar;
    }

}
