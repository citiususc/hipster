package es.usc.citius.lab.hipster.algebra;

public class DoubleAdditionMonoidFactory implements MonoidFactory<DoubleMonoid> {

	
	public DoubleAdditionMonoidFactory(){

	}
	
	public DoubleMonoid getIdentityElem() {
		return new DoubleMonoid(0, new BinaryOperation<DoubleMonoid>() {
			public DoubleMonoid apply(DoubleMonoid a, DoubleMonoid b) {
				return new DoubleMonoid(a.value + b.value, this);
			}
		});
	}

	
}
