package es.usc.citius.lab.hipster.algorithm;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.node.ADStarNode;
import es.usc.citius.lab.hipster.node.Node;
import es.usc.citius.lab.hipster.node.NodeBuilder;
import es.usc.citius.lab.hipster.node.ADStarNodeUpdater;
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
public class ADStar<S> implements Iterator<Node<S>> {

    private final ADStarNode<S> beginNode;
    private final ADStarNode<S> goalNode;
    private final TransitionFunction<S> successorFunction;
    private final TransitionFunction<S> predecessorFunction;
    private final NodeBuilder<S, ADStarNode<S>> builder;
    private final ADStarNodeUpdater<S, ADStarNode<S>> updater;
    private final Map<S, ADStarNode<S>> visited;
    private Map<S, ADStarNode<S>> open;
    private Map<S, ADStarNode<S>> closed;
    private Map<S, ADStarNode<S>> incons;
    private Queue<ADStarNode<S>> queue;

    public ADStar(S begin, S goal, TransitionFunction<S> successors, TransitionFunction<S> predecessors, NodeBuilder<S, ADStarNode<S>> builder, ADStarNodeUpdater<S, ADStarNode<S>> updater) {
        this.builder = builder;
        this.updater = updater;
        this.successorFunction = successors;
        this.predecessorFunction = predecessors;
        this.open = new HashMap<S, ADStarNode<S>>();
        this.closed = new HashMap<S, ADStarNode<S>>();
        this.incons = new HashMap<S, ADStarNode<S>>();
        this.queue = new PriorityQueue<ADStarNode<S>>();
        this.visited = new HashMap<S, ADStarNode<S>>();
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
        ADStarNode<S> current = takePromising();
        S state = current.transition().to();
        if (this.goalNode.compareTo(current) > 0 || this.goalNode.getV().compareTo(this.goalNode.getG()) < 0) {
            //s removed from OPEN
            this.open.remove(state);
            //if v(s) > g(s)
            boolean consistent = current.getV().compareTo(current.getG()) > 0;
            if(consistent){
                //v(s) = g(s)
                current.setV(current.getG());
                //closed = closed U current
                this.closed.put(state, current);
            }
            else{
                //v(s) = Infinity
                this.updater.setMaxV(current);
                update(current);
            }
            
            for(Transition<S> successor : this.successorFunction.from(state)){
                /*if s' not visited before: v(s')=g(s')=Infinity; bp(s')=null*/
                ADStarNode<S> successorNode = this.visited.get(successor.to());
                if (successorNode == null) {
                    successorNode = this.builder.node(current, successor);
                    this.visited.put(successor.to(), successorNode);
                }
                
                if(consistent){
                    //if g(s') > g(s) + c(s, s')
                    //  bp(s') = s
                    //  g(s') = g(s) + c(s, s')
                    boolean doUpdate = this.updater.updateConsistent(successorNode, current, successor);
                    if(doUpdate){
                        update(successorNode);
                    }
                }
                else{
                    //Generate 
                    if(successor.to().equals(state)){
                        //Map<Transition, Node> containing predecesors relations
                        Map<Transition<S>, ADStarNode<S>> mapPredecessors = new HashMap<Transition<S>, ADStarNode<S>>();
                        //Fill with non-null pairs of <Transition, Node>
                        for(Transition<S> predecessor : this.predecessorFunction.from(successor.to())){
                            ADStarNode<S> predecessorNode = this.visited.get(predecessor.to());
                            if(predecessorNode != null){
                                mapPredecessors.put(predecessor, predecessorNode);
                            }
                        }
                        //  bp(s') = arg min s'' predecesor of s' such that (v(s'') + c(s'', s')) 
                        //  g(s') = v(bp(s')) + c(bp(s', s''))
                        this.updater.updateInconsistent(successorNode, mapPredecessors);
                        update(successorNode);
                    }
                }
            }            
        } else {
            /*Executes the changed relations processing and Epsilon updating.*/
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
