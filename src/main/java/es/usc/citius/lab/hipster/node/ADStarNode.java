package es.usc.citius.lab.hipster.node;

/**
 * Search node for AD* implementation: requires the values g, rsh and the Key to
 * order the open queue.
 *
 * @author Adrián González Sieira
 * @since 27-03-2013
 * @version 1.0
 */
public class ADStarNode<S> extends AbstractNode<S> implements AStarNode<S> {

    private double g;
    private double v;
    private Key key;

    /**
     * Default constructor for this class, that requires the parent transition
     * and previous node.
     *
     * @param transition incoming transition
     * @param previousNode parent node
     */
    public ADStarNode(Transition<S> transition, Node<S> previousNode) {
        super(transition, previousNode);
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

    public double getG() {
        return g;
    }

    public double getV() {
        return v;
    }

    public void setG(double g) {
        this.g = g;
    }

    public void setV(double v) {
        this.v = v;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public void setPreviousNode(ADStarNode<S> previousNode) {
        this.previousNode = previousNode;
    }

    @Override
    public ADStarNode<S> previousNode() {
        return (ADStarNode<S>) previousNode;
    }

    /**
     * Compares {@link ADStarNode} instances attending to their {@link Key}
     * values.
     *
     * @param o other node instance
     * @return comparation result
     */
    public int compareTo(AStarNode<S> o) {
        ADStarNode<S> oCast = (ADStarNode<S>) o;
        return this.key.compareTo(oCast.key);
    }
}
