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

package es.usc.citius.lab.hipster.search.local;


import es.usc.citius.lab.hipster.function.HeuristicFunction;
import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.node.Node;
import es.usc.citius.lab.hipster.node.NodeFactory;
import es.usc.citius.lab.hipster.node.Transition;
import es.usc.citius.lab.hipster.node.impl.SimpleNodeFactory;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class EnforcedHillClimbing<S, T extends Comparable<T>> implements Iterator<Node<S>> {

    private Queue<Node<S>> queue = new LinkedList<Node<S>>();
    private T bestHeuristic = null;
    private S initialState;
    private TransitionFunction<S> transitionFunction;
    private HeuristicFunction<S, T> heuristicFunction;
    private NodeFactory<S, Node<S>> factory;

    public EnforcedHillClimbing(S initialState, TransitionFunction<S> transitionFunction, NodeFactory<S, Node<S>> factory, HeuristicFunction<S, T> hf){
        Node<S> initial = factory.node(null, new Transition<S>(initialState));
        this.initialState = initialState;
        this.transitionFunction = transitionFunction;
        this.heuristicFunction = hf;
        this.factory = factory;
        this.bestHeuristic = hf.estimate(initialState);
        this.queue.add(initial);
    }

    public EnforcedHillClimbing(S initialState, TransitionFunction<S> transitionFunction, HeuristicFunction<S, T> hf){
        this(initialState, transitionFunction, new SimpleNodeFactory<S>(), hf);
    }

    @Override
    public boolean hasNext() {
        return !queue.isEmpty();
    }

    @Override
    public Node<S> next() {
        Node<S> current = this.queue.poll();
        // Generate successors
        for(Transition<S> successor : this.transitionFunction.from(current.transition().to())){
            S currentState = successor.to();
            // Evaluate
            T heuristic = this.heuristicFunction.estimate(currentState);
            // If this node has better heuristic, "climb" over the rest of the partial solutions
            if (heuristic.compareTo(bestHeuristic)<0){
                bestHeuristic = heuristic;
                this.queue.clear();
                this.queue.add(this.factory.node(current, successor));
                // Skip other successors
                break;
            }
            this.queue.add(this.factory.node(current, successor));
        }
        return current;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    public Queue<Node<S>> getQueue() {
        return queue;
    }

    public void setQueue(Queue<Node<S>> queue) {
        this.queue = queue;
    }

    public T getBestHeuristic() {
        return bestHeuristic;
    }

    public void setBestHeuristic(T bestHeuristic) {
        this.bestHeuristic = bestHeuristic;
    }

    public S getInitialState() {
        return initialState;
    }

    public void setInitialState(S initialState) {
        this.initialState = initialState;
    }

    public TransitionFunction<S> getTransitionFunction() {
        return transitionFunction;
    }

    public void setTransitionFunction(TransitionFunction<S> transitionFunction) {
        this.transitionFunction = transitionFunction;
    }

    public HeuristicFunction<S, T> getHeuristicFunction() {
        return heuristicFunction;
    }

    public void setHeuristicFunction(HeuristicFunction<S, T> heuristicFunction) {
        this.heuristicFunction = heuristicFunction;
    }

    public NodeFactory<S, Node<S>> getFactory() {
        return factory;
    }

    public void setFactory(NodeFactory<S, Node<S>> factory) {
        this.factory = factory;
    }
}
