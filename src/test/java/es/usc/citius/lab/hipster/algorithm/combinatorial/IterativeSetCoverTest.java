package es.usc.citius.lab.hipster.algorithm.combinatorial;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class IterativeSetCoverTest {

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

        IterativeSetCover<String> it = new IterativeSetCover<String>(sets);
        Set<Set<String>> result = null;
        int i = 0;
        while(it.hasNext()){
            result = it.next();
            System.out.println("Solution found: " + result);
            i++;
        }
        assertTrue(i==1 && result != null && result.equals(solution));

	}

}
