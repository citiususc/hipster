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


import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import es.usc.citius.lab.hipster.algorithm.factory.BreadthFirstSearchIteratorFactory;
import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.node.Node;
import es.usc.citius.lab.hipster.node.Transition;
import org.junit.Test;

import java.util.Arrays;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BreadthFirstSearchTest {
    @Test
    public void treeSearch(){
        /*
           BFS over this tree example
                    A
                   /|\
                  B C D
                 /\ |
                E F H
                |
                G
         */
        final ListMultimap<String, String> tree = ArrayListMultimap.create();
        tree.get("A").addAll(Arrays.asList("B", "C", "D"));
        tree.get("B").addAll(Arrays.asList("E","F"));
        tree.get("E").add("G");
        tree.get("C").add("H");
        validate(bfs(tree, "A"), new String[]{"A","B","C","D","E","F","H","G"});
    }

    @Test
    public void consistentHasNext(){
        final ListMultimap<String, String> graph = ArrayListMultimap.create();
        graph.get("A").add("B");
        BreadthFirstSearch<String> bfs = bfs(graph, "A");
        assertTrue(bfs.hasNext());
        bfs.next();
        assertTrue(bfs.hasNext());
        bfs.next();
        assertFalse(bfs.hasNext());
    }

    @Test
    public void noSuccessors(){
        final ListMultimap<String, String> graph = ArrayListMultimap.create();
        BreadthFirstSearch<String> bfs = bfs(graph, "A");
        bfs.next();
        assertFalse(bfs.hasNext());
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
        validate(bfs(graph, "A"), new String[]{"A", "B", "C", "D", "E", "F", "G"});
    }

    private BreadthFirstSearch<String> bfs(final Multimap<String, String> graph, String initial){
        TransitionFunction<String> tf = new TransitionFunction<String>() {
            @Override
            public Iterable<? extends Transition<String>> from(String current) {
                return Transition.map(current, graph.get(current));
            }
        };
        return new BreadthFirstSearch<String>(initial, tf);
    }

    private void validate(BreadthFirstSearch<String> dfs,String[] expected){
        int i = 0;
        while(dfs.hasNext()){
            Node<String> node = dfs.next();
            assertEquals(expected[i++], node.transition().to());
        }
    }
}
