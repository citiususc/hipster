package es.usc.citius.lab.hipster.algorithm;

import java.util.Iterator;
import java.util.Stack;

import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.composit.search.node.SimpleNode;
import es.usc.citius.lab.hipster.node.Transition;




public class DFSIterator<S> implements Iterator<SimpleNode<S>> {

	private Stack<SimpleNode<S>> stack = new Stack<SimpleNode<S>>();
	private TransitionFunction<S> successors;
	
	// required: Successors function
	public DFSIterator(final S initialState, TransitionFunction<S> successors) {
		stack.add(new SimpleNode<S>(new Transition<S>(null, initialState), null));
		this.successors = successors;
	}

	public boolean hasNext() {
		return !stack.isEmpty();
	}

	public SimpleNode<S> next() {
		SimpleNode<S> current = stack.pop();
		for(Transition<S> successor : this.successors.from(current.successor().state())){
			stack.add(new SimpleNode<S>(successor, current));
		}
		return current;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	

}
