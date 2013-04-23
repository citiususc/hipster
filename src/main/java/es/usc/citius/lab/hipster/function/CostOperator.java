package es.usc.citius.lab.hipster.function;

public class CostOperator<E extends Comparable<E>> implements Operator<E> {

	private E maxElem;
	private E identityElem;
	private Operator<E> op;

	public CostOperator(Operator<E> operation, E identityElem, E maxElem) {
		this.maxElem = maxElem;
		this.identityElem = identityElem;
		this.op = operation;
	}

	public E apply(E a, E b) {
		return this.op.apply(a, b);
	}
	
	public E getMaxElem() {
		return maxElem;
	}

	public E getIdentityElem() {
		return identityElem;
	}

	public static CostOperator<Double> doubleAdditionOp() {
		return new CostOperator<Double>(new Operator<Double>() {
			public Double apply(Double a, Double b) {
				return a + b;
			}
		}, 0d, Double.POSITIVE_INFINITY);
	}

	public static CostOperator<Double> doubleMultiplicationOp() {
		return new CostOperator<Double>(new Operator<Double>() {

			public Double apply(Double a, Double b) {
				return a * b;
			}
		}, 1d, Double.POSITIVE_INFINITY);
	}
}
