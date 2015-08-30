package es.usc.citius.hipster.algorithm;

import es.usc.citius.hipster.model.HeuristicNode;
import es.usc.citius.hipster.model.function.NodeExpander;
import es.usc.citius.hipster.util.Iterators;

import java.util.*;


public class BSStar<A,S,C extends Comparable<C>,N extends HeuristicNode<A,S,C,N>> extends Algorithm<A,S,N> {

    private N initialNode;
    private N goalNode;
    private NodeExpander<A,S,N>[] expander;

    public BSStar(N initialNode, N goalNode, NodeExpander<A, S, N> forward, NodeExpander<A, S, N> backward) {
        this.initialNode = initialNode;
        this.goalNode = goalNode;
        this.expander = new NodeExpander[]{forward, backward};
    }

    @Override
    public java.util.Iterator<N> iterator() {
        return new Iterator();
    }

    public class Iterator extends Iterators.AbstractIterator<N> {
        private Queue<N>[] open = new Queue[]{new PriorityQueue<N>(), new PriorityQueue<N>()};
        private Map<S,N>[] closed = new Map[]{new HashMap<S,N>(), new HashMap<S,N>()};
        private int d = 0; // 0 = forwards, 1 = backwards
        N best = null;

        public Iterator() {
            this.open[0].add(initialNode);
            this.open[1].add(goalNode);
        }

        @Override
        protected N computeNext() {
            // Select the minimum from open in both directions
            N min = open[0].peek().compareTo(open[1].peek()) < 0 ? open[0].peek() : open[1].peek();
            if (best == null) best = min;
            if (min.compareTo(best) < 0) best = min;
            // Select the current queue with the minimum number of elements
            d = (open[0].size() < open[1].size()) ? 0 : 1;
            // Extract next lowest f node
            N current = open[d].poll();
            closed[d].put(current.state(), current);
            // If the cost of the current is greater than the best path so far, skip
            if (current.compareTo(best) >= 0) return computeNext();
            // if the current state has been reached by the other direction, a solution has been found
            if (closed[1-d].containsKey(current.state())) {
                // Update the best cost (minimum between the best so far and the sum of the forward + backward paths
                best = best; // TODO: a cost monoid is required to compute the aggregated value for best!
                // Pruning step: remove descendants of current node that appear in open[1-d] to avoid
                // recomputing again the same nodes
                return computeNext(); // continue
            }
            for(N succ : expander[d].expand(current)){
                if (succ.compareTo(best) >= 0) continue; // no improvement!
                // Add a new valid successor
                open[d].add(succ);
            }

            return current;
        }
    }

}
