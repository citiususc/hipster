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

package es.usc.citius.hipster.algorithm.problem.romanian;


import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.model.problem.InformedSearchProblem;
import es.usc.citius.hipster.util.examples.RomanianProblem;
import es.usc.citius.hipster.util.graph.GraphBuilder;
import es.usc.citius.hipster.util.graph.GraphSearchProblem;
import es.usc.citius.hipster.util.graph.HipsterGraph;
import org.junit.Test;

import static es.usc.citius.hipster.util.examples.RomanianProblem.*;

public class RomanianProblemOptimalPathTest {


    @Test
    public void RomanianProblemTest(){

        HipsterGraph<City,Double> graph =
                GraphBuilder.<City,Double>newGraph()
                .connect(City.Arad).to(City.Zerind).withEdge(75d)
                .connect(City.Arad).to(City.Timisoara).withEdge(118d)
                .connect(City.Arad).to(City.Sibiu).withEdge(140d)
                .connect(City.Bucharest).to(City.Giurgiu).withEdge(90d)
                .connect(City.Bucharest).to(City.Urziceni).withEdge(85d)
                .connect(City.Bucharest).to(City.Fagaras).withEdge(211d)
                .connect(City.Bucharest).to(City.Pitesti).withEdge(101d)
                .connect(City.Craiova).to(City.Drobeta).withEdge(120d)
                .connect(City.Craiova).to(City.Rimnicu_Vilcea).withEdge(146d)
                .connect(City.Craiova).to(City.Pitesti).withEdge(138d)
                .connect(City.Drobeta).to(City.Mehadia).withEdge(75d)
                .connect(City.Eforie).to(City.Hirsova).withEdge(86d)
                .connect(City.Fagaras).to(City.Sibiu).withEdge(99d)
                .connect(City.Hirsova).to(City.Urziceni).withEdge(98d)
                .connect(City.Iasi).to(City.Neamt).withEdge(87d)
                .connect(City.Iasi).to(City.Vaslui).withEdge(92d)
                .connect(City.Lugoj).to(City.Timisoara).withEdge(111d)
                .connect(City.Lugoj).to(City.Mehadia).withEdge(70d)
                .connect(City.Oradea).to(City.Zerind).withEdge(71d)
                .connect(City.Oradea).to(City.Sibiu).withEdge(151d)
                .connect(City.Pitesti).to(City.Rimnicu_Vilcea).withEdge(97d)
                .connect(City.Rimnicu_Vilcea).to(City.Sibiu).withEdge(80d)
                .connect(City.Urziceni).to(City.Vaslui).withEdge(142d);

        System.out.println(Hipster.createDijkstra(GraphSearchProblem.from(City.Arad).to(City.Bucharest).in(graph)).search());
    }
}
