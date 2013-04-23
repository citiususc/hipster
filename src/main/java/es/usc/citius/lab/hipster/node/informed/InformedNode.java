package es.usc.citius.lab.hipster.node.informed;

import es.usc.citius.lab.hipster.node.AbstractNode;
import es.usc.citius.lab.hipster.node.Transition;

/**
 * 
 * @author Pablo Rodríguez Mier
 *
 * @param <S>
 * @param <T>
 */
public class InformedNode<S, T extends Comparable<T>> extends AbstractNode<S> implements HeuristicNode<S, T>, Comparable<HeuristicNode<S,T>> {

	private T cost;
	private T score;
	
	public InformedNode(Transition<S> transition, HeuristicNode<S,T> previousNode, T cost, T score) {
		super(transition, previousNode);
		this.cost = cost;
		this.score = score;
	}
	
	public InformedNode(Transition<S> transition, HeuristicNode<S,T> previousNode, T cost) {
		super(transition, previousNode);
		this.cost = cost;
		this.score = cost;
	}

	public T getCost() {
		return this.cost;
	}

	public T getScore() {
		return this.score;
	}

	public int compareTo(HeuristicNode<S, T> o) {
		return this.score.compareTo(o.getScore());
	}

}
