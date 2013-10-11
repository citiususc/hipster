/*
 * Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS), University of Santiago de Compostela (USC).
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

package es.usc.citius.lab.hipster.jung;

import edu.uci.ics.jung.graph.Graph;
import es.usc.citius.lab.hipster.algorithm.problem.HeuristicSearchProblem;
import es.usc.citius.lab.hipster.function.CostFunction;
import es.usc.citius.lab.hipster.function.HeuristicFunction;
import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.function.impl.CostOperator;
import es.usc.citius.lab.hipster.node.Transition;
import org.apache.commons.collections15.Transformer;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Pablo Rodríguez Mier
 */
public class JUNGSearchProblem<V, E> implements HeuristicSearchProblem<V, Double> {
    private V start;
    private V end;
    private Graph<V, E> graph;
    private Transformer<E, Number> transformer;


    public JUNGSearchProblem(Graph<V, E> graph, V start, V end) {
        this.graph = graph;
        this.start = start;
        this.end = end;
        this.transformer = new Transformer<E, Number>() {
            @Override
            public Number transform(E e) {
                if (e instanceof Number) {
                    return ((Number) e).doubleValue();
                }
                return 1.0;
            }
        };
    }

    public JUNGSearchProblem(Graph<V, E> graph, V start, V end, Transformer<E, Number> transformer) {
        this.graph = graph;
        this.start = start;
        this.end = end;
        this.transformer = transformer;
    }

    @Override
    public V getInitialState() {
        return this.start;
    }

    @Override
    public V getGoalState() {
        return this.end;
    }

    @Override
    public TransitionFunction<V> getTransitionFunction() {
        return new TransitionFunction<V>() {
            @Override
            public Iterable<? extends Transition<V>> from(V current) {
                Collection<Transition<V>> transitions = new ArrayList<Transition<V>>();
                for (V successor : graph.getSuccessors(current)) {
                    transitions.add(new Transition<V>(current, successor));
                }
                return transitions;
            }
        };
    }

    @Override
    public CostFunction<V, Double> getCostFunction() {
        // TODO: The best place to compute the cost is when
        // the neighbors are calculated in almost all cases!
        // Transition + NodeFactory can be mixed!
        return new CostFunction<V, Double>() {
            @Override
            public Double evaluate(Transition<V> transition) {
                // Get the distance of the transition
                E edge = graph.findEdge(transition.from(), transition.to());
                return transformer.transform(edge).doubleValue();
            }
        };
    }

    @Override
    public CostOperator<Double> getAccumulator() {
        return CostOperator.doubleAdditionOp();
    }

    @Override
    public HeuristicFunction<V, Double> getHeuristicFunction() {
        return new HeuristicFunction<V, Double>() {
            @Override
            public Double estimate(V state) {
                return 0d;
            }
        };
    }
}
