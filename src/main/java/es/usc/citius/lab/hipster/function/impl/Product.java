package es.usc.citius.lab.hipster.function.impl;

import es.usc.citius.lab.hipster.function.ScalarFunction;

public class Product implements ScalarFunction<Double> {

	public Double scale(Double a, double b) {
		return a*b;
	}

}
