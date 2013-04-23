package es.usc.citius.lab.hipster.function;

public class Product implements ScalarFunction<Double> {

	public Double scale(Double a, double b) {
		return a*b;
	}

}
