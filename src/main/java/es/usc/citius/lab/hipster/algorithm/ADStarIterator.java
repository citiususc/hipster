package es.usc.citius.lab.hipster.algorithm;

import es.usc.citius.lab.hipster.function.CostFunction;
import es.usc.citius.lab.hipster.function.HeuristicFunction;
import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.node.ADStarNode;
import es.usc.citius.lab.hipster.node.ComparableNode;
import es.usc.citius.lab.hipster.node.NodeBuilder;
import es.usc.citius.lab.hipster.node.Transition;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Iterator to execute an AD* search algorithm.
 *
 * @author Adrián González Sieira
 * @param <S> class that defines the states
 * @since 26-03-2013
 * @version 1.0
 */
public class ADStarIterator<S> implements Iterator<ComparableNode<S>> {

    private final ADStarNode<S> beginNode;
    private final ADStarNode<S> goalNode;
    private final TransitionFunction<S> predecessorFunction;
    private final TransitionFunction<S> successorFunction;
    private final CostFunction<S, Double> costFunction;
    private final HeuristicFunction<S, Double> heuristicFunction;
    private final NodeBuilder<S, ADStarNode<S>> nodeBuilder;
    private Map<S, ADStarNode<S>> open;
    private Map<S, ADStarNode<S>> closed;
    private Map<S, ADStarNode<S>> incons;
    private Queue<ADStarNode<S>> queue;
    private Double epsilon = 1.0;

    public ADStarIterator(S begin, S goal, TransitionFunction<S> predecessors, TransitionFunction<S> successors, CostFunction<S, Double> costFunction, HeuristicFunction<S, Double> heuristic, NodeBuilder<S, ADStarNode<S>> builder) {
        this.nodeBuilder = builder;
        this.predecessorFunction = predecessors;
        this.successorFunction = successors;
        this.costFunction = costFunction;
        this.heuristicFunction = heuristic;
        this.open = new HashMap<S, ADStarNode<S>>();
        this.closed = new HashMap<S, ADStarNode<S>>();
        this.incons = new HashMap<S, ADStarNode<S>>();
        this.queue = new PriorityQueue<ADStarNode<S>>();
        this.beginNode = this.nodeBuilder.node(null, new Transition<S>(null, begin));
        this.goalNode = this.nodeBuilder.node(null, new Transition<S>(null, goal));

        /*Initialization step*/
        this.beginNode.setG(0);
        insertOpen(beginNode);
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
     * Inserts a node in the open queue.
     *
     * @param node instance of node to add
     */
    private void insertOpen(ADStarNode<S> node) {
        node.setKey(new ADStarNode.Key(node.getG(), node.getV(), this.heuristicFunction.estimate(node.transition().to()), this.epsilon));
        this.open.put(node.transition().to(), node);
        this.queue.offer(node);
    }

    /**
     * Updates the membership of the node to the algorithm queues.
     *
     * @param node instance of {@link ADStarNode}
     */
    private void update(ADStarNode<S> node) {
        S state = node.transition().to();
        if (Double.compare(node.getV(), node.getG()) != 0) {
            if (!this.closed.containsKey(state)) {
                this.open.put(state, node);
                this.queue.offer(node);
            } else {
                this.incons.put(state, node);
            }
        } else {
            this.open.remove(state);
            this.incons.remove(state);
        }
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

    public ComparableNode<S> next() {
        /*First node in queue is retrieved.*/
        ADStarNode<S> s = takePromising();
        if (this.goalNode.compareTo(s) > 0 || this.goalNode.getV() < this.goalNode.getG()) {
            /*Loop of ComputeOrImprovePath is true: Actions taken.*/
            /*Removes from Open the most promising node.*/
            this.open.remove(s.transition().to());
            if (s.getV() > s.getG()) {
                s.setV(s.getG());
                this.closed.put(s.transition().to(), s);
                for (Iterator<Transition<S>> it = this.successorFunction.from(s.transition().to()).iterator(); it.hasNext();) {
                    Transition<S> succesor = it.next();
                    ADStarNode<S> current = this.nodeBuilder.node(s, succesor);
                    if (current.getG() > s.getG() + this.costFunction.evaluate(current.transition())) {
                        current.setG(current.previousNode().getG() + this.costFunction.evaluate(succesor));
                        update(current);
                    }
                }
            } else {
                s.setV(Double.POSITIVE_INFINITY);
                update(s);
                for (Iterator<Transition<S>> it = this.successorFunction.from(s.transition().to()).iterator(); it.hasNext();) {
                    Transition<S> succesor = it.next();
                    ADStarNode<S> current = this.nodeBuilder.node(s, succesor);
                    if (current.previousNode().equals(s)) {
                        Double minValue = Double.POSITIVE_INFINITY;
                        ADStarNode<S> minPredecessorNode = null;
                        for (Iterator<Transition<S>> it2 = this.predecessorFunction.from(succesor.to()).iterator(); it2.hasNext();) {
                            Transition<S> predecessor = it2.next();
                            ADStarNode<S> predecessorNode = this.nodeBuilder.node(current, predecessor);
                            Double currentValue = predecessorNode.getV() + this.costFunction.evaluate(predecessor);
                            if (currentValue < minValue) {
                                minValue = currentValue;
                                minPredecessorNode = predecessorNode;
                            }
                        }
                        current.setPreviousNode(minPredecessorNode);
                        current.setG(current.previousNode().getV() + this.costFunction.evaluate(current.transition()));
                        update(current);
                    }
                }
            }

        } else {
            /*Executes the changed relations processing and Epsilon updating.*/
        }
        return s;
    }

    /**
     * Method not supported.
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
