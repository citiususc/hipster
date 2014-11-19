/*
 * Copyright 2014 CITIUS <http://citius.usc.es>, University of Santiago de Compostela.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package es.usc.citius.hipster.model.function.impl;

import es.usc.citius.hipster.model.function.BinaryFunction;

/**
 * A implementation of {@link es.usc.citius.hipster.model.function.BinaryFunction} used to define
 * a custom cost algebra that also has the following definitions:
 * <ul>
 * 		<li>identity element (A*I = A)</li>
 * 		<li>maximum element (A*M = M)</li>
 * </ul>
 *
 * @param <E> element type
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 * @author Adrián González Sieira <<a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 */
public class BinaryOperation<E> implements BinaryFunction<E> {

	private E maxElem;
	private E identityElem;
	private BinaryFunction<E> op;

	/**
	 *
	 *
	 * @param operation
	 * @param identityElem
	 * @param maxElem
	 */
	public BinaryOperation(BinaryFunction<E> operation, E identityElem, E maxElem) {
        // Check properties
        assert operation.apply(identityElem, maxElem).equals(maxElem);
        assert operation.apply(maxElem, identityElem).equals(maxElem);
        assert operation.apply(identityElem, identityElem).equals(identityElem);
        //Preconditions.checkArgument(operation.apply(identityElem, maxElem).equals(maxElem), "Property error: I x A != A");
        //Preconditions.checkArgument(operation.apply(maxElem, identityElem).equals(maxElem), "Property error: A x I != A");
        //Preconditions.checkArgument(operation.apply(identityElem, identityElem).equals(identityElem), "Property error: I x I != I");
		this.maxElem = maxElem;
		this.identityElem = identityElem;
		this.op = operation;
	}

	@Override
	public E apply(E a, E b) {
		return this.op.apply(a, b);
	}

	/**
	 * @return maximum cost
	 */
	public E getMaxElem() {
		return maxElem;
	}

	/**
	 * @return minimum (identity) cost
	 */
	public E getIdentityElem() {
		return identityElem;
	}

    /**
     * @return a default addition implementation which works with doubles.
	 * For example, {@literal BinaryOperation.doubleAdditionOp().apply(2.5d, 1.0d)} returns 3.5d.
     */
	public static BinaryOperation<Double> doubleAdditionOp() {
		return new BinaryOperation<Double>(new BinaryFunction<Double>() {
			public Double apply(Double a, Double b) {
				return a + b;
			}
		}, 0d, Double.POSITIVE_INFINITY);
	}

    /**
     * @return a multiplication implementation which works with doubles.
     * For example, {@literal BinaryOperation.doubleMultiplicationOp.apply(2.5d, 2.0d)} returns 5.0d.
     */
	public static BinaryOperation<Double> doubleMultiplicationOp() {
		return new BinaryOperation<Double>(new BinaryFunction<Double>() {

			public Double apply(Double a, Double b) {
				return a * b;
			}
		}, 1d, Double.POSITIVE_INFINITY);
	}
}
