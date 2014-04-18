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
import es.usc.citius.hipster.model.function.TransitionFunction;
import es.usc.citius.hipster.model.function.impl.BinaryOperation;
import es.usc.citius.hipster.model.impl.UnweightedNode;
import es.usc.citius.hipster.model.impl.WeightedNode;
import es.usc.citius.hipster.model.problem.ProblemBuilder;

/**
 * @author Pablo Rodríguez Mier
 */
public final class GraphSearchProblem {

    public static class FromVertex<V> {
        private V fromVertex;

        private FromVertex(V fromVertex) {
            this.fromVertex = fromVertex;
        }

        public class ToVertex {
            private V toVertex;

            private ToVertex(V toVertex) {
                this.toVertex = toVertex;
            }

            public class WeightType<E> {
                private TransitionFunction<E,V> tf;

                private WeightType(TransitionFunction<E, V> tf) {
                    this.tf = tf;
                }

                public Hipster.SearchComponents<E, V, WeightedNode<E, V, Double>> takeEdgesAsCosts(){
                    // Try to automatically obtain weights from edges
                    CostFunction<E,V,Double> cf = new CostFunction<E, V, Double>() {
                        @Override
                        public Double evaluate(Transition<E, V> transition) {
                            E action = transition.getAction();
                            if (action instanceof Number){
                                return ((Number)action).doubleValue();
                            } else {
                                throw new ClassCastException("The defined graph uses edges of type " +
                                        action.getClass() + " instead of Number. For custom edge costs" +
                                        " please use withGenericCosts method.");
                            }

                        }
                    };
                    return ProblemBuilder.create()
                            .initialState(fromVertex)
                            .goalState(toVertex)
                            .defineProblemWithExplicitActions()
                            .useTransitionFunction(tf)
                            .useCostFunction(cf)
                            .build();
                }

                public <C extends Comparable<C>> Hipster.SearchComponents<E, V, WeightedNode<E, V, C>> withGenericCosts(BinaryOperation<C> costAlgebra){
                    return ProblemBuilder.create()
                            .initialState(fromVertex)
                            .goalState(toVertex)
                            .defineProblemWithExplicitActions()
                            .useTransitionFunction(tf)
                            .useGenericCostFunction(new CostFunction<E, V, C>() {
                                @Override
                                public C evaluate(Transition<E, V> transition) {
                                    return (C)transition.getAction();
                                }
                            }, costAlgebra)
                            .build();
                }

                public Hipster.SearchComponents<E, V, UnweightedNode<E,V>> withoutCosts(){
                    return ProblemBuilder.create()
                            .initialState(fromVertex)
                            .goalState(toVertex)
                            .defineProblemWithExplicitActions()
                            .useTransitionFunction(tf)
                            .build();
                }
            }

            public <E> WeightType<E> in(final HipsterGraph<V, E> graph) {
                TransitionFunction<E,V> tf;
                if (graph instanceof HipsterDirectedGraph){
                    final HipsterDirectedGraph<V,E> dg = (HipsterDirectedGraph<V,E>) graph;
                    tf = new TransitionFunction<E, V>() {
                        @Override
                        public Iterable<Transition<E, V>> transitionsFrom(final V state) {
                            return Iterables.transform(dg.outgoingEdgesOf(state), new Function<GraphEdge<V, E>, Transition<E,V>>() {
                                @Override
                                public Transition<E,V> apply(GraphEdge<V, E> edge) {
                                    return Transition.create(state, edge.getEdgeValue(), edge.getVertex2());
                                }
                            });
                        }
                    };
                } else {
                    tf = new TransitionFunction<E, V>() {
                        @Override
                        public Iterable<Transition<E, V>> transitionsFrom(final V state) {
                            return Iterables.transform(graph.edgesOf(state), new Function<GraphEdge<V, E>, Transition<E,V>>() {
                                @Override
                                public Transition<E,V> apply(GraphEdge<V, E> edge) {
                                    V oppositeVertex = edge.getVertex1().equals(state) ? edge.getVertex2() : edge.getVertex1();
                                    return Transition.create(state, edge.getEdgeValue(), oppositeVertex);
                                }
                            });
                        }
                    };
                }
                return new WeightType<E>(tf);
            }

        }
        public ToVertex to(V vertex) {
            return new ToVertex(vertex);
        }
    }
    public static <V> FromVertex<V> from(V vertex) {
        return new FromVertex<V>(vertex);
    }
}
