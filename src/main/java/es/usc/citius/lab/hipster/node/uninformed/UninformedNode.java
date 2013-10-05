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
package es.usc.citius.lab.hipster.node.uninformed;

import es.usc.citius.lab.hipster.node.AbstractNode;
import es.usc.citius.lab.hipster.node.Transition;
import es.usc.citius.lab.hipster.node.informed.CostNode;

public class UninformedNode<S, T extends Comparable<T>> extends AbstractNode<S> implements CostNode<S, T>, Comparable<CostNode<S,T>> {

	private T cost;

	public UninformedNode(Transition<S> transition, CostNode<S,T> previousNode, T cost) {
		super(transition, previousNode);
		this.cost = cost;
	}

	public T getCost() {
		return this.cost;
	}

	
	public int compareTo(CostNode<S, T> o) {
		return this.cost.compareTo(o.getCost());
	}

}