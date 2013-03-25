package es.usc.citius.composit.search.node;

public class SimpleNode<S> extends AbstractSearchNode<S, SimpleNode<S>> {

	public SimpleNode(Successor<S> state, SimpleNode<S> previousNode) {
		super(state, previousNode);
	}

}
