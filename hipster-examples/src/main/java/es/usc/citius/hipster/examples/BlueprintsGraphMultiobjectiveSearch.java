/**
 * Copyright (C) 2013-2018 Centro de Investigación en Tecnoloxías da Información (CITIUS) (http://citius.usc.es)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package es.usc.citius.hipster.examples;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLReader;
import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.model.Transition;
import es.usc.citius.hipster.model.function.ActionFunction;
import es.usc.citius.hipster.model.function.ActionStateTransitionFunction;
import es.usc.citius.hipster.model.function.BinaryFunction;
import es.usc.citius.hipster.model.function.CostFunction;
import es.usc.citius.hipster.model.function.impl.BinaryOperation;
import es.usc.citius.hipster.model.problem.ProblemBuilder;
import es.usc.citius.hipster.model.problem.SearchProblem;

import java.io.IOException;
import java.net.URL;

/**
 * <p>Simple example that loads a GraphML example graph from GitHub, loads it with the
 * Blueprints API and then solves the multiobjective problem with Hipster</p>
 *
 * A full description of the problem is published on
 * <a href="https://github.com/citiususc/hipster/wiki/Multiobjective-Search-with-Hipster-and-TinkerPop-Blueprints">Hipster's wiki</a>.
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class BlueprintsGraphMultiobjectiveSearch {

    public static void main(String[] args) throws IOException {

        Graph g = new TinkerGraph();
        GraphMLReader.inputGraph(g, new URL("https://gist.githubusercontent.com/pablormier/5d52543b4dcae297ab14/raw/multiobjective-graph.graphml").openStream());

        // Since we use a special cost, we need to define a BinaryOperation<Cost>
        // that provides the required elements to work with our special cost type.
        // These elements are: a BinaryFunction<Cost> that defines how to compute
        // a new cost from two costs: C x C -> C, the identity element I of our
        // cost (C + I = C, I + C = C), and the maximum value.

        // Cost a + Cost b is defined as a new cost a.c1+b.c1, a.c2+b.c2
        BinaryFunction<Cost> f = new BinaryFunction<Cost>() {
            @Override
            public Cost apply(Cost a, Cost b) {
                Cost c = new Cost(a.c1 + b.c1, a.c2 + b.c2);
                return c;
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


        // Define a problem
        SearchProblem p = ProblemBuilder.create()
                .initialState(g.getVertex("v1"))
                .defineProblemWithExplicitActions()
                .useActionFunction(new ActionFunction<Edge, Vertex>() {
                    @Override
                    public Iterable<Edge> actionsFor(Vertex state) {
                        return state.getEdges(Direction.OUT);
                    }
                }).useTransitionFunction(new ActionStateTransitionFunction<Edge, Vertex>() {
                    @Override
                    public Vertex apply(Edge action, Vertex state) {
                        return action.getVertex(Direction.IN);
                    }
                }).useGenericCostFunction(new CostFunction<Edge, Vertex, Cost>() {
                    @Override
                    public Cost evaluate(Transition<Edge, Vertex> transition) {
                        Double cost1 = Double.valueOf(transition.getAction().getProperty("c1").toString());
                        Double cost2 = Double.valueOf(transition.getAction().getProperty("c2").toString());
                        return new Cost(cost1, cost2);
                    }
                }, bf).build();

        System.out.println(Hipster.createMultiobjectiveLS(p).search(g.getVertex("v6")));


    }

    /**
     * Build an example graph to execute in this example.
     *
     * @return example graph
     */
    private static Graph buildGraph() {
        Graph g = new TinkerGraph();
        //add vertices
        Vertex v1 = g.addVertex("v1");
        Vertex v2 = g.addVertex("v2");
        Vertex v3 = g.addVertex("v3");
        Vertex v4 = g.addVertex("v4");
        Vertex v5 = g.addVertex("v5");
        Vertex v6 = g.addVertex("v6");

        //add edges labeled with costs
        Edge e1 = g.addEdge("e1", v1, v2, "(7, 1)");
        e1.setProperty("c1", 7);
        e1.setProperty("c2", 1);

        Edge e2 = g.addEdge("e2", v1, v3, "(1, 4)");
        e2.setProperty("c1", 1);
        e2.setProperty("c2", 4);

        Edge e3 = g.addEdge("e3", v1, v4, "(2, 1)");
        e3.setProperty("c1", 2);
        e3.setProperty("c2", 1);

        Edge e4 = g.addEdge("e4", v2, v4, "(1, 1)");
        e4.setProperty("c1", 1);
        e4.setProperty("c2", 1);

        Edge e5 = g.addEdge("e5", v2, v6, "(2, 1)");
        e5.setProperty("c1", 2);
        e5.setProperty("c2", 1);

        Edge e6 = g.addEdge("e6", v3, v4, "(1, 1)");
        e6.setProperty("c1", 1);
        e6.setProperty("c2", 1);

        Edge e7 = g.addEdge("e7", v4, v5, "(3, 2)");
        e7.setProperty("c1", 3);
        e7.setProperty("c2", 2);

        Edge e8 = g.addEdge("e8", v4, v6, "(4, 8)");
        e8.setProperty("c1", 4);
        e8.setProperty("c2", 8);

        Edge e9 = g.addEdge("e9", v5, v6, "(1, 1)");
        e9.setProperty("c1", 1);
        e9.setProperty("c2", 1);

        return g;
    }

    /**
     * Inner class to define a multi-objective cost.
     */
    static class Cost implements Comparable<Cost> {
        private double c1;
        private double c2;

        /**
         * Creation of multi-objective cost from two double values.
         *
         * @param c1 first element of the cost
         * @param c2 second element of the cost
         */
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

        /**
         * A cost is lower/higher than other if both components
         * are (dominated cost). Returns 0 in the rest of the cases
         * (non-dominated).
         *
         * @param o other cost element
         * @return comparison result: -1/1 (dominated) or 0 (non-dominated)
         */
        @Override
        public int compareTo(Cost o) {

            if (c1 <= o.c1 && c2 <= o.c2){
                if (c1 < o.c1 || c2 < o.c2){
                    return -1;
                }
            } else if (o.c1 <= c1 && o.c2 <= o.c2){
                if (o.c1 < c1 || o.c2 < c2){
                    return 1;
                }
            }

            // Non-dominated
            return 0;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Cost cost = (Cost) o;

            if (Double.compare(cost.c1, c1) != 0) return false;
            if (Double.compare(cost.c2, c2) != 0) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            temp = Double.doubleToLongBits(c1);
            result = (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(c2);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            return result;
        }
    }
}
