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

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SetCoverIteratorTest {

	@Test
	public void testIterativeSetCover01() {
		// Single solution [1,2],[3,4],[5,6]
        Set<Set<String>> solution = new HashSet<Set<String>>();
        solution.add(new HashSet<String>(Arrays.asList("1","2")));
        solution.add(new HashSet<String>(Arrays.asList("3","4")));
        solution.add(new HashSet<String>(Arrays.asList("5","6")));

        Set<Set<String>> sets = new HashSet<Set<String>>();
        sets.add(new HashSet<String>(Arrays.asList("1","2")));
        sets.add(new HashSet<String>(Arrays.asList("3","4")));
        sets.add(new HashSet<String>(Arrays.asList("5","6")));
        sets.add(new HashSet<String>(Arrays.asList("1","3", "5")));

        SetCoverIterator<String> it = new SetCoverIterator<String>(sets);
        Set<Set<String>> result = null;
        int i = 0;
        while(it.hasNext()){
            result = it.next();
            i++;
        }
        assertTrue(i==1 && result != null && result.equals(solution));

	}

    @Test
    public void testIterativeSetCover02() {

        Set<Set<String>> sets = new HashSet<Set<String>>();
        sets.add(new HashSet<String>(Arrays.asList("3","7")));
        sets.add(new HashSet<String>(Arrays.asList("2","4")));
        sets.add(new HashSet<String>(Arrays.asList("3","4","5","6")));
        sets.add(new HashSet<String>(Arrays.asList("5")));
        sets.add(new HashSet<String>(Arrays.asList("1")));
        sets.add(new HashSet<String>(Arrays.asList("1","2","5","6")));

        SetCoverIterator<String> it = new SetCoverIterator<String>(sets);
        Set<Set<String>> result = null;
        while(it.hasNext()){
            result = it.next();
            assertTrue(result.size()==3);
            break;
        }

    }
}
