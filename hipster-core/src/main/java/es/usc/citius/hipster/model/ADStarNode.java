package es.usc.citius.hipster.model;

import es.usc.citius.hipster.model.function.ScalarFunction;
import es.usc.citius.hipster.model.function.impl.BinaryOperation;

/**
 * Implementation of {@link es.usc.citius.hipster.model.Node} to be used with the AD* algorithm, implemented in
 * {@link es.usc.citius.hipster.algorithm.ADStarForward}. AD* nodes are formed by two cost elements, G and V,
 * and a {@link es.usc.citius.hipster.model.ADStarNode.Key} which is used to order the nodes by priority
 * in the queues of the algorithm. This implementation extends {@link es.usc.citius.hipster.model.HeuristicNode}.
 *
 * @param <A> type of the actions
 * @param <S> type of the state
 * @param <C> type of the cost (must extend {@link java.lang.Comparable})
 * @param <N> node type
 *
 * @author Adrián González Sieira <<a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 */
public interface ADStarNode<A, S, C extends Comparable<C>, N extends ADStarNode<A,S,C,N>> extends HeuristicNode<A, S, C, N> {

    /**
     * @return G-cost of the node
     */
    public C getG();

    /**
     * @return V-cost (also RHS) of the node
     */
    public C getV();

    /**
     * @return determines if the nodes must be updated by a {@link es.usc.citius.hipster.model.function.impl.ADStarNodeUpdater}.
     */
    public boolean isDoUpdate();

    /**
     * @return determines if the node is in a consistent or inconsistent state based on the values of G and V
     */
    public boolean isConsistent();

    /**
     * @param g new value of G
     */
    public void setG(C g);

    /**
     * @param v new value of V
     */
    public void setV(C v);

    /**
     * @param update set a new value for the calculate flag of this node
     */
    public void setDoUpdate(boolean update);

    /**
     * @param key new key to compare the priority of the nodes
     */
    public void setKey(Key<C> key);

    /**
     * @return retrieves the current key of the node
     */
    public Key<C> getKey();

    /**
     * @param parent new parent of the node
     */
    public void setPreviousNode(N parent);

    /**
     * @param state state of this node
     */
    public void setState(S state);

    /**
     * @param action action between the parent and this node
     */
    public void setAction(A action);


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
            calculate(g, v, h, e, add, scale);
        }

        /**
         * Updates the value of the key. This is done for efficiency, to avoid
         * creating new instances.
         */
        public void update(C g, C v, C h, double e, BinaryOperation<C> add, ScalarFunction<C> scale){
            calculate(g, v, h, e, add, scale);
        }

        /**
         * Updates the value of the key
         */
        private void calculate(C g, C v, C h, double e, BinaryOperation<C> add, ScalarFunction<C> scale){
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

        /**
         * @return first value of the key
         */
        public C getFirst() {
            return first;
        }

        /**
         * @return second value of the key
         */
        public C getSecond() {
            return second;
        }
    }

}
