package es.usc.citius.hipster.algorithm;

import es.usc.citius.hipster.model.HeuristicNode;
import es.usc.citius.hipster.model.function.NodeExpander;
import es.usc.citius.hipster.model.function.impl.BinaryOperation;
import es.usc.citius.hipster.util.Iterators;

import java.util.*;


public class BSStar<A,S,C extends Comparable<C>,N extends HeuristicNode<A,S,C,N>> extends Algorithm<A,S,N> {

    private N initialNode;
    private N goalNode;
    private NodeExpander<A,S,N>[] expander;
    private BinaryOperation<C> binaryOperation;

    public BSStar(N initialNode, N goalNode, NodeExpander<A, S, N> forward, NodeExpander<A, S, N> backward, BinaryOperation<C> binaryOperation) {
        this.initialNode = initialNode;
        this.goalNode = goalNode;
        this.expander = new NodeExpander[]{forward, backward};
        this.binaryOperation = binaryOperation;
    }

    @Override
    public java.util.Iterator<N> iterator() {
        return new Iterator();
    }

    public class Iterator extends Iterators.AbstractIterator<N> {
        private Queue<N>[] open = new Queue[]{new PriorityQueue<N>(), new PriorityQueue<N>()};
        private Map<S,N>[] closed = new Map[]{new HashMap<S,N>(), new HashMap<S,N>()};
        private int d = 0; // 0 = forwards, 1 = backwards
        private C bestCost = binaryOperation.getMaxElem();

        public Iterator() {
            this.open[0].add(initialNode);
            this.open[1].add(goalNode);
        }

        @Override
        protected N computeNext() {
            // Select the minimum from open in both directions
            //N min = open[0].peek().compareTo(open[1].peek()) < 0 ? open[0].peek() : open[1].peek();
            //if (bestCost == null || min.getCost().compareTo(bestCost) < 0) bestCost = min.getCost();
            // Select the current queue with the minimum number of elements
            d = (open[0].size() < open[1].size()) ? 0 : 1;
            // Extract next lowest f node
            N current = open[d].poll();
            closed[d].put(current.state(), current);
            // If the cost of the current is greater than the best path so far, skip
            if (current.getScore().compareTo(bestCost) >= 0) return computeNext();
            // if the current state has been reached by the other direction, a solution has been found
            if (closed[1-d].containsKey(current.state())) {
                // Update the best cost (minimum between the best so far and the sum of the forward + backward paths
                N opposite = closed[1 - d].get(current.state());
                // Compute the cost of the path
                C pathCost = binaryOperation.apply(current.getCost(), opposite.getCost());
                // Update threshold
                bestCost = pathCost.compareTo(bestCost) < 0 ? pathCost : bestCost;
                // Pruning step: remove descendants of current node that appear in open[1-d] to avoid
                // recomputing again the same nodes
                return computeNext(); // continue
            }
            for(N succ : expander[d].expand(current)){
                if (succ.getScore().compareTo(bestCost) >= 0) continue; // no improvement!
                // Add a new valid successor
                open[d].add(succ);
            }

            return current;
        }
    }

}
