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

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;
import es.usc.citius.hipster.model.Transition;
import es.usc.citius.hipster.model.function.CostFunction;
import es.usc.citius.hipster.model.function.HeuristicFunction;
import es.usc.citius.hipster.model.function.TransitionFunction;
import es.usc.citius.hipster.model.problem.HeuristicSearchProblem;
import org.apache.commons.collections15.Transformer;


/**
 * @author Pablo Rodríguez Mier
 */
public class JUNGSearchProblem<V, E> implements HeuristicSearchProblem<E, V, Double> {
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
    public TransitionFunction<E,V> getTransitionFunction() {
        return new TransitionFunction<E,V>() {
            @Override
            public Iterable<Transition<E, V>> transitionsFrom(final V state) {
                return Collections2.transform(graph.getOutEdges(state), new Function<E, Transition<E, V>>() {
                    @Override
                    public Transition<E, V> apply(E edge) {
                        Pair<V> endpoints = graph.getEndpoints(edge);
                        V successor = endpoints.getFirst().equals(state) ? endpoints.getSecond() : endpoints.getFirst();
                        return Transition.create(state, edge, successor);
                    }
                });
            }

        };
    }

    @Override
    public CostFunction<E, V, Double> getCostFunction() {
        return new CostFunction<E, V, Double>() {
            @Override
            public Double evaluate(Transition<E, V> transition) {
                return transformer.transform(transition.getAction()).doubleValue();
            }
        };
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
