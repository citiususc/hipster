package es.usc.citius.lab.hipster.algorithm;

import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.node.ADStarNode;
import es.usc.citius.lab.hipster.node.ComparableNode;
import es.usc.citius.lab.hipster.node.NodeBuilder;
import es.usc.citius.lab.hipster.node.Transition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;

/**
 * Iterator to execute an AD* search algorithm.
 *
 * @author Adrián González Sieira
 * @param <S> class that defines the states
 * @since 26-03-2013
 * @version 1.0
 */
public class ADStarIterator<S> implements Iterator<ADStarNode<S>> {

    private final ADStarNode<S> beginNode;
    private final Iterable<S> goalStates;
    private final TransitionFunction<S> predecessorFunction;
    private final TransitionFunction<S> successorFunction;
    private Map<S, ADStarNode<S>> open;
    private Map<S, ADStarNode<S>> closed;
    private Map<S, ADStarNode<S>> incons;
    private Queue<ADStarNode<S>> queue;
    private NodeBuilder<S, ADStarNode<S>> nodeBuilder;

    public ADStarIterator(S begin, Iterable<S> goals, TransitionFunction<S> predecessors, TransitionFunction<S> successors) {
        this.beginNode = this.nodeBuilder.node(null, new Transition<S>(null, begin));
        this.goalStates = goals;
        this.predecessorFunction = predecessors;
        this.successorFunction = successors;

        /*Initialization step*/
        Collection<ComparableNode<S>> goalNodes = new ArrayList<ComparableNode<S>>();
        for (Iterator<S> it = goals.iterator(); it.hasNext();) {
            S currentGoal = it.next();
            ADStarNode<S> currentGoalNode = this.nodeBuilder.node(null, new Transition<S>(null, currentGoal));
            goalNodes.add(currentGoalNode);
        }
    }

    /**
     * Retrieves the most promising node from the open collection, or null if it
     * is empty.
     *
     * @return most promising node
     */
    private ADStarNode<S> takePromising() {
        while (!this.queue.isEmpty()) {
            ADStarNode<S> head = this.queue.peek();
            if (!this.open.containsKey(head.transition().to())) {
                this.queue.poll();
            } else {
                return head;
            }
        }
        return null;
    }

    /**
     * As the algorithm is executed iteratively refreshing the changed relations
     * between nodes, this method will return always true.
     *
     * @return always true
     */
    public boolean hasNext() {
        return takePromising() != null;
    }

    public ADStarNode<S> next() {
        ADStarNode<S> mostPromising = takePromising();
        /*Loop of ComputeOrImprovePath is true: Actions taken.*/
        if (mostPromising.compareTo(this.beginNode) < 0 || Double.compare(this.beginNode.getRhs(), this.beginNode.getG()) != 0) {
        } /*Executes the changed relations processing and Epsilon updating.*/ else {
        }
    }

    /**
     * Method not supported.
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
