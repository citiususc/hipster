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
import es.usc.citius.hipster.util.examples.RomanianProblem;
import es.usc.citius.hipster.util.graph.GraphSearchProblem;
import org.junit.Test;

import static es.usc.citius.hipster.util.examples.RomanianProblem.*;

public class RomanianProblemOptimalPathTest {

    @Test
    public void RomanianProblemTest(){
        System.out.println(Hipster.createDijkstra(GraphSearchProblem.from(City.Arad).to(City.Bucharest).in(RomanianProblem.graph())).search());
    }
}
