package es.usc.citius.lab.hipster.node;

public class UninformedNode<S, T extends Comparable<T>> extends AbstractNode<S> implements CostNode<S, T>, Comparable<CostNode<S,T>> {

	private T cost;

	public UninformedNode(Transition<S> transition, CostNode<S,T> previousNode, T cost) {
		super(transition, previousNode);
		this.cost = cost;
	}

	public T getCost() {
		return this.cost;
	}

	
	public int compareTo(CostNode<S, T> o) {
		return this.cost.compareTo(o.getCost());
	}

}
