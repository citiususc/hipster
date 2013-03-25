package es.usc.citius.composit.search.node;

public class NumericNode<S> extends AbstractSearchNode<S> implements ComparableNode<S> {

    protected final double cost; // g(n)
    protected final double score; // f(n) = g(n) + h(n)

    public NumericNode(Transition<S> state,
            NumericNode<S> previousNode, double cost, double score) {
        super(state, previousNode);
        this.cost = cost;
        this.score = score;
    }

    public double cost() {
        return this.cost;
    }

    public double score() {
        return this.score;
    }

    public int compareTo(ComparableNode<S> o) {
        NumericNode<S> node = (NumericNode<S>)o;
        return Double.compare(this.score, node.cost);
    }
}
