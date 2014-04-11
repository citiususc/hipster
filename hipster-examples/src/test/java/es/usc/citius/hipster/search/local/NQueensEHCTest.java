///*
// * Copyright 2014 CITIUS <http://citius.usc.es>, University of Santiago de Compostela.
// *
// *    Licensed under the Apache License, Version 2.0 (the "License");
// *    you may not use this file except in compliance with the License.
// *    You may obtain a copy of the License at
// *
// *        http://www.apache.org/licenses/LICENSE-2.0
// *
// *    Unless required by applicable law or agreed to in writing, software
// *    distributed under the License is distributed on an "AS IS" BASIS,
// *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *    See the License for the specific language governing permissions and
// *    limitations under the License.
// */
//
//package es.usc.citius.hipster.search.local;
//
//
//import es.usc.citius.lab.hipster.examples.NQueens;
//import es.usc.citius.lab.hipster.function.HeuristicFunction;
//import es.usc.citius.lab.hipster.function.TransitionFunction;
//import es.usc.citius.lab.hipster.node.Node;
//import es.usc.citius.lab.hipster.node.Transition;
//import es.usc.citius.hipster.algorithm.localsearch.EnforcedHillClimbing;
//import org.junit.Test;
//
//import java.util.*;
//
//public class NQueensEHCTest {
//
//    @Test
//    public void testSearch8Queens() throws Exception {
//        // Transition: Interchange two positions randomly
//        final int size=8;
//        TransitionFunction<NQueens> tf = new TransitionFunction<NQueens>() {
//            @Override
//            public Iterable<? extends Transition<NQueens>> from(NQueens current) {
//                // Generate all possible movements of one queen
//                // There are size*(size-1) available movements
//                Set<NQueens> states = new HashSet<NQueens>();
//                for(int i=0; i < size; i++){
//                    for(int j=0; j < size; j++){
//                        // Change the queen at row i to column j
//                        // If i is already in j, then do not add the state
//                        if (current.getQueens()[i]!=j){
//                            int[] queens = Arrays.copyOf(current.getQueens(), size);
//                            queens[i]=j;
//                            states.add(new NQueens(queens));
//                        }
//                    }
//                }
//                return Transition.map(current, states);
//            }
//        };
//
//        HeuristicFunction<NQueens, Integer> hf = new HeuristicFunction<NQueens, Integer>() {
//            @Override
//            public Integer estimate(NQueens state) {
//                return state.attackedQueens();
//            }
//        };
//
//        NQueens initial = new NQueens(size);
//        EnforcedHillClimbing<NQueens, Integer> ehc = new EnforcedHillClimbing<NQueens, Integer>(initial, tf,hf);
//
//        System.out.println("Initial state: ");
//        System.out.println(initial.toString());
//
//        int iteration=0;
//        Integer best = ehc.getBestHeuristic();
//        while(ehc.hasNext()){
//            iteration++;
//            Node<NQueens> node = ehc.next();
//            if (ehc.getBestHeuristic() < best){
//                best = ehc.getBestHeuristic();
//                System.out.println("New local minimum found with value " + best + " at iteration " + iteration);
//            }
//            int attacked = node.transition().to().attackedQueens();
//            if (attacked == 0){
//                System.out.println("Solution found: ");
//                System.out.println(node.transition().to().toString());
//                break;
//            }
//        }
//    }
//}
