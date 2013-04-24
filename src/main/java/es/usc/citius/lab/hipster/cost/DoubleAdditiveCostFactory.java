package es.usc.citius.lab.hipster.cost;

import es.usc.citius.lab.hipster.function.BinaryOperation;

public class DoubleAdditiveCostFactory implements CostElementFactory<DoubleAdditiveCost> {

	private BinaryOperation<DoubleAdditiveCost> op;
	
	public DoubleAdditiveCostFactory(BinaryOperation<DoubleAdditiveCost> operation){
		this.op = operation;
	}
	
	public DoubleAdditiveCost identity() {
		return new DoubleAdditiveCost(0d,op);
	}

	public DoubleAdditiveCost max() {
		return new DoubleAdditiveCost(Double.POSITIVE_INFINITY, op);
	}

	

}
