package es.usc.citius.lab.hipster.experimental.algebra;

import es.usc.citius.lab.hipster.function.BinaryOperation;

public class DoubleMonoidElement implements MonoidElement<DoubleMonoidElement>, Comparable<DoubleMonoidElement> {

	protected double value;
	private BinaryOperation<DoubleMonoidElement> op;
	
	public DoubleMonoidElement(double value, BinaryOperation<DoubleMonoidElement> op){
		this.value = value;
		this.op = op;
	}
	
	public DoubleMonoidElement operate(DoubleMonoidElement e) {
		return op.apply(this, e);
	}

	public int compareTo(DoubleMonoidElement o) {
		return Double.compare(value, o.value);
	}
	
	

}
