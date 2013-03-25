package es.usc.citius.composit.search.algorithm;

import java.util.Iterator;
import java.util.Stack;

import es.usc.citius.composit.search.function.SuccessorFunction;
import es.usc.citius.composit.search.node.SimpleNode;
import es.usc.citius.composit.search.node.Successor;




public class DFSIterator<S> implements Iterator<SimpleNode<S>> {

	private Stack<SimpleNode<S>> stack = new Stack<SimpleNode<S>>();
	private SuccessorFunction<S> successors;
	
	// required: Successors function
	public DFSIterator(final S initialState, SuccessorFunction<S> successors) {
		stack.add(new SimpleNode<S>(new Successor<S>(null, initialState), null));
		this.successors = successors;
	}

	public boolean hasNext() {
		return !stack.isEmpty();
	}

	public SimpleNode<S> next() {
		SimpleNode<S> current = stack.pop();
		for(Successor<S> successor : this.successors.from(current.successor().state())){
			stack.add(new SimpleNode<S>(successor, current));
		}
		return current;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	

}
