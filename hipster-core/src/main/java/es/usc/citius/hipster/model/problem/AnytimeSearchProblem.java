package es.usc.citius.hipster.model.problem;

import es.usc.citius.hipster.model.Node;
import es.usc.citius.hipster.model.function.NodeExpander;

/**
 * Search problem definition for Anytime search algorithms, containing the following information:
 * <ul>
 *     <li>Initial and final states</li>
 *     <li>Expander component, which creates the successors nodes from the current one</li>
 *     <li>Heuristic scale factor to obtain sub-optimal solutions in anytime Algorithms (like ARA*)</li>
 * </ul>
 *
 * {@see ProblemBuilder} for further information and assisted creation of instances of this class
 *
 * @author Adrián González Sieira <<a href="mailto:adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 */
public class AnytimeSearchProblem<A,S,N extends Node<A,S,N>> extends SearchProblem<A, S, N>{

    protected float scaleFactor;

    public AnytimeSearchProblem(N initialNode, NodeExpander<A, S, N> expander, float scaleFactor) {
        super(initialNode, expander);
        this.scaleFactor = scaleFactor;
    }

    public AnytimeSearchProblem(N initialNode, N finalNode, NodeExpander<A, S, N> expander, float scaleFactor) {
        super(initialNode, finalNode, expander);
        this.scaleFactor = scaleFactor;
    }

    public AnytimeSearchProblem(N initialNode, NodeExpander<A, S, N> expander, double scaleFactor){
        this(initialNode, expander, (float) scaleFactor);
    }

    public AnytimeSearchProblem(N initialNode, N finalNode, NodeExpander<A, S, N> expander, double scaleFactor){
        this(initialNode, finalNode, expander, (float) scaleFactor);
    }

    /**
     * @return the heuristic scale factor for anytime problems.
     */
    public float getScaleFactor() {
        return scaleFactor;
    }
}
