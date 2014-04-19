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

package es.usc.citius.hipster.util.graph;


import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.model.Transition;
import es.usc.citius.hipster.model.function.CostFunction;
import es.usc.citius.hipster.model.function.HeuristicFunction;
import es.usc.citius.hipster.model.function.TransitionFunction;
import es.usc.citius.hipster.model.function.impl.BinaryOperation;
import es.usc.citius.hipster.model.impl.UnweightedNode;
import es.usc.citius.hipster.model.impl.WeightedNode;
import es.usc.citius.hipster.model.problem.ProblemBuilder;

/**
 * @author Pablo Rodríguez Mier
 */
public final class GraphSearchProblem {

    public static <V> FromVertex<V> startingFrom(V vertex) {
        return new FromVertex<V>(vertex);
    }

    public static class FromVertex<V> {
        private V fromVertex;

        private FromVertex(V fromVertex) {
            this.fromVertex = fromVertex;
        }

        public <E> CostType<E> in(final HipsterGraph<V, E> graph) {
            TransitionFunction<E, V> tf;
            if (graph instanceof HipsterDirectedGraph) {
                final HipsterDirectedGraph<V, E> dg = (HipsterDirectedGraph<V, E>) graph;
                tf = new TransitionFunction<E, V>() {
                    @Override
                    public Iterable<Transition<E, V>> transitionsFrom(final V state) {
                        return Iterables.transform(dg.outgoingEdgesOf(state), new Function<GraphEdge<V, E>, Transition<E, V>>() {
                            @Override
                            public Transition<E, V> apply(GraphEdge<V, E> edge) {
                                return Transition.create(state, edge.getEdgeValue(), edge.getVertex2());
                            }
                        });
                    }
                };
            } else {
                tf = new TransitionFunction<E, V>() {
                    @Override
                    public Iterable<Transition<E, V>> transitionsFrom(final V state) {
                        return Iterables.transform(graph.edgesOf(state), new Function<GraphEdge<V, E>, Transition<E, V>>() {
                            @Override
                            public Transition<E, V> apply(GraphEdge<V, E> edge) {
                                V oppositeVertex = edge.getVertex1().equals(state) ? edge.getVertex2() : edge.getVertex1();
                                return Transition.create(state, edge.getEdgeValue(), oppositeVertex);
                            }
                        });
                    }
                };
            }
            return new CostType<E>(tf);
        }

        public class CostType<E> {
            private TransitionFunction<E, V> tf;

            private CostType(TransitionFunction<E, V> tf) {
                this.tf = tf;
            }

            public HeuristicType<Double> takeCostsFromEdges() {
                // Try to automatically obtain weights from edges
                CostFunction<E, V, Double> cf = new CostFunction<E, V, Double>() {
                    @Override
                    public Double evaluate(Transition<E, V> transition) {
                        E action = transition.getAction();
                        if (action instanceof Number) {
                            return ((Number) action).doubleValue();
                        } else {
                            throw new ClassCastException("The defined graph uses edges of type " +
                                    action.getClass() + " instead of Number. For custom edge costs" +
                                    " please use withGenericCosts method.");
                        }

                    }
                };
                return new HeuristicType<Double>(cf, BinaryOperation.doubleAdditionOp());
            }

            public HeuristicType<Double> extractCostFromEdges(final Function<E, Double> extractor) {
                CostFunction<E, V, Double> cf = new CostFunction<E, V, Double>() {
                    @Override
                    public Double evaluate(Transition<E, V> transition) {
                        return extractor.apply(transition.getAction());
                    }
                };
                return new HeuristicType<Double>(cf, BinaryOperation.doubleAdditionOp());
            }

            public <C extends Comparable<C>> HeuristicType<C> useGenericCosts(BinaryOperation<C> costAlgebra) {
                CostFunction<E, V, C> cf = new CostFunction<E, V, C>() {
                    @Override
                    public C evaluate(Transition<E, V> transition) {
                        return (C) transition.getAction();
                    }
                };
                return new HeuristicType<C>(cf, costAlgebra);
            }

            public Hipster.SearchProblem<E, V, UnweightedNode<E, V>> build() {
                return ProblemBuilder.create()
                        .initialState(fromVertex)
                        .defineProblemWithExplicitActions()
                        .useTransitionFunction(tf)
                        .build();
            }

            public class HeuristicType<C extends Comparable<C>> {
                private CostFunction<E, V, C> cf;
                private BinaryOperation<C> costAlgebra;

                private HeuristicType(CostFunction<E, V, C> cf, BinaryOperation<C> costAlgebra) {
                    this.cf = cf;
                    this.costAlgebra = costAlgebra;
                }

                public Final useHeuristicFunction(HeuristicFunction<V, C> hf) {
                    return new Final(hf);
                }

                public Hipster.SearchProblem<E, V, WeightedNode<E, V, C>> build() {
                    return ProblemBuilder.create()
                            .initialState(fromVertex)
                            .defineProblemWithExplicitActions()
                            .useTransitionFunction(tf)
                            .useGenericCostFunction(cf, costAlgebra)
                            .build();
                }

                public class Final {
                    private HeuristicFunction<V, C> hf;

                    private Final(HeuristicFunction<V, C> hf) {
                        this.hf = hf;
                    }

                    public Hipster.SearchProblem<E, V, WeightedNode<E, V, C>> build() {
                        return ProblemBuilder.create()
                                .initialState(fromVertex)
                                .defineProblemWithExplicitActions()
                                .useTransitionFunction(tf)
                                .useGenericCostFunction(cf, costAlgebra)
                                .useHeuristicFunction(hf)
                                .build();
                    }
                }
            }
        }

    }
}
