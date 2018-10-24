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
package es.usc.citius.lab.hipster.examples;


import es.usc.citius.hipster.examples.problem.NQueens;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class NQueensTest {

    @Test
    public void test8QueenZeroAttacked() throws Exception {
        int solution[]={3,6,2,7,1,4,0,5};
        assertEquals(0, new NQueens(solution).attackedQueens());
    }

    @Test
    public void test8QueenSameColumnDiagonalAttacked() throws Exception {
        int solution[]={3,6,3,7,1,4,0,5};
        assertEquals(3, new NQueens(solution).attackedQueens());
    }

    @Test
    public void test8QueenSameColumnAttacked() throws Exception {
        int solution[]={3,6,2,7,1,4,2,5};
        assertEquals(2, new NQueens(solution).attackedQueens());
    }

    @Test
    public void test8QueenEqualsHashCode() throws Exception {
        Set<NQueens> states = new HashSet<NQueens>();
        // Two different instances with the same queens are the same state!
        states.add(new NQueens(new int[]{0,1,2,3,4,5,6,7}));
        states.add(new NQueens(new int[]{0,1,2,3,4,5,6,7}));
        assertEquals(1, states.size());
    }
}
