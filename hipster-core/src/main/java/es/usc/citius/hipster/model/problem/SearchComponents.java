package es.usc.citius.hipster.model.problem;

import es.usc.citius.hipster.model.function.CostFunction;
import es.usc.citius.hipster.model.function.HeuristicFunction;
import es.usc.citius.hipster.model.function.TransitionFunction;
import es.usc.citius.hipster.model.function.impl.BinaryOperation;
import es.usc.citius.hipster.model.function.impl.ScalarOperation;

/**
 * This class should be used to instantiate an ADStar algorithm through the {@code Hipster.createADStar} method.
 * This may change in future versions.
 *
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 */
public class SearchComponents<A, S, C extends Comparable<C>> {

    // NOTE: This part of the API is still under discussion and is subject to changes
    // in future versions. 

    private final CostFunction<A, S, C> cf;
    private final HeuristicFunction<S, C> hf;
    private final TransitionFunction<A, S> sf;
    private final TransitionFunction<A, S> pf;
    private final BinaryOperation<C> costAlgebra;
    private final ScalarOperation<C> scaleAlgebra;
    private final S begin;
    private final S goal;

    public SearchComponents(S begin, S goal, CostFunction<A, S, C> cf, HeuristicFunction<S, C> hf, TransitionFunction<A, S> sf,
                            TransitionFunction<A, S> pf, BinaryOperation<C> costAlgebra, ScalarOperation<C> scaleOperation) {
        this.begin = begin;
        this.goal = goal;
        this.cf = cf;
        this.hf = hf;
        this.sf = sf;
        this.pf = pf;
        this.costAlgebra = costAlgebra;
        this.scaleAlgebra = scaleOperation;
    }

    public SearchComponents(S begin, S goal, CostFunction<A, S, C> cf, HeuristicFunction<S, C> hf, TransitionFunction<A, S> sf,
                            BinaryOperation<C> costAlgebra) {
        this(begin, goal, cf, hf, sf, null, costAlgebra, null);
    }

    public SearchComponents(S begin, S goal, CostFunction<A, S, C> cf, HeuristicFunction<S, C> hf, TransitionFunction<A, S> sf,
                            BinaryOperation<C> costAlgebra, ScalarOperation<C> scaleOperation) {
        this(begin, goal, cf, hf, sf, null, costAlgebra, scaleOperation);
    }

    public CostFunction<A, S, C> costFunction() {
        return cf;
    }

    public HeuristicFunction<S, C> heuristicFunction() {
        return hf;
    }

    public TransitionFunction<A, S> successorFunction() {
        return sf;
    }

    public TransitionFunction<A, S> predecessorFunction() {
        return pf;
    }

    public BinaryOperation<C> costAlgebra() {
        return costAlgebra;
    }

    public ScalarOperation<C> scaleAlgebra() {
        return scaleAlgebra;
    }

    public S getBegin() {
        return begin;
    }

    public S getGoal() {
        return goal;
    }
}
