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

package es.usc.citius.hipster.algorithm.localsearch;




import es.usc.citius.hipster.algorithm.Algorithm;
import es.usc.citius.hipster.model.ActionState;
import es.usc.citius.hipster.model.HeuristicNode;
import es.usc.citius.hipster.model.function.HeuristicFunction;
import es.usc.citius.hipster.model.function.NodeFactory;
import es.usc.citius.hipster.model.function.TransitionFunction;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Pablo Rodríguez Mier
 * @param <A>
 * @param <S>
 * @param <C>
 * @param <N>
 */
public class EnforcedHillClimbing<A,S,C extends Comparable<C>,N extends HeuristicNode<A,S,C,N>> extends Algorithm<A,S,N> {

    private S initialState;
    private TransitionFunction<A,S> transitionFunction;
    private HeuristicFunction<S, C> heuristicFunction;
    private NodeFactory<A,S,N> factory;

    public EnforcedHillClimbing(S initialState, TransitionFunction<A,S> transitionFunction, NodeFactory<A,S,N> factory, HeuristicFunction<S, C> hf){
        this.initialState = initialState;
        this.transitionFunction = transitionFunction;
        this.heuristicFunction = hf;
        this.factory = factory;
    }

    public class EHCIter implements Iterator<N> {
        private Queue<N> queue = new LinkedList<N>();
        private C bestHeuristic = null;

        private EHCIter() {
            N initial = factory.makeNode(null, ActionState.<A, S>create(null, initialState));
            bestHeuristic = heuristicFunction.estimate(initialState);
            queue.add(initial);
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public N next() {
            N current = this.queue.poll();
            // Generate successors
            for(ActionState<A,S> successor : transitionFunction.transitionsFrom(current.state())){
                S currentState = successor.getState();
                // Evaluate
                C heuristic = heuristicFunction.estimate(currentState);
                // If this node has better heuristic, "climb" over the rest of the partial solutions
                if (heuristic.compareTo(bestHeuristic)<0){
                    bestHeuristic = heuristic;
                    this.queue.clear();
                    this.queue.add(factory.makeNode(current, successor));
                    // Skip other successors
                    break;
                }
                this.queue.add(factory.makeNode(current, successor));
            }
            return current;
        }

        @Override
        public void remove() {

        }

        public Queue<N> getQueue() {
            return queue;
        }

        public void setQueue(Queue<N> queue) {
            this.queue = queue;
        }

        public C getBestHeuristic() {
            return bestHeuristic;
        }

        public void setBestHeuristic(C bestHeuristic) {
            this.bestHeuristic = bestHeuristic;
        }
    }


    @Override
    public Iterator<N> iterator() {
        return new EHCIter();
    }
}
