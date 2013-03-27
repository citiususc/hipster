package es.usc.citius.lab.hipster.node;

/**
 * Concrete implementation of {@link AbstractNode} for nodes comparable by
 * numeric values.
 *
 * @author Pablo Rodríguez Mier <pablo.rodriguez.mier@usc.es>
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @param <S> class defining the state
 * @since 26/03/2013
 * @version 1.0
 */
public class NumericNode<S> extends AbstractNode<S> implements ComparableNode<S> {

    protected final double cost; // In A*: g(n)
    protected final double score; // In A*: f(n) = g(n) + h(n)

    /**
     * Default builder
     *
     * @param transition incoming transition to this node
     * @param previous incoming node
     * @param cost total cost value
     * @param score heuristic value
     */
    public NumericNode(Transition<S> transition,
            NumericNode<S> previous, double cost, double score) {
        super(transition, previous);
        this.cost = cost;
        this.score = score;
    }

    /**
     * Resturns the total cost for this node
     *
     * @return double value
     */
    public double cost() {
        return this.cost;
    }

    /**
     * Resturns the heuristic value for this node
     *
     * @return double value
     */
    public double score() {
        return this.score;
    }

    /**
     * Comparation implementation between instances of NumericNode.
     *
     * @param o NumericNode object
     * @return int result
     */
    public int compareTo(ComparableNode<S> o) {
        NumericNode<S> node = (NumericNode<S>) o;
        return Double.compare(this.score, node.score);
    }

    @Override
    public String toString() {
        return this.state.to().toString().concat(" (").concat(new Double(this.cost).toString()).concat(", ").concat(new Double(this.score).toString()).concat(")"); //To change body of generated methods, choose Tools | Templates.
    }
}
