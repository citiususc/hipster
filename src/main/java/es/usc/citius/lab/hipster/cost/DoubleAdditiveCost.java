package es.usc.citius.lab.hipster.cost;

import es.usc.citius.lab.hipster.function.BinaryOperation;

public class DoubleAdditiveCost implements CostElement<DoubleAdditiveCost> {

	private double value;
	private BinaryOperation<DoubleAdditiveCost> op;
	
	public DoubleAdditiveCost(double value, BinaryOperation<DoubleAdditiveCost> operation){
		this.value = value;
		this.op = operation;
	}
	public int compareTo(DoubleAdditiveCost o) {
		return Double.compare(value, o.value);
	}

	public DoubleAdditiveCost x(DoubleAdditiveCost e) {
		return op.apply(this, e);
	}


}
