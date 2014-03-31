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


import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ActionState<A,S> {
    private A action;
    private S state;

    public ActionState(A action, S state) {
        this.action = action;
        this.state = state;
    }

    public A getAction() {
        return action;
    }

    public void setAction(A action) {
        this.action = action;
    }

    public S getState() {
        return state;
    }

    public void setState(S state) {
        this.state = state;
    }

    public static <A,S> Set<ActionState<A,S>> fromMap(Map<A, S> actionStates){
        Set<ActionState<A,S>> actionStatesSet = new HashSet<ActionState<A, S>>();
        for(Map.Entry<A,S> as : actionStates.entrySet()){
            actionStatesSet.add(new ActionState<A, S>(as.getKey(), as.getValue()));
        }
        return actionStatesSet;
    }
}
