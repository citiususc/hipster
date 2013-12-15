/*
 * Copyright 2013 CITIUS <http://citius.usc.es>, University of Santiago de Compostela.
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
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import es.usc.citius.lab.hipster.algorithm.problem.DefaultSearchProblem;
import es.usc.citius.lab.hipster.algorithm.problem.InformedSearchProblem;
import es.usc.citius.lab.hipster.function.CostFunction;
import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.node.HeuristicNode;
import es.usc.citius.lab.hipster.node.Transition;
import es.usc.citius.lab.hipster.testutils.MazeHeuristicSearchProblem;
import es.usc.citius.lab.hipster.util.maze.MazeSearch;
import es.usc.citius.lab.hipster.util.maze.Mazes;
import org.junit.Test;

import java.awt.*;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class IDAStarMazeTest {

    public IDAStarMazeTest() {
    }

    @Test
    public void treeSearch(){
        /*
           IDA over this tree example
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
        search(tree, "A", "H");
        //validate(dfs(tree, "A"), new String[]{"A", "B", "E", "G", "F", "C", "H", "D"});
    }


    @Test
    public void IDAStar_Maze1() throws InterruptedException {
        execute(Mazes.Example.MAZE1, true);
    }

    @Test
    public void IDAStar_Maze3() throws InterruptedException {
        execute(Mazes.Example.MAZE3, true);
    }



    private void search(final Multimap<String, String> graph, String initial, String goal){
        TransitionFunction<String> tf = new TransitionFunction<String>() {
            @Override
            public Iterable<? extends Transition<String>> from(String current) {
                java.util.List<Transition<String>> transitions = Lists.newArrayList(Transition.map(current, graph.get(current)));
                // By default, this DFS implementation iterates from right to left.
                // Inverting the order of the successors makes the algorithm iterate from left to right
                //Collections.reverse(transitions);
                return transitions;
            }
        };

        for(HeuristicNode<String, Double> node :Algorithms.createIDAStar(new DefaultSearchProblem<String>(initial, goal, tf, new CostFunction<String, Double>() {
            @Override
            public Double evaluate(Transition<String> transition) {
                return 1d;
            }
        }))) {
            System.out.println(node.toString());
        }

    }

    private void execute(Mazes.Example example, boolean heuristic) {
        Iterator<HeuristicNode<Point,Double>> it = Algorithms.createIDAStar(new MazeHeuristicSearchProblem(example.getMaze(), heuristic)).iterator();
        MazeSearch.Result resultIterator = MazeSearch.executeIteratorSearch(it, example.getMaze().getGoalLoc());
        assertEquals(example.getMinimalPathCost(), resultIterator.getCost(), 0.0000001);
    }

}
