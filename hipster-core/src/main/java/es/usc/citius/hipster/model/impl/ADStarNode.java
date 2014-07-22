package es.usc.citius.hipster.model.impl;

import es.usc.citius.hipster.model.AbstractNode;
import es.usc.citius.hipster.model.HeuristicNode;
import es.usc.citius.hipster.model.Node;
import es.usc.citius.hipster.model.Transition;
import es.usc.citius.hipster.model.function.ScalarFunction;
import es.usc.citius.hipster.model.function.impl.BinaryOperation;

/**
 * Interface defining the basic operations for {@link es.usc.citius.hipster.model.Node} to be used with
 * {@link es.usc.citius.hipster.algorithm.ADStarForward}. Contains the declaration of the methods to retrieve
 * te cost elements of the node (G and V) and the definition of the {@link ADStarNode.Key}
 * to compare {@link ADStarNode} elements.
 *
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @since 1.0.0
 */
public class ADStarNode<A, S, C extends Comparable<C>> extends AbstractNode<A, S, ADStarNode<A, S, C>>
        implements HeuristicNode<A, S, C, ADStarNode<A, S, C>>, Comparable<ADStarNode<A, S, C>>{

    protected C g;
    protected C v;
    protected Key<C> key;

    /**
     * Default constructor for ADStarNode. Requires the transition used
     * to reach the new one and the previous node. The current cost (G),
     * score (V) and key to compare between ADStarNode instances are also required.
     *
     * @param transition incoming transition
     * @param previousNode parent node
     * @param g accumulated cost from begin
     * @param v score to goal
     * @param k key value evaluated over G and V
     */
    public ADStarNode(Transition<A, S> transition, ADStarNode<A, S, C> previousNode, C g, C v, Key<C> k) {
        super(previousNode, transition.getState(), transition.getAction());
        this.g = g;
        this.v = v;
        this.key = k;
    }

    /**
     * Cost from beginning state.
     *
     * @return object representing the current cost
     */
    public C getG() {
        return g;
    }

    /**
     * Score to goal given as heuristic.
     *
     * @return object representing the estimated cost to goal
     */
    public C getV() {
        return v;
    }

    public void setG(C g) {
        this.g = g;
    }

    public void setV(C v) {
        this.v = v;
    }

    public void setKey(Key<C> key) {
        this.key = key;
    }

    public void setPreviousNode(ADStarNode<A, S, C> parent){
        this.previousNode = parent;
    }

    public void setState(S state){
        this.state = state;
    }

    public void setAction(A action){
        this.action = action;
    }

    @Override
    public C getEstimation() {
        return v;
    }

    @Override
    public C getScore() {
        return key.first;
    }

    @Override
    public C getCost() {
        return g;
    }

    /**
     * Method to retrieve the parent ADStarNode, the same
     * retrieved by {@link es.usc.citius.hipster.model.Transition#getFromState()}.
     *
     * @return parent {@link es.usc.citius.hipster.model.impl.ADStarNode},
     */
    @SuppressWarnings("unchecked") //suppress warnings to return an ADStarNode instead of Node, which is the inherited return type from parent
    @Override
    public ADStarNode<A, S, C> previousNode() {
        return previousNode;
    }

    /**
     * Compares ADSTarNode instances attending to their {@link Key}
     * values.
     *
     * @param o ADStarNode instance
     * @return usual comparison value
     */
    public int compareTo(ADStarNode<A, S, C> o) {
        return this.key.compareTo(o.key);
    }


    /**
     * Inner class defining the key of the node, which depends on the values of G and V. The
     * key of the node is the comparison criterion for ADStarForward to order the open queue.
     */
    public static class Key<C extends Comparable<C>> implements Comparable<Key<C>> {

        private C first;
        private C second;

        /**
         * Constructor to calculate a the key to order the nodes in the Open
         * queue.
         *
         * @param g g value of the node
         * @param v v value of the node
         * @param h value of the heuristic
         * @param e inflation value
         */
        public Key(C g, C v, C h, double e, BinaryOperation<C> add, ScalarFunction<C> scale) {
            if (v.compareTo(g) >= 0) {
                this.first = add.apply(g, scale.scale(h, e)); //g + h*e
                this.second = g;
            } else {
                this.first = add.apply(v, h); //v + h
                this.second = v;
            }
        }

        /**
         * Instantiates a new Key given its first and second value instead of
         * calculating them.
         *
         * @param first first cost value
         * @param second second cost value
         */
        public Key(C first, C second){
            this.first = first;
            this.second = second;
        }

        /**
         * Compares by the first value and, if equal, by the second one.
         *
         * @param o other Key object
         * @return comparison result
         */
        public int compareTo(Key<C> o) {
            int firstCompare = this.first.compareTo(o.first);
            if (firstCompare == 0) {
                return this.second.compareTo(o.second);
            } else {
                return firstCompare;
            }
        }
    }
}
