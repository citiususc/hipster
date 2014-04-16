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

package es.usc.citius.lab.hipster.algorithm;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import es.usc.citius.hipster.algorithm.MultiobjectiveLS;
import es.usc.citius.hipster.model.Transition;
import es.usc.citius.hipster.model.function.BinaryFunction;
import es.usc.citius.hipster.model.function.CostFunction;
import es.usc.citius.hipster.model.function.TransitionFunction;
import es.usc.citius.hipster.model.function.impl.BinaryOperation;
import es.usc.citius.hipster.model.function.impl.LazyNodeExpander;
import es.usc.citius.hipster.model.function.impl.WeightedNodeFactory;
import es.usc.citius.hipster.model.impl.WeightedNode;
import es.usc.citius.hipster.util.graph.GraphBuilder;
import es.usc.citius.hipster.util.graph.GraphEdge;
import es.usc.citius.hipster.util.graph.HipsterDirectedGraph;
import org.junit.Test;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class MultiobjectiveShortestPathTest {

    public static class Cost implements Comparable<Cost> {
        private double c1;
        private double c2;

        public Cost(double c1, double c2) {
            this.c1 = c1;
            this.c2 = c2;
        }

        @Override
        public String toString() {
            return "Cost{" +
                    "c1=" + c1 +
                    ", c2=" + c2 +
                    '}';
        }

        @Override
        public int compareTo(Cost o) {
            // Lexicographical comparation
            if (c1 < o.c1 && c2 < o.c2){
                return -1;
            } else if (o.c1 < c1 && o.c2 < c2){
                return 1;
            }
            // Non-dominated
            return 0;
        }
    }
    @Test
    public void test(){
        // Create a multiobjective graph
        final HipsterDirectedGraph<String, Cost> graph =
                GraphBuilder.newGraph()
                        .connect("v1").to("v2").withEdge(new Cost(7d,1d))
                        .connect("v1").to("v3").withEdge(new Cost(1d,7d))
                        .connect("v1").to("v4").withEdge(new Cost(8d,4d))
                        .connect("v2").to("v4").withEdge(new Cost(2d,1d))
                        .connect("v2").to("v6").withEdge(new Cost(2d,2d))
                        .connect("v3").to("v4").withEdge(new Cost(1d,1d))
                        .connect("v4").to("v5").withEdge(new Cost(6d,4d))
                        .connect("v4").to("v6").withEdge(new Cost(2d,2d))
                        .buildDirectedGraph();

        // Define the custom components to work with the special cost
        WeightedNodeFactory<Cost, String, Cost> factory = new WeightedNodeFactory<Cost, String, Cost>(
                new CostFunction<Cost, String, Cost>() {
                    @Override
                    public Cost evaluate(Transition<Cost, String> transition) {
                        return transition.getAction();
                    }
                },

                new BinaryOperation<Cost>(new BinaryFunction<Cost>() {
                    @Override
                    public Cost apply(Cost a, Cost b) {
                        return new Cost(a.c1+b.c1, a.c2 + b.c2);
                    }
                }, new Cost(0d,0d), new Cost(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY))

        );

        TransitionFunction<Cost,String> tf = new TransitionFunction<Cost, String>() {
            @Override
            public Iterable<Transition<Cost, String>> transitionsFrom(final String state) {
                return Iterables.transform(graph.outgoingEdgesOf(state), new Function<GraphEdge<String, Cost>, Transition<Cost, String>>() {
                    @Override
                    public Transition<Cost, String> apply(GraphEdge<String, Cost> input) {
                        return Transition.create(state, input.getEdgeValue(), input.getVertex2());
                    }
                });
            }
        };

        System.out.println(tf.transitionsFrom("v1"));
        // Create the expander
        LazyNodeExpander<Cost, String, WeightedNode<Cost, String, Cost>> expander = new LazyNodeExpander<Cost, String, WeightedNode<Cost, String, Cost>>(tf, factory);

        // Create the initial node
        WeightedNode<Cost, String, Cost> initialNode = factory.makeNode(null, Transition.<Cost, String>create(null, null, "v1"));

        MultiobjectiveLS<Cost, String, Cost, WeightedNode<Cost, String, Cost>> algorithm = new MultiobjectiveLS<Cost, String, Cost, WeightedNode<Cost, String, Cost>>(initialNode, expander);
        algorithm.setGoalState("v6");
        System.out.println(algorithm.search());

        // TODO; Add solution verification
    }
}
