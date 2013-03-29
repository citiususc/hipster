package es.usc.citius.lab.hipster.algorithm;

import java.util.Iterator;
import java.util.Stack;

import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.node.Node;
import es.usc.citius.lab.hipster.node.SimpleNode;
import es.usc.citius.lab.hipster.node.Transition;




public class DepthFirstSearch<S> implements Iterator<Node<S>> {

	private Stack<Node<S>> stack = new Stack<Node<S>>();
	private TransitionFunction<S> successors;
	
	// required: Successors function
	public DepthFirstSearch(final S initialState, TransitionFunction<S> successors) {
		stack.add(new SimpleNode<S>(new Transition<S>(null, initialState), null));
		this.successors = successors;
	}

	public boolean hasNext() {
		return !stack.isEmpty();
	}

	public Node<S> next() {
		Node<S> current = stack.pop();
		for(Transition<S> successor : this.successors.from(current.transition().to())){
			stack.add(new SimpleNode<S>(successor, current));
		}
		return current;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	

}
