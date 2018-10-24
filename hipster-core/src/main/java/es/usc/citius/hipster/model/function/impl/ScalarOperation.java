/**
 * Copyright (C) 2013-2018 Centro de Investigación en Tecnoloxías da Información (CITIUS) (http://citius.usc.es)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

			@Override
			public double div(Double a, Double b) {
				return a / b;
			}

		}, 1d);
	}

	@Override
	public double div(E a, E b) {
		return this.op.div(a, b);
	}
}
