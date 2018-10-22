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

package es.usc.citius.hipster.algorithm;

import es.usc.citius.hipster.algorithm.localsearch.AnnealingSearch;
import es.usc.citius.hipster.algorithm.localsearch.HillClimbing;
import es.usc.citius.hipster.algorithm.localsearch.AnnealingSearch.AcceptanceProbability;
import es.usc.citius.hipster.algorithm.localsearch.AnnealingSearch.SuccessorFinder;
import es.usc.citius.hipster.model.CostNode;
import es.usc.citius.hipster.model.HeuristicNode;
import es.usc.citius.hipster.model.Node;
import es.usc.citius.hipster.model.function.impl.ADStarNodeExpander;
import es.usc.citius.hipster.model.function.impl.ADStarNodeFactory;
import es.usc.citius.hipster.model.function.impl.ScaleWeightedNodeFactory;
import es.usc.citius.hipster.model.impl.ADStarNodeImpl;
import es.usc.citius.hipster.model.problem.SearchComponents;
import es.usc.citius.hipster.model.problem.SearchProblem;

import java.util.Collections;

/**
 * Util class to create algorithms easily. Each method expects a
 * {@link es.usc.citius.hipster.model.problem.SearchProblem} with the components
 * of the algorithm and returns an iterable algorithm that can be used to search
 * a goal or iterate over the state space. A SearchProblem can be easily defined
 * with the {@link es.usc.citius.hipster.model.problem.ProblemBuilder} class.
 *
 * @see es.usc.citius.hipster.model.problem.ProblemBuilder
 *
 * @author Pablo Rodríguez Mier <
 *         <a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc
 *         .es</a>>
 * @author Adrián González Sieira <
 *         <a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 */
public final class Hipster {

	private Hipster() {

	}

	/**
	 * Instantiates a A* algorithm given a problem definition.
	 *
	 * @param components
	 *            search problem definition with the components of the algorithm
	 * @param <A>
	 *            type of the actions
	 * @param <S>
	 *            type of the states
	 * @param <C>
	 *            type of the cost
	 * @param <N>
	 *            type of the nodes
	 * @return instance of {@link es.usc.citius.hipster.algorithm.AStar} for the
	 *         problem definition
	 */
	public static <A, S, C extends Comparable<C>, N extends HeuristicNode<A, S, C, N>> AStar<A, S, C, N> createAStar(
			SearchProblem<A, S, N> components) {
		return new AStar<A, S, C, N>(components.getInitialNode(), components.getExpander());
	}

	/**
	 * Instantiates a Dijkstra algorithm (A* algorithm with no heuristic
	 * function) given a problem definition.
	 *
	 * @param components
	 *            search problem definition with the components of the algorithm
	 * @param <A>
	 *            type of the actions
	 * @param <S>
	 *            type of the states
	 * @param <C>
	 *            type of the cost
	 * @param <N>
	 *            type of the nodes
	 * @return instance of {@link es.usc.citius.hipster.algorithm.AStar} for the
	 *         problem definition, using no heuristic.
	 */
	public static <A, S, C extends Comparable<C>, N extends HeuristicNode<A, S, C, N>> AStar<A, S, C, N> createDijkstra(
			SearchProblem<A, S, N> components) {
		// TODO: There is no difference with AStar. Actually if the NodeExpander
		// uses heuristics, this "Dijkstra" impl works as the AStar. This should
		// be changed!
		return new AStar<A, S, C, N>(components.getInitialNode(), components.getExpander());
	}

	/**
	 * Instantiates a Bellman Ford algorithm for a problem definition.
	 *
	 * @param components
	 *            search problem definition with the components of the algorithm
	 * @param <A>
	 *            type of the actions
	 * @param <S>
	 *            type of the states
	 * @param <C>
	 *            type of the cost
	 * @param <N>
	 *            type of the nodes
	 * @return instance of {@link es.usc.citius.hipster.algorithm.BellmanFord}
	 *         for the problem definition
	 */
	public static <A, S, C extends Comparable<C>, N extends CostNode<A, S, C, N>> BellmanFord<A, S, C, N> createBellmanFord(
			SearchProblem<A, S, N> components) {
		return new BellmanFord<A, S, C, N>(components.getInitialNode(), components.getExpander());
	}

	/**
	 * Instantiates Breadth First Search algorithm for a problem definition.
	 *
	 * @param components
	 *            search problem definition with the components of the algorithm
	 * @param <A>
	 *            type of the actions
	 * @param <S>
	 *            type of the states
	 * @param <N>
	 *            type of the nodes
	 * @return instance of
	 *         {@link es.usc.citius.hipster.algorithm.BreadthFirstSearch} for
	 *         the problem definition
	 */
	public static <A, S, N extends Node<A, S, N>> BreadthFirstSearch<A, S, N> createBreadthFirstSearch(
			SearchProblem<A, S, N> components) {
		return new BreadthFirstSearch<A, S, N>(components.getInitialNode(), components.getExpander());
	}

	/**
	 * Instantiates Depth First Search algorithm for a problem definition.
	 *
	 * @param components
	 *            search problem definition with the components of the algorithm
	 * @param <A>
	 *            type of the actions
	 * @param <S>
	 *            type of the states
	 * @param <N>
	 *            type of the nodes
	 * @return instance of
	 *         {@link es.usc.citius.hipster.algorithm.DepthFirstSearch} for the
	 *         problem definition
	 */
	public static <A, S, N extends Node<A, S, N>> DepthFirstSearch<A, S, N> createDepthFirstSearch(
			SearchProblem<A, S, N> components) {
		return new DepthFirstSearch<A, S, N>(components.getInitialNode(), components.getExpander());
	}

	/**
	 * Instantiates Depth Limited Search algorithm for a problem definition.
	 *
	 * @param components
	 *            search problem definition with the components of the algorithm
	 * @param <A>
	 *            type of the actions
	 * @param <S>
	 *            type of the states
	 * @param <N>
	 *            type of the nodes
	 * @return instance of
	 *         {@link es.usc.citius.hipster.algorithm.DepthFirstSearch} for the
	 *         problem definition
	 */
	public static <A, S, N extends Node<A, S, N>> DepthLimitedSearch<A, S, N> createDepthLimitedSearch(
			SearchProblem<A, S, N> components, int depth) {
		return new DepthLimitedSearch<A, S, N>(components.getInitialNode(), components.getFinalNode(),
				components.getExpander(), depth);
	}

	/**
	 * Instantiates a IDA* algorithm given a problem definition.
	 *
	 * @param components
	 *            search problem definition with the components of the algorithm
	 * @param <A>
	 *            type of the actions
	 * @param <S>
	 *            type of the states
	 * @param <C>
	 *            type of the cost
	 * @param <N>
	 *            type of the nodes
	 * @return instance of {@link es.usc.citius.hipster.algorithm.IDAStar} for
	 *         the problem definition
	 */
	public static <A, S, C extends Comparable<C>, N extends HeuristicNode<A, S, C, N>> IDAStar<A, S, C, N> createIDAStar(
			SearchProblem<A, S, N> components) {
		return new IDAStar<A, S, C, N>(components.getInitialNode(), components.getExpander());
	}

	/**
	 * Instantiates a Hill Climbing algorithm given a problem definition.
	 *
	 * @param components
	 *            search problem definition with the components of the algorithm
	 * @param enforced
	 *            flag to use Enforced Hill Climbing instead of classic Hill
	 *            Climbing algorithm
	 * @param <A>
	 *            type of the actions
	 * @param <S>
	 *            type of the states
	 * @param <C>
	 *            type of the cost
	 * @param <N>
	 *            type of the nodes
	 * @return instance of
	 *         {@link es.usc.citius.hipster.algorithm.localsearch.HillClimbing}
	 *         for the problem definition
	 */
	public static <A, S, C extends Comparable<C>, N extends HeuristicNode<A, S, C, N>> HillClimbing<A, S, C, N> createHillClimbing(
			SearchProblem<A, S, N> components, boolean enforced) {
		return new HillClimbing<A, S, C, N>(components.getInitialNode(), components.getExpander(), enforced);
	}

	/**
	 * Instantiates an AnnealingSearch algorithm given a problem definition.
	 *
	 * @param components
	 *            search problem definition with the components of the algorithm
	 * @param alpha
	 *            coefficient of the geometric cooling schedule
	 * @param <A>
	 *            type of the actions
	 * @param <S>
	 *            type of the states
	 * @param <N>
	 *            type of the nodes
	 * @return instance of
	 *         {@link es.usc.citius.hipster.algorithm.localsearch.HillClimbing}
	 *         for the problem definition
	 */
	public static <A, S, N extends HeuristicNode<A, S, Double, N>> AnnealingSearch<A, S, N> createAnnealingSearch(
			SearchProblem<A, S, N> components, Double alpha, Double minTemp,
			AcceptanceProbability acceptanceProbability, SuccessorFinder<A, S, N> successorFinder) {
		return new AnnealingSearch<A, S, N>(components.getInitialNode(), components.getExpander(), alpha,
				minTemp, acceptanceProbability, successorFinder);
	}

	/**
	 * Instantiates a Multi-objective Label Setting algorithm given a problem
	 * definition.
	 *
	 * @param components
	 *            search problem definition with the components of the algorithm
	 * @param <A>
	 *            type of the actions
	 * @param <S>
	 *            type of the states
	 * @param <C>
	 *            type of the cost
	 * @param <N>
	 *            type of the nodes
	 * @return instance of
	 *         {@link es.usc.citius.hipster.algorithm.MultiobjectiveLS} for the
	 *         problem definition
	 */
	public static <A, S, C extends Comparable<C>, N extends HeuristicNode<A, S, C, N>> MultiobjectiveLS<A, S, C, N> createMultiobjectiveLS(
			SearchProblem<A, S, N> components) {
		return new MultiobjectiveLS<A, S, C, N>(components.getInitialNode(), components.getExpander());
	}

	/**
	 * Instantiates a Anytime Dynamic A* algorithm given the search components.
	 * Search components can be obtained easily for graph-based problems using
	 * {@link es.usc.citius.hipster.graph.GraphSearchProblem}.
	 *
	 * @param components
	 *            search components to be used by the algorithm
	 * @param <A>
	 *            type of the actions
	 * @param <S>
	 *            type of the states
	 * @param <C>
	 *            type of the cost
	 * @return instance of {@link es.usc.citius.hipster.algorithm.ADStarForward}
	 *         for the search components
	 */
	public static <A, S, C extends Comparable<C>> ADStarForward<A, S, C, ADStarNodeImpl<A, S, C>> createADStar(
			SearchComponents<A, S, C> components) {
		// node factory instantiation
		ADStarNodeFactory<A, S, C> factory = new ADStarNodeFactory<A, S, C>(components);
		// node expander instantiation
		ADStarNodeExpander<A, S, C, ADStarNodeImpl<A, S, C>> expander = new ADStarNodeExpander<A, S, C, ADStarNodeImpl<A, S, C>>(
				components, factory, 1.0);
		// instantiate algorithm
		return new ADStarForward(components.getBegin(), Collections.singleton(components.getGoal()), expander);
	}

	/**
	 * Instantiates a ARA* algorithm given a problem definition.
	 *
	 * @param components
	 *            search problem definition with the components of the algorithm
	 * @param <A>
	 *            type of the actions
	 * @param <S>
	 *            type of the states
	 * @param <C>
	 *            type of the cost
	 * @param <N>
	 *            type of the nodes
	 * @return instance of {@link es.usc.citius.hipster.algorithm.ARAStar} for
	 *         the problem definition
	 */
	public static <A, S, C extends Comparable<C>, N extends HeuristicNode<A, S, C, N>> ARAStar<A, S, C, N> createARAStar(
			SearchProblem<A, S, N> components) {

		//This solves the issue of calling this method without
		if(!(components.getExpander().getNodeFactory() instanceof ScaleWeightedNodeFactory)){
			throw new IllegalArgumentException("ARA* is an Anytime Algorithm. Please, set up your Search Problem properly in the ProblemBuilder");
		}

		return new ARAStar<A, S, C, N>(
				components.getInitialNode().state(), components.getFinalNode().state(),
				components.getScaleFactor(),
				components.getExpander()
		);
	}
}
