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

import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.model.function.BinaryFunction;
import es.usc.citius.hipster.model.function.impl.BinaryOperation;
import es.usc.citius.hipster.util.graph.GraphBuilder;
import es.usc.citius.hipster.util.graph.GraphSearchProblem;
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
            // Lexicographical comparison
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
                GraphBuilder.create()
                        .connect("v1").to("v2").withEdge(new Cost(7d, 1d))
                        .connect("v1").to("v3").withEdge(new Cost(1d, 7d))
                        .connect("v1").to("v4").withEdge(new Cost(8d, 4d))
                        .connect("v2").to("v4").withEdge(new Cost(2d, 1d))
                        .connect("v2").to("v6").withEdge(new Cost(2d, 2d))
                        .connect("v3").to("v4").withEdge(new Cost(1d, 1d))
                        .connect("v4").to("v5").withEdge(new Cost(6d, 4d))
                        .connect("v4").to("v6").withEdge(new Cost(2d, 2d))
                        .buildDirectedGraph();

        // Since we use a special cost, we need to define a BinaryOperation<Cost>
        // that provides the required elements to work with our special cost type.
        // These elements are: a BinaryFunction<Cost> that defines how to compute
        // a new cost from two costs: C x C -> C, the identity element I of our
        // cost (C + I = C, I + C = C), and the maximum value.

        // Cost a + Cost b is defined as a new cost a.c1+b.c1, a.c2+b.c2
        BinaryFunction<Cost> f = new BinaryFunction<Cost>() {
            @Override
            public Cost apply(Cost a, Cost b) {
                return new Cost(a.c1+b.c1, a.c2 + b.c2);
            }
        };
        // The identity cost identity satisfy:
        // f.apply(c, identity).equals(c)
        // f.apply(identity, c).equals(c)
        Cost identity = new Cost(0d, 0d);

        // Maximum value of our costs
        Cost max = new Cost(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);

        // Create our custom binary operation:
        BinaryOperation<Cost> bf = new BinaryOperation<Cost>(f, identity, max);

        System.out.println(Hipster.createMultiobjectiveLS(GraphSearchProblem.from("v1").to("v6").in(graph).useGenericCosts(bf).build()).search());

        // TODO; Add solution verification
    }
}
