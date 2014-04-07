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

package es.usc.citius.hipster.model.problem;

import es.usc.citius.hipster.model.function.*;
import es.usc.citius.hipster.model.function.impl.DefaultActionStateTransitionFunction;

public class Problem {

    private Problem(){ }

    public static class InternalNoActionStep1 {

        private InternalNoActionStep1(){}

        public <S> InternalNoActionStep2<S> withStates(Class<S> stateClass){
            return new InternalNoActionStep2<S>(stateClass);
        }
    }

    public static class InternalNoActionStep2<S> {
        private Class<S> stateClass;

        public InternalNoActionStep2(Class<S> stateClass) {
            this.stateClass = stateClass;
        }

        public InternalNoActionStep3<S> setStateTransitionFunction(StateTransitionFunction<S> transitionFunction){
            return new InternalNoActionStep3<S>(transitionFunction);
        }

    }

    public static class InternalNoActionStep3<S> {

        private final StateTransitionFunction<S> transitionFunction;

        public InternalNoActionStep3(StateTransitionFunction<S> transitionFunction) {
            this.transitionFunction = transitionFunction;
        }

        public InternalNoActionStep4<S> setCostFunction(CostFunction<Void,S,Double> costFunction){
            return new InternalNoActionStep4<S>(transitionFunction, costFunction);
        }

        public SearchProblem<Void,S> build(){
            return new SearchProblem<Void, S>() {
                @Override
                public TransitionFunction<Void, S> getTransitionFunction() {
                    //return new DefaultActionStateTransitionFunction<Void, S>(null, transitionFunction);
                    return null;
                }

                @Override
                public S getInitialState() {
                    return null;
                }

                @Override
                public S getGoalState() {
                    return null;
                }
            };
        }

    }

    public static class InternalNoActionStep4<S> {

        public InternalNoActionStep4(StateTransitionFunction<S> transitionFunction, CostFunction<Void, S, Double> costFunction) {
        }

        public InternalNoActionStep5<S> setHeuristicFunction(){
            return new InternalNoActionStep5<S>();
        }

        public InformedSearchProblem<Void,S,Double> build(){
            return null;
        }

    }

    public static class InternalNoActionStep5<S> {

        public InternalNoActionStep5() {
        }

        public HeuristicSearchProblem<Void,S,Double> build(){
            return null;
        }

    }


    public static class InternalWithActionStep1<A> {
        private Class<A> actionClass;

        private InternalWithActionStep1(Class<A> actionClass) {
            this.actionClass = actionClass;
        }

        public <S> InternalWithActionStep3<A,S> withStates(Class<S> stateClass){
            return new InternalWithActionStep3<A, S>(actionClass, stateClass);
        }

    }

    public static class InternalWithActionStep3<A,S> {
        //private Step previousStep;
        private Class<A> actionClass;
        private Class<S> stateClass;

        public InternalWithActionStep3(Class<A> actionClass, Class<S> stateClass) {
            this.actionClass = actionClass;
            this.stateClass = stateClass;
        }

        public InternalWithActionStep4<A,S> setActionFunction(ActionFunction<A,S> actionFunction){
            return new InternalWithActionStep4<A, S>();
        }
    }

    public static class InternalWithActionStep4<A,S> {
        public InternalWithActionStep5<A,S> setTransitionFunction(ActionStateTransitionFunction<A,S> transitionFunction){
            //return this;
            return new InternalWithActionStep5<A,S>();
        }

    }

    public static class InternalWithActionStep5<A,S> {

        public InternalWithActionStep6<A,S> setCostFunction(){
            return new InternalWithActionStep6<A, S>();
        }

        public SearchProblem<A,S> build(){
            return null;
        }

    }

    public static class InternalWithActionStep6<A,S> {
        public InternalWithActionStep7<A,S> setHeuristicFunction(){
            return new InternalWithActionStep7<A,S>();
        }

        public InformedSearchProblem<A,S,Double> build(){
            return null;
        }
    }

    public static class InternalWithActionStep7<A,S> {

        public HeuristicSearchProblem<A,S,Double> build(){
            return null;
        }
    }

    public static class BuilderAssistant {

        private BuilderAssistant(){

        }

        public InternalNoActionStep1 withoutActions(){
            return new InternalNoActionStep1();
        }

        public <A> InternalWithActionStep1<A> withActions(Class<A> actionClass){
            return new InternalWithActionStep1<A>(actionClass);
        }
    }

    public static BuilderAssistant create(){
        return new BuilderAssistant();
    }
}
