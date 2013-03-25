package es.usc.citius.composit.search.node;

import java.util.LinkedList;
import java.util.List;

public class Successor<S> {

	//
	private final S from;
	// State
	private final S state;
	
	public Successor(S from, S to){
		this.from = from;
		this.state = to;
	}
	
	public Successor(Successor<S> successor){
		this.from = successor.from;
		this.state = successor.state;
	}
	
	public Successor(S to){
		this(null, to);
	}

	public S from() {
		return from;
	}

	public S state() {
		return state;
	}
	
	public static <S> Iterable<Successor<S>> map(final S fromState, final Iterable<S> toStates){
		List<Successor<S>> successors = new LinkedList<Successor<S>>();
		for(S state : toStates){
			successors.add(new Successor<S>(fromState, state));
		}
		return successors;
	}

}
