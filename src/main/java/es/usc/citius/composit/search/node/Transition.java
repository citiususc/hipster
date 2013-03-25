package es.usc.citius.composit.search.node;

import java.util.LinkedList;
import java.util.List;

public class Transition<S> {

	//
	private final S from;
	// State
	private final S state;
	
	public Transition(S from, S to){
		this.from = from;
		this.state = to;
	}
	
	public Transition(Transition<S> successor){
		this.from = successor.from;
		this.state = successor.state;
	}
	
	public Transition(S to){
		this(null, to);
	}

	public S from() {
		return from;
	}

	public S state() {
		return state;
	}
	
	public static <S> Iterable<Transition<S>> map(final S fromState, final Iterable<S> toStates){
		List<Transition<S>> successors = new LinkedList<Transition<S>>();
		for(S state : toStates){
			successors.add(new Transition<S>(fromState, state));
		}
		return successors;
	}

}
