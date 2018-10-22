package es.usc.citius.hipster.model.problem;

import es.usc.citius.hipster.model.Node;
import es.usc.citius.hipster.model.function.NodeExpander;

/**
 * Defines a search problems in terms of a initial node to start with and the node expander
 * function that generates new successor nodes. A SearchProblem can be fully defined using
 * the {@link es.usc.citius.hipster.model.problem.ProblemBuilder} class assistant builder.
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 * @author Adrián González Sieira <<a href="mailto:adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 */
public class SearchProblem<A,S,N extends Node<A,S,N>> {
    private N initialNode;
    private N finalNode;
    private NodeExpander<A,S,N> expander;

    public SearchProblem(N initialNode, NodeExpander<A, S, N> expander) {
        this.initialNode = initialNode;
        this.finalNode = null;
        this.expander = expander;
    }

    public SearchProblem(N initialNode, N finalNode, NodeExpander<A, S, N> expander) {
        this.initialNode = initialNode;
        this.finalNode = finalNode;
        this.expander = expander;
    }

    public N getInitialNode() {
        return initialNode;
    }

    public NodeExpander<A, S, N> getExpander() {
        return expander;
    }

    public N getFinalNode() {
        return finalNode;
    }

    /**
     * @deprecated will disappear in next release
     */
    public void setFinalNode(N finalNode) {
        this.finalNode = finalNode;
    }
}
