/*
 * Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS), University of Santiago de Compostela (USC).
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

package es.usc.citius.lab.hipster.function.impl;


import es.usc.citius.lab.hipster.function.BinaryOperation;

public class CostOperator<E extends Comparable<E>> implements BinaryOperation<E> {

	private E maxElem;
	private E identityElem;
	private BinaryOperation<E> op;

	public CostOperator(BinaryOperation<E> operation, E identityElem, E maxElem) {
		this.maxElem = maxElem;
		this.identityElem = identityElem;
		this.op = operation;
	}

	public E apply(E a, E b) {
		return this.op.apply(a, b);
	}
	
	public E getMaxElem() {
		return maxElem;
	}

	public E getIdentityElem() {
		return identityElem;
	}

	public static CostOperator<Double> doubleAdditionOp() {
		return new CostOperator<Double>(new BinaryOperation<Double>() {
			public Double apply(Double a, Double b) {
				return a + b;
			}
		}, 0d, Double.POSITIVE_INFINITY);
	}

	public static CostOperator<Double> doubleMultiplicationOp() {
		return new CostOperator<Double>(new BinaryOperation<Double>() {

			public Double apply(Double a, Double b) {
				return a * b;
			}
		}, 1d, Double.POSITIVE_INFINITY);
	}
}
