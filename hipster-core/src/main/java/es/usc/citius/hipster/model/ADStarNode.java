package es.usc.citius.hipster.model;

import es.usc.citius.hipster.model.function.ScalarFunction;
import es.usc.citius.hipster.model.function.impl.BinaryOperation;

/**
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @since 29/07/2014
 */
public interface ADStarNode<A, S, C extends Comparable<C>, N extends ADStarNode<A,S,C,N>> extends HeuristicNode<A, S, C, N> {

    public C getG();

    public C getV();

    public boolean isDoUpdate();

    public boolean isConsistent();

    public void setG(C g);

    public void setV(C v);

    public void setDoUpdate(boolean update);

    public void setKey(Key<C> key);

    public void setPreviousNode(N parent);

    public void setState(S state);

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

        public C getFirst() {
            return first;
        }

        public C getSecond() {
            return second;
        }
    }

}
