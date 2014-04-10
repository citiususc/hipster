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

public final class ProblemBuilder {

    public static final class ProblemBuilderAssistant {
        private ProblemBuilderAssistant(){}

        public static final class ActionStateProblemBuilder<S> {
            private final S initialState;
            private S optionalGoalState;

            public ActionStateProblemBuilder(S initialState) {
                this.initialState = initialState;
            }

            public ActionStateProblemBuilder<S> goalState(S goal){
                this.optionalGoalState = goal;
                return this;
            }

            /**
             * Create a problem model that uses explicit actions.
             * This forces you to implement functions to operate with actions,
             * such as ActionFunction to obtain applicable actions for a given state,
             * or ActionStateTransitionFunction to apply actions to states in order
             * to obtain new states.
             */
            public ExplicitActionProblemBuilder defineProblemWithExplicitActions(){
                return new ExplicitActionProblemBuilder();
            }

            /**
             * This generates a simple problem model without actions. You just need
             * to define functions to navigate from state to state without using actions.
             */
            public NoActionsProblem defineProblemWithoutActions(){
                return new NoActionsProblem();
            }

            public final class NoActionsProblem {
                private NoActionsProblem(){}

                public GenericSearchProblemBuilder<Void> useTransitionFunction(TransitionFunction<Void, S> transitionFunction){
                    return new GenericSearchProblemBuilder<Void>(transitionFunction);
                }
            }

            public final class ExplicitActionProblemBuilder {
                private ExplicitActionProblemBuilder(){}

                public <A> ExplicitActionProblem useActionFunction(ActionFunction<A, S> actionFunction){
                    return new ExplicitActionProblem(actionFunction);
                }

                public final class ExplicitActionProblem<A> {
                    private ActionFunction<A, S> af;

                    public ExplicitActionProblem(ActionFunction<A, S> af) {
                        this.af = af;
                    }

                    public GenericSearchProblemBuilder<A> useTransitionFunction(ActionStateTransitionFunction<A, S> atf){
                        return new GenericSearchProblemBuilder<A>(new DefaultActionStateTransitionFunction<A, S>(af, atf));
                    }
                }

                /**
                 * Use a transition function that computes all the actions/states that are
                 * reachable from a given state. The function returns a set of Transition
                 * with the action and the new resultant state. If you prefer to define actions
                 * separately, use useActionFunction instead.
                 * @param transitionFunction
                 *
                 */
                public <A> GenericSearchProblemBuilder<A> useTransitionFunction(TransitionFunction<A, S> transitionFunction){
                    return new GenericSearchProblemBuilder<A>(transitionFunction);
                }
            }

            public final class GenericSearchProblemBuilder<A> {
                private final TransitionFunction<A,S> tf;

                private GenericSearchProblemBuilder(TransitionFunction<A,S> tf){
                    this.tf = tf;
                }

                // Build an uninformed search problem
                public SearchProblem<A,S> build(){
                    return new SearchProblem<A, S>() {
                        @Override
                        public TransitionFunction<A, S> getTransitionFunction() {
                            return tf;
                        }

                        @Override
                        public S getInitialState() {
                            return initialState;
                        }

                        @Override
                        public S getGoalState() {
                            return optionalGoalState;
                        }
                    };
                }

                /**
                 * Define a cost function. This immediately defines an InformedSearchProblem
                 * @param cf
                 *
                 */
                public <C extends Comparable<C>> InformedSearchProblemBuilder<C> useCostFunction(CostFunction<A, S, C> cf){
                    return new InformedSearchProblemBuilder<C>(cf);
                }

                /**
                 * An informed search problem builder generates informed search problems with a generic cost
                 * @param <C> generic cost type
                 */
                public final class InformedSearchProblemBuilder<C extends Comparable<C>> {
                    private CostFunction<A,S,C> cf;

                    public InformedSearchProblemBuilder(CostFunction<A, S, C> cf) {
                        this.cf = cf;
                    }

                    public InformedSearchProblem<A,S,C> build(){
                        return new InformedSearchProblem<A, S, C>() {
                            @Override
                            public CostFunction<A, S, C> getCostFunction() {
                                return cf;
                            }

                            @Override
                            public TransitionFunction<A, S> getTransitionFunction() {
                                return tf;
                            }

                            @Override
                            public S getInitialState() {
                                return initialState;
                            }

                            @Override
                            public S getGoalState() {
                                return optionalGoalState;
                            }
                        };

                    }

                    public HeuristicSearchProblemBuilder useHeuristicFunction(HeuristicFunction<S, C> hf){
                        return new HeuristicSearchProblemBuilder(hf);
                    }

                    public final class HeuristicSearchProblemBuilder {
                        private HeuristicFunction<S,C> hf;

                        public HeuristicSearchProblemBuilder(HeuristicFunction<S, C> hf) {
                            this.hf = hf;
                        }


                        public HeuristicSearchProblem<A,S,C> build(){
                            return new HeuristicSearchProblem<A, S, C>() {
                                @Override
                                public HeuristicFunction<S, C> getHeuristicFunction() {
                                    return hf;
                                }

                                @Override
                                public CostFunction<A, S, C> getCostFunction() {
                                    return cf;
                                }

                                @Override
                                public TransitionFunction<A, S> getTransitionFunction() {
                                    return tf;
                                }

                                @Override
                                public S getInitialState() {
                                    return initialState;
                                }

                                @Override
                                public S getGoalState() {
                                    return optionalGoalState;
                                }
                            };
                        }
                    }
                }
            }
        }

        public <S> ActionStateProblemBuilder<S> initialState(S initialState){
            return new ActionStateProblemBuilder<S>(initialState);
        }

    }

    public static ProblemBuilderAssistant create(){
        return new ProblemBuilderAssistant();
    }
}
