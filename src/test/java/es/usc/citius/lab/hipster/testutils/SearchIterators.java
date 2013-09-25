package es.usc.citius.lab.hipster.testutils;

import java.util.Iterator;

import es.usc.citius.lab.hipster.function.ScalarFunction;
import es.usc.citius.lab.hipster.node.informed.CostNode;

public final class SearchIterators {

	private SearchIterators(){};
	
	public static <S,T extends Comparable<T>> Iterator<? extends CostNode<S, T>> createAStar(SearchComponentFactory<S, T> componentFactory){
		return new AStarIteratorFactory<S,T>(componentFactory).buildIteratorSearch();
	}
	
	public static <S,T extends Comparable<T>> Iterator<? extends CostNode<S, T>> createBellmanFord(SearchComponentFactory<S, T> componentFactory){
		return new BellmanFordIteratorFactory<S,T>(componentFactory).buildIteratorSearch();
	}
	
	public static <S,T extends Comparable<T>> Iterator<? extends CostNode<S, T>> createADStar(SearchComponentFactory<S, T> componentFactory, ScalarFunction<T> scale, double epsilon, T min, T max){
		return new ADStarIteratorFactory<S,T>(componentFactory, scale, epsilon, min, max).buildIteratorSearch();
	}

}
