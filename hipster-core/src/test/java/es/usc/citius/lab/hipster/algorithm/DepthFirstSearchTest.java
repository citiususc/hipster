/*
 * Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS), University of Santiago de Compostela (USC).
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


import com.google.common.collect.*;
import es.usc.citius.lab.hipster.algorithm.problem.SearchProblem;
import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.node.Node;
import es.usc.citius.lab.hipster.node.Transition;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DepthFirstSearchTest {


    @Test
    public void treeSearch(){
        /*
           DFS over this tree example
                    A
                   /|\
                  B C D
                 /\ |
                E F H
                |
                G
         */
        final ListMultimap<String, String> tree = ArrayListMultimap.create();
        tree.get("A").addAll(Arrays.asList("B","C","D"));
        tree.get("B").addAll(Arrays.asList("E","F"));
        tree.get("E").add("G");
        tree.get("C").add("H");
        validate(dfs(tree, "A"), new String[]{"A", "B", "E", "G", "F", "C", "H", "D"});
    }

    @Test
    public void consistentHasNext(){
        final ListMultimap<String, String> graph = ArrayListMultimap.create();
        graph.get("A").add("B");
        DepthFirstSearch<String> dfs = dfs(graph, "A");
        assertTrue(dfs.hasNext());
        dfs.next();
        assertTrue(dfs.hasNext());
        dfs.next();
        assertFalse(dfs.hasNext());
    }

    @Test
    public void noSuccessors(){
        final ListMultimap<String, String> graph = ArrayListMultimap.create();
        DepthFirstSearch<String> dfs = dfs(graph, "A");
        dfs.next();
        assertFalse(dfs.hasNext());
    }

    @Test
    public void cyclicGraphSearch(){
        final ListMultimap<String, String> graph = ArrayListMultimap.create();
        graph.get("A").addAll(Arrays.asList("B", "C", "D"));
        graph.get("B").addAll(Arrays.asList("E", "F"));
        graph.get("E").addAll(Arrays.asList("F", "B"));
        graph.get("F").addAll(Arrays.asList("E", "B"));
        graph.get("C").addAll(Arrays.asList("D","A"));
        graph.get("D").addAll(Arrays.asList("A","C","G"));
        graph.get("G").add("D");
        validate(dfs(graph, "A"), new String[]{"A", "B", "E", "F", "C", "D", "G"});
    }

    private DepthFirstSearch<String> dfs(final Multimap<String, String> graph, String initial){
        TransitionFunction<String> tf = new TransitionFunction<String>() {
            @Override
            public Iterable<? extends Transition<String>> from(String current) {
                List<Transition<String>> transitions = Lists.newArrayList(Transition.map(current, graph.get(current)));
                // By default, this DFS implementation iterates from right to left.
                // Inverting the order of the successors makes the algorithm iterate from left to right
                Collections.reverse(transitions);
                return transitions;
            }
        };

        return new DepthFirstSearch<String>(initial, tf);
    }

    private void validate(DepthFirstSearch<String> dfs,String[] expected){
        int i = 0;
        while(dfs.hasNext()){
            Node<String> node = dfs.next();
            assertEquals(expected[i++], node.transition().to());
        }
    }

}
