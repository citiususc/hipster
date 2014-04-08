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


import es.usc.citius.hipster.model.ActionState;
import es.usc.citius.hipster.model.function.CostFunction;
import es.usc.citius.hipster.model.function.TransitionFunction;
import es.usc.citius.hipster.model.problem.InformedSearchProblem;

import java.util.HashSet;
import java.util.Set;

/**
 * Create typical graph search problems working with directed graphs and
 * double weights.
 * @author Pablo Rodríguez Mier
 */
public final class GraphProblem {

    public static class FromVertex<V> {
        private V fromVertex;

        public FromVertex(V fromVertex) {
            this.fromVertex = fromVertex;
        }

        public class ToVertex {
            private V toVertex;

            public ToVertex(V toVertex) {
                this.toVertex = toVertex;
            }

            public InformedSearchProblem<WeightedEdge,V,Double> in(final HipsterDirectedGraph<V,WeightedEdge> graph){
                return new InformedSearchProblem<WeightedEdge, V, Double>() {
                    @Override
                    public CostFunction<WeightedEdge, V, Double> getCostFunction() {
                        return new CostFunction<WeightedEdge, V, Double>() {
                            @Override
                            public Double evaluate(ActionState<WeightedEdge, V> actionState) {
                                return actionState.getAction().getValue();
                            }
                        };
                    }

                    @Override
                    public TransitionFunction<WeightedEdge, V> getTransitionFunction() {
                        return new TransitionFunction<WeightedEdge, V>() {
                            @Override
                            public Iterable<ActionState<WeightedEdge, V>> transitionsFrom(V state) {
                                Set<ActionState<WeightedEdge, V>> as = new HashSet<ActionState<WeightedEdge, V>>();
                                for(WeightedEdge edge : graph.outgoingEdgesFrom(state)){
                                    as.add(new ActionState<WeightedEdge, V>(edge, graph.targetVertexOf(edge)));
                                }
                                return as;
                            }
                        };
                    }

                    @Override
                    public V getInitialState() {
                        return fromVertex;
                    }

                    @Override
                    public V getGoalState() {
                        return toVertex;
                    }
                };
            }
        }

        public ToVertex to(V vertex){
            return new ToVertex(vertex);
        }
    }

    public static <V> FromVertex from(V vertex){
        return new FromVertex<V>(vertex);
    }
}
