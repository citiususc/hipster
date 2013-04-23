package es.usc.citius.lab.hipster.algebra;

public class DoubleMonoid implements Monoid<DoubleMonoid> {

	protected double value;
	private BinaryOperation<DoubleMonoid> op;
	
	public DoubleMonoid(double value, BinaryOperation<DoubleMonoid> op){
		this.value = value;
		this.op = op;
	}
	
	public DoubleMonoid operate(DoubleMonoid e) {
		return op.apply(this, e);
	}

	public int compareTo(DoubleMonoid o) {
		return Double.compare(value, o.value);
	}
	
	

}
