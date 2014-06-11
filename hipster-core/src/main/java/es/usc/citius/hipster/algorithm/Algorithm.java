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


import com.google.common.base.Predicate;
import com.google.common.base.Stopwatch;
import es.usc.citius.hipster.model.Node;

import java.util.*;

public abstract class Algorithm<A,S,N extends Node<A,S,N>> implements Iterable<N> {


    /**
     * Holds information about the search process.
     */
    public final class SearchResult {
        Stopwatch stopwatch;
        int iterations;
        Collection<N> goalNodes;


        public SearchResult(N goalNode, int iterations, Stopwatch stopwatch) {
            this.goalNodes = Collections.singletonList(goalNode);
            this.iterations = iterations;
            this.stopwatch = stopwatch;
        }

        public SearchResult(Collection<N> goalNodes, int iterations, Stopwatch stopwatch) {
            this.goalNodes = goalNodes;
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
         * @return goal node.
         */
        public N getGoalNode() {
            return goalNodes.iterator().next();
        }

        public Collection<N> getGoalNodes() {
            return goalNodes;
        }

        public List<List<S>> getOptimalPaths() {
            List<List<S>> paths = new ArrayList<List<S>>(goalNodes.size());
            for(N goalNode : goalNodes){
                paths.add(recoverStatePath(goalNode));
            }

            return paths;
        }

        @Override
        public String toString() {
            final String ls = System.getProperty("line.separator");
            StringBuilder builder = new StringBuilder();
            builder.append("Total solutions: ").append(goalNodes.size()).append(ls);
            builder.append("Total time: ").append(getStopwatch().toString()).append(ls);
            builder.append("Total number of iterations: ").append(getIterations()).append(ls);
            // Take solutions
            int solution=1;
            for(N goalNode : goalNodes){
                builder.append("+ Solution ").append(solution).append(": ").append(ls);
                builder.append(" - States: ").append(ls);
                builder.append("\t").append(recoverStatePath(goalNode).toString()).append(ls);
                builder.append(" - Actions: ").append(ls);
                builder.append("\t").append(recoverActionPath(goalNode).toString()).append(ls);
                builder.append(" - Search information: ").append(ls);
                builder.append("\t").append(goalNode.toString());
                solution++;
            }
            return builder.toString();
        }
    }

    public interface SearchListener<N> {
        void handle(N node);
    }

    /**
     * Run the algorithm until the goal is found or no more states are
     * available.
     * @return SearchResult with the information of the search
     */
    public SearchResult search(final S goalState){
        return search(new Predicate<N>() {
            @Override
            public boolean apply(N n) {
                if (goalState != null) {
                    return n.state().equals(goalState);
                }
                return false;
            }
        });
    }


    public SearchResult search(Predicate<N> condition){
        int iteration = 0;
        Iterator<N> it = iterator();
        Stopwatch w = Stopwatch.createStarted();
        N currentNode = null;
        while(it.hasNext()){
            iteration++;
            currentNode = it.next();
            if (condition.apply(currentNode)) {
                break;
            }

        }
        w.stop();
        return new SearchResult(currentNode, iteration, w);
    }

    /**
     * Executes the search algorithm and invokes the method
     * {@link SearchListener#handle(Object)} passing the current
     * explored node to the listener.
     *
     * <pre>
     * {@code
     *    TODO;
     * }
     * </pre>
     *
     * @param listener listener used to receive the explored nodes.
     */
    public void search(SearchListener<N> listener){
        Iterator<N> it = iterator();
        while(it.hasNext()){
            listener.handle(it.next());
        }
    }

    public static <S, N extends Node<?,S,N>>  List<S> recoverStatePath(N node){
        List<S> states = new LinkedList<S>();
        for(N n : node.path()){
            states.add(n.state());
        }
        Collections.reverse(states);
        return states;
    }

    public static <A, N extends Node<A,?,N>>  List<A> recoverActionPath(N node){
        List<A> actions = new LinkedList<A>();
        for(N n : node.path()){
            if (n.action() != null) actions.add(n.action());
        }
        Collections.reverse(actions);
        return actions;
    }
}
