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

package es.usc.citius.lab.hipster.algorithm.combinatorial;


import es.usc.citius.common.parallel.Parallel;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;


/**
 * This class computes all combinations of sets that covers all of the possible
 * elements. Combinations of sets that does not provide new information are not
 * considered. The implementation of the class relies on BitSet class and
 * performs logic operations between bits. The generation of the combinations is
 * done using a Parallelized Breadth-First-Search.<br>
 * <br>
 * <b>Example:</b><br>
 * Suppose you have the following sets: 1={a,c}, 2={a,b}, 3={c}, 4={d},
 * 5={a,b,d}. <br>
 * <p/>
 * Then, the goal is to find the whole combinations of sets that cover all
 * elements (a,b,c,d).
 * <ul>
 * <li>3 + 5 ({c} U {a,b,e}) it's a valid solution, which uses two sets (3 and
 * 5).</li>
 * <li>1 + 5 it's a valid solution, which uses two sets (1 and 5).</li>
 * <li>2 + 3 + 5 it's not a valid solution since there exist a solution which
 * takes only sets 3 and 5, thus set 2 is not required and does not provide new
 * information. This combination is redundant.</li>
 * </ul>
 * <p/>
 * This problem is based on the SCP (Set Cover Problem) but it just does not find the
 * combination with the minimum number of sets. Instead, it enumerates all
 * possible combinations that cover all elements discarding redundant solutions (sorted
 * by the size of the solution).
 *
 * For more information read <a
 * href="http://en.wikipedia.org/wiki/Set_cover_problem"
 * >http://en.wikipedia.org/wiki/Set_cover_problem</a>
 *
 * @param <E>
 * @author Pablo Rodríguez Mier <pablo.rodriguez.mier@usc.es>
 */
public class SetCoverIterator<E> implements Iterator<Set<Set<E>>> {



    // Maps bitset with their set representation
    private final Map<BitSet, Set<E>> bitsetMap = new HashMap<BitSet, Set<E>>();
    // List of subsets, ordered by size
    private final List<BitSet> bitsetList;
    // Holds an ordered list with all possible elements
    private List<E> elements;
    // Bit size used to store the information of all elements
    private int size;
    // Buffer queue with the non-consumed solutions
    private Queue<Set<Set<E>>> buffer = new LinkedList<Set<Set<E>>>();
    // List with all solutions. This list is required in order to test dominance
    private final List<Set<Set<E>>> solutions = new LinkedList<Set<Set<E>>>();
    // Queue used for BFS. A synchronized queue is not required
    private final Queue<State> queue = new LinkedList<State>();
    private Set<Set<E>> nextElement = null;
    // Use parallelization
    private boolean parallelized = false;

    /**
     * Class Result is used to store the information obtained for each thread
     * while performing the BFS
     */
    private class Result {
        Set<State> candidates;
        Collection<Set<Set<E>>> solutions;

        Result(Set<State> candidates, Collection<Set<Set<E>>> solutions) {
            this.candidates = candidates;
            this.solutions = solutions;
        }
    }

    /**
     * State class is used to represent a state node during the search. A state
     * is defined by a set of bits (statebits) that indicates which elements are
     * selected upon this state, a set of bits (selected) that indicates the
     * movement (the set selected) and the previous state.<br>
     * <br>
     * Example:<br>
     * from State1 (initial) to State2 selecting {a,d} <br>
     * Elements = {a,b,c,d} (4 bits)<br>
     * State1 = {previous=null, selected=0000, statebits=0000}<br>
     * State2 = {previous=State1, selected=1001 (a,d), statebits=1001 (0000 | 1001)}
     */
    private class State {
        State previous;
        BitSet selected;
        BitSet statebits;

        public State() {
            this.previous = null;
            this.selected = new BitSet(size);
            this.statebits = new BitSet(size);
        }

        public State(State previous, BitSet selected) {
            this.previous = previous;
            this.selected = selected;
            this.statebits = new BitSet(size);
            this.statebits.or(previous.statebits);
            this.statebits.or(selected);
        }

        boolean isFinal() {
            return this.statebits.cardinality() == size;
        }

        Set<Set<E>> stateSets() {
            Set<Set<E>> combination = new HashSet<Set<E>>();
            combination.add(bitsetMap.get(this.selected));
            State parent = previous;
            while (parent != null) {
                Set<E> set = bitsetMap.get(parent.selected);
                if (set != null && !set.isEmpty()) {
                    combination.add(set);
                }
                parent = parent.previous;
            }
            return combination;
        }

        Set<State> candidates() {
            Set<State> candidateStates = new HashSet<State>();
            Set<BitSet> candidates = new HashSet<BitSet>();
            // Now, select all those bitsets with a bigger index
            // which sets some bits of the current bitset
            int from = bitsetList.indexOf(this.selected);
            // If position = -1, start from 0
            from = (from < 0) ? 0 : from;
            List<Integer> zeros = findBitIndex(this.statebits, false);
            for (Integer i : zeros) {
                Set<BitSet> c = findByColumn(from, i, true);
                // If there is no candidate for the current bit
                // then this branch is not solvable
                if (c.isEmpty()) {
                    return Collections.emptySet();
                }
                candidates.addAll(c);
            }
            for (BitSet c : candidates) {
                candidateStates.add(new State(this, c));
            }
            candidates.clear();
            return candidateStates;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            State state = (State) o;

            if (previous != null ? !previous.equals(state.previous) : state.previous != null) return false;
            if (!selected.equals(state.selected)) return false;
            if (!statebits.equals(state.statebits)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = previous != null ? previous.hashCode() : 0;
            result = 31 * result + selected.hashCode();
            result = 31 * result + statebits.hashCode();
            return result;
        }
    }

    public SetCoverIterator(Set<Set<E>> sets) {
        // Ordered map (descending by key) with the available sets
        // ordered by size and mapped back to their bitset representation
        TreeMap<Set<E>, BitSet> orderedSets = new TreeMap<Set<E>, BitSet>(
                new Comparator<Set<E>>() {
                    public int compare(Set<E> o1, Set<E> o2) {
                        if (o2.size() > o1.size()) {
                            return 1;
                        }
                        return -1;
                    }
                });
        // Obtain the elements from the sets
        this.elements = elements(sets);
        this.size = this.elements.size();

        // Initialize the values of the bitset list (there are m sets)
        for (Set<E> set : sets) {
            // Create a set of n bits (1 bit per element)
            BitSet b = new BitSet(this.size);
            for (int j = 0; j < this.size; j++) {
                // b(j)=1 if the element j appears on set(i)
                b.set(j, set.contains(this.elements.get(j)));
            }
            orderedSets.put(set, b);
            this.bitsetMap.put(b, set);
        }
        // TODO; Make unmodifiable
        this.bitsetList = new ArrayList<BitSet>(orderedSets.size());
        // Insert ordered bitsets into a list
        while(!orderedSets.isEmpty()){
            this.bitsetList.add(orderedSets.pollFirstEntry().getValue());
        }
        // Initialize the queue and put the first states to explore
        this.queue.add(new State());
        //this.queue.addAll(new State().candidates());

    }

    /**
     * Return BitSets with the bit at position = value
     *
     * @param value True (1) false (0)
     */
    private Set<BitSet> findByColumn(Collection<BitSet> bitsets, int position,
                                     boolean value) {
        Set<BitSet> rows = new HashSet<BitSet>();
        // Find rows with ones in that position
        for (BitSet b : bitsets) {
            if (b.get(position) == value) {
                rows.add(b);
            }
        }
        return rows;
    }

    private Set<BitSet> findByColumn(int fromIndex, int bitPosition,
                                     boolean value) {
        Set<BitSet> rows = new HashSet<BitSet>();
        for (int i = fromIndex; i < this.bitsetList.size(); i++) {
            BitSet current = this.bitsetList.get(i);
            if (current.get(bitPosition) == value) {
                rows.add(current);
            }
        }
        return rows;
    }

    private List<Integer> findBitIndex(BitSet bitset, boolean value) {
        List<Integer> indexes = new ArrayList<Integer>();
        for (int i = 0; i < this.size; i++) {
            if (bitset.get(i) == value) {
                indexes.add(i);
            }
        }
        return indexes;
    }

    private boolean isDominated(Collection<Set<Set<E>>> solutions,
                                Set<Set<E>> candidate) {
        for (Set<Set<E>> solution : solutions) {
            // Check if this combination is worse. If a valid combination
            // has less length than the current combination, check if the
            // valid combination is a subset of combination.
            if (solution.size() < candidate.size()) {
                if (candidate.containsAll(solution)) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<E> elements(Collection<Set<E>> sets) {
        Set<E> universe = new HashSet<E>();
        for (Set<E> set : sets) {
            universe.addAll(set);
        }
        return new ArrayList<E>(universe);
    }

    private void compute(){
        // Start processing until queue is empty or there are solutions in the buffer
        while(!this.queue.isEmpty() && this.buffer.isEmpty()){
            // Process all elements in the queue without removing them
            Collection<Result> nextLevel = null;
            if (parallelized) {
                try {
                    // Breadth-First-Search, parallelized and synchronized by levels.
                    // In each step, take all states in the same level, process them and
                    // collect all new candidate states to process in the next step
                    nextLevel = parallelSearch();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } else {
                nextLevel = sequentialSearch();
            }
            // Elements in the queue were processed. Clear it
            queue.clear();
            // Take the results obtained
            for (Result result : nextLevel) {
                // Add the candidates for the next level to the queue
                this.queue.addAll(result.candidates);
                // Fill the buffer with the solutions
                this.buffer.addAll(result.solutions);
                this.solutions.addAll(result.solutions);
            }
        }
    }

    public Set<Set<E>> next() {
        // Check buffer
        if (!this.buffer.isEmpty()){
            return this.buffer.poll();
        } else {
            // Compute and return
            compute();
            // Can be null!
            return this.buffer.poll();
        }
    }

    public boolean hasNext() {
        // Check if the is a non consumed solution
        if (this.nextElement != null){
            return true;
        }
        // Check if there are more solutions in the buffer
        if (!this.buffer.isEmpty()){
            return true;
        }
        // At this point there are no solutions, we have to process the node queue
        // to find new solutions. If the queue is empty, process is over.
        if (this.queue.isEmpty()){
            return false;
        }
        // To answer the hasNext question, we have to check if there are more
        // solutions or not.
        compute();
        return !this.buffer.isEmpty();
    }

    private Collection<Result> sequentialSearch() {
        Collection<Result> results = new ArrayList<Result>(this.queue.size());
        for (State state : this.queue) {
            Set<State> candidates = new HashSet<State>();
            Collection<Set<Set<E>>> localSolutions = new HashSet<Set<Set<E>>>();
            for (State candidate : state.candidates()) {
                Set<Set<E>> candidateSets = candidate.stateSets();
                if (!isDominated(this.solutions, candidateSets)) {
                    if (candidate.isFinal()) {
                        localSolutions.add(candidateSets);
                    } else {
                        candidates.add(candidate);
                    }
                }
            }
            results.add(new Result(candidates, localSolutions));
        }
        return results;
    }

    private Collection<Result> parallelSearch() throws ExecutionException, InterruptedException {
        return new Parallel.ForEach<State, Result>(queue)
                .apply(new Parallel.F<State, Result>() {
                    public Result apply(State state) {
                        Set<State> candidates = new HashSet<State>();
                        Collection<Set<Set<E>>> localSolutions = new HashSet<Set<Set<E>>>();
                        for (State candidate : state.candidates()) {
                            Set<Set<E>> candidateSets = candidate
                                    .stateSets();
                            if (!isDominated(solutions, candidateSets)) {
                                if (candidate.isFinal()) {
                                    localSolutions.add(candidateSets);
                                } else {
                                    candidates.add(candidate);
                                }
                            }
                        }
                        return new Result(candidates, localSolutions);

                    }
                }).values();
    }



    public void remove() {
        throw new UnsupportedOperationException();
    }

    public boolean isParallelized() {
        return parallelized;
    }

    public void useParallelization(boolean parallelized) {
        this.parallelized = parallelized;
    }

}
