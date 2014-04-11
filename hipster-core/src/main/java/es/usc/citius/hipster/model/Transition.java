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

public class Transition<A,S> {
    // Source state (origin) of the transition
    private S fromState;
    // Action applied to fromState
    private A action;
    // Resultant state after applying the action to fromState
    private S state;

    public Transition(A action, S state) {
        this.action = action;
        this.state = state;
    }

    public Transition(S fromState, A action, S toState) {
        this.fromState = fromState;
        this.action = action;
        this.state = toState;
    }

    public static <A,S> Transition<A,S> create(S fromState, A action, S toState){
        return new Transition<A, S>(fromState, action, toState);
    }

    public static <S> Transition<Void,S> create(S fromState, S toState){
        return new Transition<Void, S>(fromState, null, toState);
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

    public S getFromState() {
        return fromState;
    }
}
