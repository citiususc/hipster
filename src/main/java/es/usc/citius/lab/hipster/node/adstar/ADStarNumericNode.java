package es.usc.citius.lab.hipster.node.adstar;

import es.usc.citius.lab.hipster.node.AbstractNode;
import es.usc.citius.lab.hipster.node.Node;
import es.usc.citius.lab.hipster.node.Transition;

/**
 * Search node for AD* implementation: requires the values g, rsh and the Key to
 * order the open queue.
 *
 * @author Adrián González Sieira
 * @since 27-03-2013
 * @version 1.0
 */
public class ADStarNumericNode<S> extends AbstractNode<S> implements Comparable<ADStarNumericNode<S>> {

    protected Double g;
    protected Double v;
    protected Key key;

    /**
     * Default constructor for this class, that requires the parent transition
     * and previous node.
     *
     * @param transition incoming transition
     * @param previousNode parent node
     */
    public ADStarNumericNode(Transition<S> transition, Node<S> previousNode, Double g, Double v, Key k) {
        super(transition, previousNode);
        this.g = g;
        this.v = v;
        this.key = k;
    }

    /**
     * Compares {@link ADStarNode} instances attending to their {@link Key}
     * values.
     *
     * @param o other node instance
     * @return comparation result
     */
    public int compareTo(ADStarNumericNode<S> o) {
        return this.key.compareTo(o.key);
    }

    /**
     * Class defining the key of the state, used to order them
     */
    public static class Key implements Comparable<Key> {

        private double first;
        private double second;

        /**
         * Constructor to calculate a the key to order the nodes in the Open
         * queue.
         *
         * @param g g value of the node
         * @param v v value of the node
         * @param h
         * @param e
         */
        public Key(double g, double v, double h, double e) {
            if (v >= g) {
                this.first = g + e * h;
                this.second = g;
            } else {
                this.first = v + h;
                this.second = v;
            }
        }
        
        public Key(double first, double second){
            this.first = first;
            this.second = second;
        }

        /**
         * Compares the first value and, if equal, the second one.
         *
         * @param o other Key object
         * @return comparation result
         */
        public int compareTo(Key o) {
            int firstCompare = Double.compare(this.first, o.first);
            if (firstCompare == 0) {
                return Double.compare(this.second, o.second);
            } else {
                return firstCompare;
            }
        }
    }

    public void setG(Double g) {
        this.g = g;
    }

    public void setV(Double v) {
        this.v = v;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public Double getG() {
        return g;
    }

    public Double getV() {
        return v;
    }

    @Override
    public ADStarNumericNode<S> previousNode() {
        return (ADStarNumericNode<S>) previousNode;
    }
    
    
}
