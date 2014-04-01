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

package es.usc.citius.hipster.algorithm;


import com.google.common.base.Stopwatch;
import es.usc.citius.hipster.model.ActionState;
import es.usc.citius.hipster.model.Node;
import es.usc.citius.hipster.model.UnweightedNode;
import es.usc.citius.hipster.model.function.NodeFactory;
import es.usc.citius.hipster.model.problem.SearchProblem;

import java.util.Iterator;
import java.util.List;

public final class Hipster {

    /**
     * Do not instantiate this class
     */
    private Hipster(){
        throw new IllegalAccessError("Do not instantiate Hipster class. Use the static methods instead.");
    }


    /**
     * Helper class.
     * @param <S>
     * @param <N>
     */
    public static final class Algorithm<A,S,N extends Node<A,S,N>> implements Iterable<N>{
        //private AlgorithmIteratorFactory<S, N> factory;
        private S goal;

        /**
         * Holds information about the search process.
         */
        public final class Result {
            Stopwatch stopwatch;
            int iterations;
            N goalNode;
            List<S> optimalPath;

            public Result(N goalNode, List<S> optimalPath, int iterations, Stopwatch stopwatch) {
                this.goalNode = goalNode;
                this.optimalPath = optimalPath;
                this.iterations = iterations;
                this.stopwatch = stopwatch;
            }

            /**
             * Returns a stopped {@link Stopwatch} with the total search time.
             * Use stopwatch.toString() to print the formatted time.
             * @return stopwatch with the total search time.
             */
            public Stopwatch getStopwatch() {
                return stopwatch;
            }

            /**
             * Number of iterations performed by the search algorithm.
             * @return number of iterations.
             */
            public int getIterations() {
                return iterations;
            }

            /**
             * Last node expanded with the goal state. Use
             * {@link es.usc.citius.lab.hipster.node.Node#path()} to obtain the
             * full path from the initial node.
             *
             * @return goal node.
             */
            public N getGoalNode() {
                return goalNode;
            }

            public List<S> getOptimalPath() {
                return optimalPath;
            }
        }

        public interface SearchListener<N> {
            void handle(N node);
        }

        public Algorithm(AlgorithmIteratorFactory<S, N> factory, S goal) {
            this.factory = factory;
            this.goal = goal;
        }

        private Result search(Iterator<N> it){
            int iteration = 0;
            Stopwatch w = Stopwatch.createStarted();
            N goal = null;
            while(it.hasNext()){
                iteration++;
                N node = it.next();
                if (node.state().equals(this.goal)){
                    goal = node;
                    break;
                }
            }
            w.stop();
            return new Result(goal, AbstractNode.statesFrom(goal.path()), iteration, w);
        }

        /**
         * Runs the search and returns shortest path from
         * the initial state to the goal state.
         *
         * @return list with states representing the path from the origin to goal state.
         */
        public List<S> getOptimalPath(){
            Iterator<N> it = factory.create();
            return AbstractNode.statesFrom(search(it).goalNode.path());
        }

        /**
         * Executes the search algorithm and returns a {@link Result} class
         * with the information of the search.
         *
         * @see Result
         * @return Result instance with the search information.
         */
        public Result search(){
            Iterator<N> it = factory.create();
            return search(it);
        }

        /**
         * Executes the search algorithm and invokes the method
         * {@link SearchListener#handle(Object)} passing the current
         * explored node to the listener.
         *
         * <pre>
         * {@code Search<String, Node<String>> searchAlgorithm;
         *     searchAlgorithm.search(new SearchListener<String>(){
         *          void handle(Node<String> node){
         *              System.out.println("Current state: " + node.transition().to());
         *          }
         *     });
         * }
         * </pre>
         *
         * @param listener listener used to receive the explored nodes.
         */
        public void search(SearchListener<N> listener){
            Iterator<N> it = factory.create();
            while(it.hasNext()){
                listener.handle(it.next());
            }
        }

        @Override
        public Iterator<N> iterator() {
            return factory.create();
        }
    }


    public static <A,S> BreadthFirstSearch<A,S,UnweightedNode<A,S>> createBreadthFirstSearch(SearchProblem<A,S> problem){
        return new BreadthFirstSearch<A, S, UnweightedNode<A,S>>(problem.getInitialState(), problem.getTransitionFunction(),
                new NodeFactory<A, S, UnweightedNode<A,S>>() {
                    @Override
                    public UnweightedNode<A,S> makeNode(UnweightedNode<A,S> fromNode, ActionState<A, S> actionState) {
                        return new UnweightedNode<A,S>(fromNode, actionState);
                    }
                });
    }
}
