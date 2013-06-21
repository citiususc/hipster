package es.usc.citius.lab.hipster.algorithm.combinatorial;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;


/**
 * This class computes all combinations of sets that covers all of the possible
 * elements. Combinations of sets that does not provide new information are not
 * considered. The implementation of the class relies on BitSet class and
 * performs logic operations between bits. The generation of the combinations is
 * done using a Breadth First Search.
 * 
 * @author Pablo Rodríguez Mier <pablo.rodriguez.mier@usc.es>
 * 
 * @param <E>
 */
public class IterativeSetCover<E> implements Iterator<Set<Set<E>>> {

	// Ordered map (descending by key) with the available sets
	// ordered by size and mapped back to their bitset representation
	private TreeMap<Set<E>, BitSet> orderedSets;
	// Maps bitset with their set representation
	private Map<BitSet, Set<E>> bitsetMap;
	// List of subsets, ordered by size
	private List<BitSet> bitsetList;
	// Holds an ordered list with all possible elements
	private List<E> elements;
	// Bit size used to store the information of all elements
	private int size;

	// Iterator
	private Iterator<Set<Set<E>>> iterator;

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
				if (c.isEmpty()){
					return Collections.emptySet();
				}
				candidates.addAll(c);
			}
			for(BitSet c : candidates){
				candidateStates.add(new State(this, c));
			}
			candidates.clear();
			return candidateStates;
		}

		

	}

	public IterativeSetCover(Set<Set<E>> sets) {
		this.elements = elements(sets);
		this.bitsetList = new ArrayList<BitSet>();
		
		this.size = this.elements.size();
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

		// Initialize the values of the bitset list
		// (there are m sets)
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
		this.iterator = enumerate().iterator();
	}

	private Comparator<BitSet> bitComparator(final BitSet source) {
		return new Comparator<BitSet>() {

			public int compare(BitSet o1, BitSet o2) {
				// Check how many bits of source are changed
				BitSet original = new BitSet();
				original.or(source);
				// Test o1
				original.or(o1);
				Integer sizeO1 = original.size();
				// Reset original bitset
				original.xor(original);
				original.or(source);
				original.or(o2);
				Integer sizeO2 = original.size();
				if (sizeO1 > sizeO2) {
					return 1;
				}
				return -1;
			}
		};
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

	public Collection<Set<Set<E>>> enumerate() {
		Collection<Set<Set<E>>> solutions = new LinkedHashSet<Set<Set<E>>>();
		// Queue used for BFS
		Queue<State> queue = new LinkedList<State>();
		// Root element
		queue.add(new State());

		// Breadth-First-Search
		while (!queue.isEmpty()) {
			State next = queue.poll();
			// Check if some candidate is final
			for (State candidate : next.candidates()) {
				// Discard this candidate if dominated
				Set<Set<E>> candidateSets = candidate.stateSets();
				if (!isDominated(solutions, candidateSets)) {
					if (candidate.isFinal()) {
						// add it as a final
						solutions.add(candidateSets);
					} else {
						queue.add(candidate);
					}
				}
			}
		}
		return solutions;
	}

	public boolean hasNext() {
		return iterator.hasNext();
	}

	public Set<Set<E>> next() {
		return iterator.next();
	}

	public void remove() {
		iterator.remove();
	}

}
