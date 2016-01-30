package es.usc.citius.hipster.algorithm.localsearch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import es.usc.citius.hipster.algorithm.Algorithm;
import es.usc.citius.hipster.model.HeuristicNode;
import es.usc.citius.hipster.model.Node;
import es.usc.citius.hipster.model.function.NodeExpander;

/**
 * Implementation of the simulated annealing search that is a probabilistic
 * technique for approximating the global optimum of a given function. It starts
 * the exploration from a random point as a global optimum and selects one of
 * its neighbors with a neighboring function. The neighbor will become the new
 * optimum if its associated cost is lower or if the acceptance probability
 * function returns a probability greater than a random number. The probability
 * function takes as an input the cost of the current selected node, the cost of
 * its randomly selected neighbour and the current temperature. The higher the
 * cost of the neighbour is or the lower the temperature is, the more unlikely
 * it is that the neighbour becomes the new optimum. The process continues until
 * the temperature is below a given threshold. The temperature decreases at each
 * iteration according to a geometric cooling schedule that has two parameters
 * alpha and temperature min. The main idea of this algorithm is to avoid to be
 * "trapped" in a bad local optimum by exploring more deeply the state space by
 * looking at states whose cost is not optimum but that may have interesting
 * neighbours. A user can adjusted the algorithm by tuning the alpha coefficient
 * (default 0.9) or the min temperature (0.00001) or by providing his own
 * implementation of the acceptance probability function (default: exp((old
 * score - new score) / current temperature)) or the neighbouring function
 * (random selection by default). Note: costs are Double in this implementation
 * and have no type parameters.
 * 
 * see <a href="https://en.wikipedia.org/wiki/Simulated_annealing">in
 * Wikipedia</a> and <a href="http://katrinaeg.com/simulated-annealing.html">in
 * annealing search</a> for more details.
 * 
 * @param <A>
 *            class defining the action
 * @param <S>
 *            class defining the state
 * @param <C>
 *            class defining the cost, must implement
 *            {@link java.lang.Comparable}
 * @param <N>
 *            type of the nodes
 * 
 * @author Christophe Moins <
 *         <a href="mailto:christophe.moins@yahoo.fr">christophe.moins@yahoo.fr
 *         </a>>
 */
public class AnnealingSearch<A, S, N extends HeuristicNode<A, S, Double, N>> extends Algorithm<A, S, N> {

	static final private Double DEFAULT_ALPHA = 0.9;
	static final private Double DEFAULT_MIN_TEMP = 0.00001;
	static final private Double START_TEMP = 1.;

	private N initialNode;
	private Double alpha;
	private Double minTemp;
	private AcceptanceProbability acceptanceProbability;
	private SuccessorFinder<A, S, N> successorFinder;
	// expander to find all the successors of a given node.
	private NodeExpander<A, S, N> nodeExpander;

	public AnnealingSearch(N initialNode, NodeExpander<A, S, N> nodeExpander, Double alpha, Double minTemp,
			AcceptanceProbability acceptanceProbability, SuccessorFinder<A, S, N> successorFinder) {
		if (initialNode == null) {
			throw new IllegalArgumentException("Provide a valid initial node");
		}
		this.initialNode = initialNode;
		if (nodeExpander == null) {
			throw new IllegalArgumentException("Provide a valid node expander");
		}
		this.nodeExpander = nodeExpander;
		if (alpha != null) {
			if ((alpha <= 0.) || (alpha >= 1.0)) {
				throw new IllegalArgumentException("alpha must be between 0. and 1.");
			}
			this.alpha = alpha;
		} else {
			this.alpha = DEFAULT_ALPHA;
		}
		if (minTemp != null) {
			if ((minTemp < 0.) || (minTemp > 1.)) {
				throw new IllegalArgumentException("Minimum temperature must be between 0. and 1.");
			}
			this.minTemp = minTemp;
		} else {
			this.minTemp = DEFAULT_MIN_TEMP;
		}
		if (acceptanceProbability != null) {
			this.acceptanceProbability = acceptanceProbability;
		} else {
			this.acceptanceProbability = new AcceptanceProbability() {
				@Override
				public Double compute(Double oldScore, Double newScore, Double temp) {
					return (newScore < oldScore ? 1 : Math.exp((oldScore - newScore) / temp));
				}
			};
		}
		if (successorFinder != null) {
			this.successorFinder = successorFinder;
		} else {
			// default implementation of the successor: picks up a successor
			// randomly
			this.successorFinder = new SuccessorFinder<A, S, N>() {
				@Override
				public N estimate(N node, NodeExpander<A, S, N> nodeExpander) {
					List<N> successors = new ArrayList<>();
					// find a random successor
					for (N successor : nodeExpander.expand(node)) {
						successors.add(successor);
					}
					Random randIndGen = new Random();
					return successors.get(Math.abs(randIndGen.nextInt()) % successors.size());
				}
			};
		}
	}

	@Override
	public ASIterator iterator() {
		// TODO Auto-generated method stub
		return new ASIterator();
	}

	public class ASIterator implements Iterator<N> {

		private Queue<N> queue = new LinkedList<N>();
		private Double bestScore = null;
		private Double curTemp = START_TEMP;

		private ASIterator() {
			bestScore = initialNode.getEstimation();
			queue.add(initialNode);
		}

		@Override
		public boolean hasNext() {
			return !queue.isEmpty();
		}

		@Override
		public N next() {
			N currentNode = this.queue.poll();
			if (curTemp > minTemp) {
				N newNode = null;
				// we add a loop to increase the effect of a change of alpha.
				for (int i = 0; i < 100; i++) {
					N randSuccessor = successorFinder.estimate(currentNode, nodeExpander);
					Double score = randSuccessor.getScore();
					if (acceptanceProbability.compute(bestScore, score, curTemp) > Math.random()) {
						newNode = randSuccessor;
						bestScore = score;
					}
				}
				if (newNode != null) {
					queue.add(newNode);
				} else {
					queue.add(currentNode);
				}
				curTemp *= alpha;
			}
			return currentNode;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();

		}
	}

	/**
	 * Interface to compute the acceptance probability. If the new score is less
	 * than the old score, 1 will be returned so that the node is selected.
	 * Otherwise, we compute a probability that will decrease when the newScore
	 * or the temperature increase.
	 * 
	 */

	public interface AcceptanceProbability {
		Double compute(Double oldScore, Double newScore, Double temp);
	}

	/**
	 * Interface to find the successor of a node.
	 *
	 * @param <N>
	 */
	public interface SuccessorFinder<A, S, N extends Node<A, S, N>> {
		/**
		 * @param Node
		 * @return the successor of a node.
		 */
		N estimate(N node, NodeExpander<A, S, N> nodeExpander);
	}
}
