package es.usc.citius.lab.hipster.function;

public class Operation<E extends Comparable<E>> implements Operator<E> {

	private E maxValue;
	private E identityValue;
	private Operator<E> op;
	
	public Operation(Operator<E> operation, E identityValue, E maxValue){
		this.maxValue = maxValue;
		this.identityValue = identityValue;
		this.op = operation;
	}
	
	public E apply(E a, E b) {
		return this.op.apply(a, b);
	}

	public E getMaxValue() {
		return maxValue;
	}

	public E getIdentityValue() {
		return identityValue;
	}

}
