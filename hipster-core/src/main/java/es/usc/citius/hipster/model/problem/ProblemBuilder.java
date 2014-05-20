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


import es.usc.citius.hipster.model.Transition;
import es.usc.citius.hipster.model.function.*;
import es.usc.citius.hipster.model.function.impl.*;
import es.usc.citius.hipster.model.impl.UnweightedNode;
import es.usc.citius.hipster.model.impl.WeightedNode;

/**
 * Problem builder that is used to guide the user through the creation of a
 * {@link SearchProblem} with the main components
 * required to instantiate an algorithm.
 */
public final class ProblemBuilder {

    private ProblemBuilder(){

    }

    /**
     * Internal wizard assistant class.
     */
    public static final class Wizard {
        private Wizard(){}

        /**
         * Step to define the initial state of the problem.
         */
        public static final class ActionState<S> {
            private final S initialState;

            public ActionState(S initialState) {
                this.initialState = initialState;
            }

            /**
             * Create a problem model that uses explicit actions.
             * This forces to implement functions to operate with actions,
             * such as the {@link es.usc.citius.hipster.model.function.ActionFunction}
             * to obtain applicable actions for a given state,
             * or {@link es.usc.citius.hipster.model.function.ActionStateTransitionFunction}
             * to apply actions to states in order to obtain new states.
             * Use this function when you want to define explicitly the actions of your problem
             * (for example, in the 8-Puzzle, actions are UP/DOWN/LEFT/RIGHT movements, see example
             * problems for more information).
             */
            public WithAction defineProblemWithExplicitActions(){
                return new WithAction();
            }

            /**
             * This generates a simple problem model without explicit actions. Use this
             * when you just want to create a simple {@link es.usc.citius.hipster.model.function.impl.StateTransitionFunction}
             * for your search problem that defines the reachable states from
             * a given states, without worrying about actions.
             */
            public WithoutAction defineProblemWithoutActions(){
                return new WithoutAction();
            }

            /**
             * Builder step to define a search problem without actions.
             */
            public final class WithoutAction {
                private WithoutAction(){}


                /**
                 * Define the transition function for your problem. The transition function
                 * ({@link es.usc.citius.hipster.model.function.impl.StateTransitionFunction})
                 * is the function that computes all the reachable states from a given state.
                 * <pre class="prettyprint">
                 * {@code
                 * StateTransitionFunction<S> tf =
                 *      new StateTransitionFunction<S>(){
                 *          public Iterable<S> successorsOf(S state) {
                 *              return successors; // return successors of state
                 *          }
                 *      }
                 * }
                 * </pre>
                 *
                 * @param transitionFunction transition function to be used.
                 */
                public Uninformed<Void> useTransitionFunction(StateTransitionFunction<S> transitionFunction){
                    return new Uninformed<Void>(transitionFunction);
                }
            }

            /**
             * Builder step to define a search problem with actions.
             */
            public final class WithAction {
                private WithAction(){}

                /**
                 * Select the action function that returns the applicable actions for
                 * each state in your problem.
                 * @param actionFunction action function to be used.
                 */
                public <A> Action<A> useActionFunction(ActionFunction<A, S> actionFunction){
                    return new Action<A>(actionFunction);
                }

                /**
                 * Builder step to select the transition function of a action-explicit search problem.
                 */
                public final class Action<A> {
                    private ActionFunction<A, S> af;

                    public Action(ActionFunction<A, S> af) {
                        this.af = af;
                    }

                    /**
                     * Select the {@link es.usc.citius.hipster.model.function.ActionStateTransitionFunction}
                     * that takes a state and an action and returns the resultant state of applying the action
                     * to the state.
                     *
                     * @param atf action state function to be used.
                     */
                    public Uninformed<A> useTransitionFunction(ActionStateTransitionFunction<A, S> atf){
                        return new Uninformed<A>(new LazyActionStateTransitionFunction<A, S>(af, atf));
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
                public <A> Uninformed<A> useTransitionFunction(TransitionFunction<A, S> transitionFunction){
                    return new Uninformed<A>(transitionFunction);
                }
            }

            public final class Uninformed<A> {
                private final TransitionFunction<A,S> tf;

                private Uninformed(TransitionFunction<A, S> tf){
                    this.tf = tf;
                }

                public SearchProblem<A, S, UnweightedNode<A, S>> build(){
                    NodeFactory<A,S,UnweightedNode<A,S>> factory = new NodeFactory<A, S, UnweightedNode<A, S>>() {
                        @Override
                        public UnweightedNode<A, S> makeNode(UnweightedNode<A, S> fromNode, Transition<A, S> transition) {
                            return new UnweightedNode<A, S>(fromNode, transition);
                        }
                    };
                    UnweightedNode<A,S> initialNode = factory.makeNode(null, Transition.<A, S>create(null, null, initialState));
                    NodeExpander<A,S,UnweightedNode<A,S>> nodeExpander = new LazyNodeExpander<A, S, UnweightedNode<A, S>>(tf, factory);
                    return new SearchProblem<A,S, UnweightedNode<A,S>>(initialNode, nodeExpander);
                }

                /**
                 * Define a cost function. This immediately defines an InformedSearchProblem
                 * @param cf
                 *
                 */
                public Informed<Double> useCostFunction(CostFunction<A, S, Double> cf){
                    // Create default components
                    return new Informed<Double>(cf, BinaryOperation.doubleAdditionOp());
                }

                public <C extends Comparable<C>> Informed<C> useGenericCostFunction(CostFunction<A,S,C> cf, BinaryOperation<C> costAlgebra){
                    return new Informed<C>(cf, costAlgebra);
                }

                /**
                 * An informed search problem builder generates informed search problems with a generic cost
                 */
                public final class Informed<C extends Comparable<C>> {
                    private CostFunction<A,S,C> cf;
                    private BinaryOperation<C> costAlgebra;

                    public Informed(CostFunction<A, S, C> cf, BinaryOperation<C> costAlgebra) {
                        this.cf = cf;
                        this.costAlgebra = costAlgebra;
                    }

                    public SearchProblem<A, S, WeightedNode<A, S, C>> build(){
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
                        return new SearchProblem<A,S,WeightedNode<A,S,C>>(initialNode, expander);
                    }

                    public Heuristic useHeuristicFunction(HeuristicFunction<S, C> hf){
                        return new Heuristic(hf);
                    }

                    public final class Heuristic {
                        private HeuristicFunction<S,C> hf;

                        public Heuristic(HeuristicFunction<S, C> hf) {
                            this.hf = hf;
                        }

                        public SearchProblem<A, S, WeightedNode<A, S, C>> build(){
                            WeightedNodeFactory<A, S, C> factory = new WeightedNodeFactory<A,S,C>(
                                    cf, hf, costAlgebra);
                            WeightedNode<A,S,C> initialNode = factory.makeNode(null, Transition.<A,S>create(null, null, initialState));
                            LazyNodeExpander<A, S, WeightedNode<A, S, C>> nodeExpander =
                                    new LazyNodeExpander<A, S, WeightedNode<A, S, C>>(tf, factory);

                            return new SearchProblem<A, S, WeightedNode<A,S,C>>(initialNode, nodeExpander);
                        }
                    }
                }
            }
        }

        public <S> ActionState<S> initialState(S initialState){
            return new ActionState<S>(initialState);
        }

    }

    /**
     * <p>
     * Creates the builder. Chain method calls until you have the problem
     * ready to call {@code build()}. Example usage:
     * </p>
     * <pre class="prettyprint">
     *     {@code
     *     Hipster.SearchProblem p =
     *          ProblemBuilder.create()
     *              .initialState(initialState)
     *              .defineProblemWithExplicitActions()
     *                  .useActionFunction(af)
     *                  .useTransitionFunction(atf)
     *                  .useCostFunction(cf)
     *                  .useHeuristicFunction(hf)
     *                  .build();
     *     }
     * </pre>
     */
    public static Wizard create(){
        return new Wizard();
    }
}
