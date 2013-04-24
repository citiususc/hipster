package es.usc.citius.lab.hipster.experimental.algebra;

import es.usc.citius.lab.hipster.function.BinaryOperation;

public class DoubleAdditionMonoidFactory implements MonoidFactory<DoubleMonoidElement> {

	
	public DoubleAdditionMonoidFactory(){

	}
	
	public DoubleMonoidElement identity() {
		return new DoubleMonoidElement(0, new BinaryOperation<DoubleMonoidElement>() {
			public DoubleMonoidElement apply(DoubleMonoidElement a, DoubleMonoidElement b) {
				return new DoubleMonoidElement(a.value + b.value, this);
			}
		});
	}

	
}
