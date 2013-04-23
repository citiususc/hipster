package es.usc.citius.lab.hipster.function;

public final class Operators {

	private Operators(){}
	
	public static Operator<Double> accumulator(){
		return new Operator<Double>() {
			public Double apply(Double a, Double b) {
				return a+b;
			}
		};
	}
	
	public static Operator<Double> scalator(){
		return new Operator<Double>() {
			public Double apply(Double a, Double b) {
				return a*b;
			}
		};
	}
	
	

}
