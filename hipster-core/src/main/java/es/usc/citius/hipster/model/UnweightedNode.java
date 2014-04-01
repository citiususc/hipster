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

package es.usc.citius.hipster.model;


public class UnweightedNode<A,S> extends AbstractNode<A,S,UnweightedNode<A,S>> {

    public UnweightedNode(UnweightedNode<A, S> previousNode, S state, A action) {
        super(previousNode, state, action);
    }

    public UnweightedNode(UnweightedNode<A, S> previousNode, ActionState<A, S> actionState) {
        super(previousNode, actionState.getState(), actionState.getAction());
    }
}
