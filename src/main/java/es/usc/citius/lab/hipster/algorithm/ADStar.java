package es.usc.citius.lab.hipster.algorithm;

import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.node.ADStarNode;
import es.usc.citius.lab.hipster.node.Node;
import es.usc.citius.lab.hipster.node.NodeBuilder;
import es.usc.citius.lab.hipster.node.Transition;
import es.usc.citius.lab.hipster.util.Operable;
import java.util.HashMap;
import java.util.HashSet;
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
public class ADStar<S, T extends Operable<T>> implements Iterator<Node<S>> {

    private final ADStarNode<S, T> beginNode;
    private final ADStarNode<S, T> goalNode;
    private final TransitionFunction<S> successorFunction;
    private final TransitionFunction<S> predecessorFunction;
    private final NodeBuilder<S, ADStarNode<S, T>> builder;
    private final ADStarNodeUpdater<S, T> updater;
    private final Map<S, ADStarNode<S, T>> visited;
    private final Iterable<Transition<S>> transitionsChanged;
    private final S begin;
    private final S goal;
    private Map<S, ADStarNode<S, T>> open;
    private Map<S, ADStarNode<S, T>> closed;
    private Map<S, ADStarNode<S, T>> incons;
    private Queue<ADStarNode<S, T>> queue;

    public ADStar(S begin, S goal, TransitionFunction<S> successors, TransitionFunction<S> predecessors, NodeBuilder<S, ADStarNode<S, T>> builder, ADStarNodeUpdater<S, T> updater) {
        this.begin = begin;
        this.builder = builder;
        this.updater = updater;
        this.successorFunction = successors;
        this.predecessorFunction = predecessors;
        this.open = new HashMap<S, ADStarNode<S, T>>();
        this.closed = new HashMap<S, ADStarNode<S, T>>();
        this.incons = new HashMap<S, ADStarNode<S, T>>();
        this.queue = new PriorityQueue<ADStarNode<S, T>>();
        this.visited = new HashMap<S, ADStarNode<S, T>>();
        this.transitionsChanged = new HashSet<Transition<S>>();
        this.beginNode = this.builder.node(null, new Transition<S>(null, begin));
        this.goalNode = this.builder.node(this.beginNode, new Transition<S>(null, goal));

        /*Initialization step*/
        this.visited.put(begin, this.beginNode);
        this.visited.put(goal, this.goalNode);
        insertOpen(this.beginNode);
    }

    /**
     * Retrieves the most promising node from the open collection, or null if it
     * is empty.
     *
     * @return most promising node
     */
    private ADStarNode<S, T> takePromising() {
        while (!this.queue.isEmpty()) {
            ADStarNode<S, T> head = this.queue.peek();
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
    private void insertOpen(ADStarNode<S, T> node) {
        this.open.put(node.transition().to(), node);
        this.queue.offer(node);
    }

    /**
     * Updates the membership of the node to the algorithm queues.
     *
     * @param node instance of {@link ADStarNode}
     */
    private void update(ADStarNode<S, T> node) {
        S state = node.transition().to();
        if (node.getV().compareTo(node.getG()) > 0) {
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
     * 
     * @param state
     * @return 
     */
    private Map<Transition<S>, ADStarNode<S, T>> predecessorsMap(S state){
        //Map<Transition, Node> containing predecesors relations
        Map<Transition<S>, ADStarNode<S, T>> mapPredecessors = new HashMap<Transition<S>, ADStarNode<S, T>>();
        //Fill with non-null pairs of <Transition, Node>
        for (Transition<S> predecessor : this.predecessorFunction.from(state)) {
            ADStarNode<S, T> predecessorNode = this.visited.get(predecessor.to());
            if (predecessorNode != null) {
                mapPredecessors.put(predecessor, predecessorNode);
            }
        }
        return mapPredecessors;
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

    public Node<S> next() {
        //First node in OPEN retrieved, not removed
        ADStarNode<S, T> current = takePromising();
        S state = current.transition().to();
        if (this.goalNode.compareTo(current) > 0 || this.goalNode.getV().compareTo(this.goalNode.getG()) < 0) {
            //s removed from OPEN
            this.open.remove(state);
            //if v(s) > g(s)
            boolean consistent = current.getV().compareTo(current.getG()) > 0;
            if (consistent) {
                //v(s) = g(s)
                current.setV(current.getG());
                //closed = closed U current
                this.closed.put(state, current);
            } else {
                //v(s) = Infinity
                this.updater.setMaxV(current);
                update(current);
            }

            for (Transition<S> successor : this.successorFunction.from(state)) {
                //if s' not visited before: v(s')=g(s')=Infinity; bp(s')=null
                ADStarNode<S, T> successorNode = this.visited.get(successor.to());
                if (successorNode == null) {
                    successorNode = this.builder.node(current, successor);
                    this.visited.put(successor.to(), successorNode);
                }

                if (consistent) {
                    //if g(s') > g(s) + c(s, s')
                    //  bp(s') = s
                    //  g(s') = g(s) + c(s, s')
                    boolean doUpdate = this.updater.updateConsistent(successorNode, current, successor);
                    if (doUpdate) {
                        update(successorNode);
                    }
                } else {
                    //Generate 
                    if (successor.to().equals(state)) {
                        //  bp(s') = arg min s'' predecesor of s' such that (v(s'') + c(s'', s')) 
                        //  g(s') = v(bp(s')) + c(bp(s'), s'')
                        this.updater.updateInconsistent(successorNode, predecessorsMap(successor.to()));
                        update(successorNode);
                    }
                }
            }
        } else {
            // for all directed edges (u, v) with changed edge costs
            for (Transition<S> transition : this.transitionsChanged) {
                state = transition.to();
                //if v != start
                if (!state.equals(this.begin)) {
                    //if s' not visited before: v(s')=g(s')=Infinity; bp(s')=null
                    ADStarNode<S, T> node = this.visited.get(state);
                    if (node == null) {
                        node = this.builder.node(current, transition);
                        this.visited.put(state, node);
                    }
                    //  bp(v) = arg min s'' predecesor of v such that (v(s'') + c(s'', v)) 
                    //  g(v) = v(bp(v)) + c(bp(v), v)
                    this.updater.updateInconsistent(node, predecessorsMap(transition.to()));
                    update(node);
                }
            }
            //move states from INCONS to OPEN
            this.open.putAll(this.incons);
            //update the priorities for all s in OPEN according to key(s)
            this.queue.clear();
            for(ADStarNode<S, T> node : this.open.values()){
                this.queue.offer(node);
            }
            //closed = empty
            this.closed.clear();
        }
        return current;
    }

    /**
     * Method not supported.
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
