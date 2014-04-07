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


import java.util.LinkedList;
import java.util.List;

public class AbstractNode<A,S,N extends AbstractNode<A,S,N>> implements Node<A,S,N> {
    protected N previousNode;
    protected S state;
    protected A action;


    public AbstractNode(N previousNode, S state, A action) {
        this.previousNode = previousNode;
        this.state = state;
        this.action = action;
    }

    @Override
    public List<N> path() {
        LinkedList<N> path = new LinkedList<N>();
        N currentNode = (N) this;
        while(currentNode != null){
            path.add(currentNode);
            currentNode = currentNode.previousNode;
        }
        return path;
    }

    @Override
    public N previousNode() {
        return this.previousNode;
    }

    @Override
    public S state() {
        return state;
    }

    @Override
    public A action() {
        return action;
    }


}
