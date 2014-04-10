/*
 * Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package es.usc.citius.lab.hipster.algorithm.combinatorial;

import es.usc.citius.hipster.util.SetCoverIterator;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SetCoverIteratorTest {

    @Test
    public void testOneElement(){
        Set<Set<String>> sets = new HashSet<Set<String>>();
        sets.add(Collections.singleton("A"));
        SetCoverIterator<String> it = new SetCoverIterator<String>(sets);
        assertTrue(it.hasNext());
        assertEquals(1, it.next().size());
    }

    @Test
    public void testTwoElements(){
        Set<Set<String>> sets = new HashSet<Set<String>>();
        sets.add(Collections.singleton("A"));
        sets.add(Collections.singleton("B"));
        SetCoverIterator<String> it = new SetCoverIterator<String>(sets);
        assertTrue(it.hasNext());
        assertEquals(2, it.next().size());
        assertFalse(it.hasNext());
    }

    @Test
    public void testOneSingleSolution() {
        // Single solution [1,2],[3,4],[5,6]
        Set<Set<String>> solution = new HashSet<Set<String>>();
        solution.add(new HashSet<String>(Arrays.asList("1", "2")));
        solution.add(new HashSet<String>(Arrays.asList("3", "4")));
        solution.add(new HashSet<String>(Arrays.asList("5", "6")));

        Set<Set<String>> sets = new HashSet<Set<String>>();
        sets.add(new HashSet<String>(Arrays.asList("1", "2")));
        sets.add(new HashSet<String>(Arrays.asList("3", "4")));
        sets.add(new HashSet<String>(Arrays.asList("5", "6")));
        sets.add(new HashSet<String>(Arrays.asList("1", "3", "5")));

        SetCoverIterator<String> it = new SetCoverIterator<String>(sets);
        Set<Set<String>> result = null;
        int i = 0;
        while (it.hasNext()) {
            result = it.next();
            i++;
        }
        assertTrue(i == 1 && result != null && result.equals(solution));

    }

    @Test
    public void testMultipleSolutions() {

        Set<Set<String>> sets = new HashSet<Set<String>>();
        sets.add(new HashSet<String>(Arrays.asList("3", "7")));
        sets.add(new HashSet<String>(Arrays.asList("2", "4")));
        sets.add(new HashSet<String>(Arrays.asList("3", "4", "5", "6")));
        sets.add(new HashSet<String>(Arrays.asList("5")));
        sets.add(new HashSet<String>(Arrays.asList("1")));
        sets.add(new HashSet<String>(Arrays.asList("1", "2", "5", "6")));

        SetCoverIterator<String> it = new SetCoverIterator<String>(sets);
        Set<Set<String>> result = null;
        while (it.hasNext()) {
            result = it.next();
            assertTrue(result.size() == 3);
            break;
        }

    }

    @Test
    public void testTwoSolution(){
        Set<Set<String>> sets = new HashSet<Set<String>>();
        // CD
        sets.add(new HashSet<String>(Arrays.asList("2", "3", "5", "6", "7")));
        // A
        sets.add(new HashSet<String>(Arrays.asList("1", "2")));
        // I
        sets.add(new HashSet<String>(Arrays.asList("4", "8")));
        sets.add(new HashSet<String>(Arrays.asList("5")));
        // EFGH
        sets.add(new HashSet<String>(Arrays.asList("3")));
        // J
        sets.add(new HashSet<String>(Arrays.asList("4")));
        // K
        sets.add(new HashSet<String>(Arrays.asList("7")));
        // B
        sets.add(new HashSet<String>(Arrays.asList("1")));
        SetCoverIterator<String> it = new SetCoverIterator<String>(sets);
        Set<Set<Set<String>>> expected = new HashSet<Set<Set<String>>>();
        Set<Set<String>> sol1 = new HashSet<Set<String>>();
        sol1.add(new HashSet<String>(Arrays.asList("2","3","5","6","7")));
        sol1.add(new HashSet<String>(Arrays.asList("2","1")));
        sol1.add(new HashSet<String>(Arrays.asList("4","8")));
        Set<Set<String>> sol2 = new HashSet<Set<String>>();
        sol2.add(new HashSet<String>(Arrays.asList("2","3","5","6","7")));
        sol2.add(new HashSet<String>(Arrays.asList("1")));
        sol2.add(new HashSet<String>(Arrays.asList("4","8")));
        expected.add(sol1);
        expected.add(sol2);

        int i=0;
        while (it.hasNext()) {
            assertTrue(expected.contains(it.next()));
            i++;
        }
        assertEquals(2, i);
    }
}
