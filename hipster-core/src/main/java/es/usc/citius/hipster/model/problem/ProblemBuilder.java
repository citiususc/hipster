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


import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.model.Transition;
import es.usc.citius.hipster.model.function.*;
import es.usc.citius.hipster.model.function.impl.*;
import es.usc.citius.hipster.model.impl.UnweightedNode;
import es.usc.citius.hipster.model.impl.WeightedNode;

/**
 * Problem builder can be used to define a search problem easily
 * when the cost type used is Double.
 */
public final class ProblemBuilder {

    private ProblemBuilder(){

    }

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

                public GenericSearchProblemBuilder<Void> useTransitionFunction(StateTransitionFunction<S> transitionFunction){
                    return new GenericSearchProblemBuilder<Void>(transitionFunction);
                }
            }

            public final class ExplicitActionProblemBuilder {
                private ExplicitActionProblemBuilder(){}

                public <A> ExplicitActionProblem<A> useActionFunction(ActionFunction<A, S> actionFunction){
                    return new ExplicitActionProblem<A>(actionFunction);
                }

                public final class ExplicitActionProblem<A> {
                    private ActionFunction<A, S> af;

                    public ExplicitActionProblem(ActionFunction<A, S> af) {
                        this.af = af;
                    }

                    public GenericSearchProblemBuilder<A> useTransitionFunction(ActionStateTransitionFunction<A, S> atf){
                        return new GenericSearchProblemBuilder<A>(new LazyActionStateTransitionFunction<A, S>(af, atf));
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

                public Hipster.SearchComponents<A, S, UnweightedNode<A, S>> build(){
                    NodeFactory<A,S,UnweightedNode<A,S>> factory = new NodeFactory<A, S, UnweightedNode<A, S>>() {
                        @Override
                        public UnweightedNode<A, S> makeNode(UnweightedNode<A, S> fromNode, Transition<A, S> transition) {
                            return new UnweightedNode<A, S>(fromNode, transition);
                        }
                    };
                    UnweightedNode<A,S> initialNode = factory.makeNode(null, Transition.<A, S>create(null, null, initialState));
                    NodeExpander<A,S,UnweightedNode<A,S>> nodeExpander = new LazyNodeExpander<A, S, UnweightedNode<A, S>>(tf, factory);
                    return new Hipster.SearchComponents<A,S, UnweightedNode<A,S>>(initialNode, nodeExpander);
                }

                /**
                 * Define a cost function. This immediately defines an InformedSearchProblem
                 * @param cf
                 *
                 */
                public InformedSearchProblemBuilder<Double> useCostFunction(CostFunction<A, S, Double> cf){
                    // Create default components
                    return new InformedSearchProblemBuilder<Double>(cf, BinaryOperation.doubleAdditionOp());
                }

                public <C extends Comparable<C>> InformedSearchProblemBuilder<C> useGenericCostFunction(CostFunction<A,S,C> cf, BinaryOperation<C> costAlgebra){
                    return new InformedSearchProblemBuilder<C>(cf, costAlgebra);
                }

                /**
                 * An informed search problem builder generates informed search problems with a generic cost
                 */
                public final class InformedSearchProblemBuilder<C extends Comparable<C>> {
                    private CostFunction<A,S,C> cf;
                    private BinaryOperation<C> costAlgebra;

                    public InformedSearchProblemBuilder(CostFunction<A, S, C> cf, BinaryOperation<C> costAlgebra) {
                        this.cf = cf;
                        this.costAlgebra = costAlgebra;
                    }

                    public Hipster.SearchComponents<A, S, WeightedNode<A, S, C>> build(){
                        WeightedNodeFactory<A,S,C> factory = new WeightedNodeFactory<A,S,C>(
                                cf,
                                new HeuristicFunction<S, C>() {
                                    @Override
                                    public C estimate(S state) {
                                        return costAlgebra.getIdentityElem();
                                    }
                                }, costAlgebra);
                        // Make the initial node. The initial node contains the initial state
                        // of the problem, and it comes from no previous node (null) and using no action (null)
                        WeightedNode<A,S,C> initialNode = factory.makeNode(null, Transition.<A,S>create(null, null, initialState));
                        // Create a Lazy Node Expander by default
                        NodeExpander<A,S,WeightedNode<A,S,C>> expander = new LazyNodeExpander<A, S, WeightedNode<A, S, C>>(tf, factory);
                        // Create the algorithm with all those components
                        return new Hipster.SearchComponents<A,S,WeightedNode<A,S,C>>(initialNode, expander);
                    }

                    public HeuristicSearchProblemBuilder useHeuristicFunction(HeuristicFunction<S, C> hf){
                        return new HeuristicSearchProblemBuilder(hf);
                    }

                    public final class HeuristicSearchProblemBuilder {
                        private HeuristicFunction<S,C> hf;

                        public HeuristicSearchProblemBuilder(HeuristicFunction<S,C> hf) {
                            this.hf = hf;
                        }

                        public Hipster.SearchComponents<A, S, WeightedNode<A, S, C>> build(){
                            WeightedNodeFactory<A, S, C> factory = new WeightedNodeFactory<A,S,C>(
                                    cf, hf, costAlgebra);
                            WeightedNode<A,S,C> initialNode = factory.makeNode(null, Transition.<A,S>create(null, null, initialState));
                            LazyNodeExpander<A, S, WeightedNode<A, S, C>> nodeExpander =
                                    new LazyNodeExpander<A, S, WeightedNode<A, S, C>>(tf, factory);

                            return new Hipster.SearchComponents<A, S, WeightedNode<A,S,C>>(initialNode, nodeExpander);
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
