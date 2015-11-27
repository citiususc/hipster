package es.usc.citius.hipster.model.function.impl;


import es.usc.citius.hipster.model.function.ScalarFunction;

/**
 * A scalar operation is an implementation of {@link ScalarFunction} that
 * also defines:
 * <ul>
 * 		<li>identity element (A*i = A)</li>
 * </ul>
 *
 * @param <E> element type
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 * @author Adrián González Sieira <<a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 */
public class ScalarOperation<E extends Comparable<E>> implements ScalarFunction<E> {

	private double identityElem;
	private ScalarFunction<E> op;

	/**
	 * Unique constructor for {@link ScalarOperation}, that takes the {@link ScalarFunction}
	 * applied and the identity element.
	 *
	 * @param operation operation definition
	 * @param identityElem identity
	 */
	public ScalarOperation(ScalarFunction<E> operation, double identityElem) {
		this.identityElem = identityElem;
		this.op = operation;
	}

	@Override
	public E scale(E a, double b) {
		return this.op.scale(a, b);
	}

	/**
	 * @return identity element
	 */
	public double getIdentityElem() {
		return identityElem;
	}

	/**
	 * Builds the scaling operation for Doubles, that is the multiplying
	 * operation for the factor.
	 *
	 * @return {@link ScalarOperation} for Double
	 */
	public static ScalarOperation<Double> doubleMultiplicationOp() {
		return new ScalarOperation<Double>(new ScalarFunction<Double>() {

			@Override
			public Double scale(Double a, double b) {
				return a * b;
			}

		}, 1d);
	}
	
}
