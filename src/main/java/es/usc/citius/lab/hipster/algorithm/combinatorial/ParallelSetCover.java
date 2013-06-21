package es.usc.citius.lab.hipster.algorithm.combinatorial;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

import es.usc.citius.lab.hipster.util.parallel.Parallel;



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
 * 
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
 * 
 * This problem is based on the SCP (Set Cover Problem) but it does not find the
 * combination with the minimum number of sets. Instead, it enumerates all
 * possible combinations that cover all elements discarding redundant solutions.
 * For more information read <a
 * href="http://en.wikipedia.org/wiki/Set_cover_problem"
 * >http://en.wikipedia.org/wiki/Set_cover_problem</a>
 * 
 * @author Pablo Rodríguez Mier <pablo.rodriguez.mier@usc.es>
 * 
 * @param <E>
 */
public class ParallelSetCover<E> implements Iterator<Set<Set<E>>> {

	// Ordered map (descending by key) with the available sets
	// ordered by size and mapped back to their bitset representation
	private final TreeMap<Set<E>, BitSet> orderedSets;
	// Maps bitset with their set representation
	private final Map<BitSet, Set<E>> bitsetMap;
	// List of subsets, ordered by size
	private final List<BitSet> bitsetList;
	// Holds an ordered list with all possible elements
	private final List<E> elements;
	// Bit size used to store the information of all elements
	private int size;
	// This collection stores the solutions found
	private List<Set<Set<E>>> solutions;
	// Queue used for BFS. A synchronized queue is not required
	private Queue<State> queue;
	// Next element index
	private int nextElementIndex = 0;
	private Set<Set<E>> nextElement = null;

	/**
	 * Class Result is used to store the information obtained for each thread
	 * while performing the BFS
	 * 
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
	 * 
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

	}

	public ParallelSetCover(Set<Set<E>> sets)
			throws InterruptedException, ExecutionException {

		this.solutions = new LinkedList<Set<Set<E>>>();
		// Obtain the elements from the sets
		this.elements = elements(sets);
		this.bitsetList = new ArrayList<BitSet>();
		this.size = this.elements.size();
		// Sort the collection of sets by the their size
		this.orderedSets = new TreeMap<Set<E>, BitSet>(
				new Comparator<Set<E>>() {
					public int compare(Set<E> o1, Set<E> o2) {
						if (o2.size() > o1.size()) {
							return 1;
						}
						return -1;
					}
				});

		this.bitsetMap = new HashMap<BitSet, Set<E>>();

		// Initialize the values of the bitset list (there are m sets)
		for (Set<E> set : sets) {
			// Create a set of n bits (1 bit per element)
			BitSet b = new BitSet(this.size);
			for (int j = 0; j < this.size; j++) {
				// b(j)=1 if the element j appears on set(i)
				b.set(j, set.contains(this.elements.get(j)));
			}
			this.orderedSets.put(set, b);
			this.bitsetMap.put(b, set);
		}

		// Insert ordered bitsets into a list
		for (Entry<Set<E>, BitSet> entry : this.orderedSets.entrySet()) {
			this.bitsetList.add(entry.getValue());
		}

		// Initialize the queue and put the first states to explore
		this.queue = new LinkedList<State>(new State().candidates());

	}

	/**
	 * Return BitSets with the bit at position = value
	 * 
	 * @param value
	 *            True (1) false (0)
	 */
	public Set<BitSet> findByColumn(Collection<BitSet> bitsets, int position,
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

	public Set<BitSet> findByColumn(int fromIndex, int bitPosition,
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

	public List<Integer> findBitIndex(BitSet bitset, boolean value) {
		List<Integer> indexes = new ArrayList<Integer>();
		for (int i = 0; i < this.size; i++) {
			if (bitset.get(i) == value) {
				indexes.add(i);
			}
		}
		return indexes;
	}

	// TODO: Optimize this function.
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

	/**
	 * Parallelized version of the Iterative Set Cover
	 * 
	 * @return Iterator with all combinations of sets
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public Collection<Set<Set<E>>> enumerate() throws InterruptedException,
			ExecutionException {

		return solutions;

	}

	public boolean hasNext() {

		// Breadth-First-Search, parallelized and synchronized by levels.
		// In each step, take all states in the same level, process them and
		// collect all new candidate states to process in the next step

		while (this.solutions.size() <= this.nextElementIndex
				&& !this.queue.isEmpty()) {

			Collection<Result> nextLevel = null;
			try {

				nextLevel = new Parallel.ForEach<State, Result>(queue)
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

			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}

			// Empty the queue
			queue.clear();

			// Take the results obtained by each thread
			for (Result result : nextLevel) {
				this.queue.addAll(result.candidates);
				this.solutions.addAll(result.solutions);
			}
		}
		// Get the next solution
		// TODO: Solutions cannot be removed!!!!!!
		if (this.nextElementIndex < solutions.size()) {
			this.nextElement = solutions.get(this.nextElementIndex);
			this.nextElementIndex++;
		} else {
			this.nextElement = null;
		}

		return this.nextElement != null;
	}

	public Set<Set<E>> next() {
		return this.nextElement;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

}
