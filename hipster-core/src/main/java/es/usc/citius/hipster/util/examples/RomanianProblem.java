package es.usc.citius.hipster.util.examples;

import es.usc.citius.hipster.graph.GraphBuilder;
import es.usc.citius.hipster.model.function.HeuristicFunction;
import es.usc.citius.hipster.graph.HipsterGraph;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Definition of the states, transitions, costs and heuristics for the Romania Problem
 * as described in http://www.pearsonhighered.com/assets/hip/us/hip_us_pearsonhighered/samplechapter/0136042597.pdf.
 * </p>
 *
 * <img src="../../../../../../../assets/images/javadoc/romanian-problem.png" />
 *
 * @author Adrián González Sieira <<a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class RomanianProblem {

    /**
     * Enum with all the cities of the problem.
     */
    public enum City{
        Arad, Bucharest, Craiova, Drobeta, Eforie, Fagaras, Giurgiu,
        Hirsova, Iasi, Lugoj, Mehadia, Neamt, Oradea, Pitesti, Rimnicu_Vilcea,
        Sibiu, Timisoara,  Urziceni, Vaslui, Zerind;
    }

    private static final Map<City, Double> heuristicMap = new HashMap<City, Double>();
    private static final HipsterGraph<City,Double> graph;

    /**
     * Define the heuristic values of the problem and create the graph.
     */
    static {
        heuristicMap.put(City.Oradea, 380d);
        heuristicMap.put(City.Zerind, 374d);
        heuristicMap.put(City.Arad, 366d);
        heuristicMap.put(City.Timisoara, 329d);
        heuristicMap.put(City.Lugoj, 244d);
        heuristicMap.put(City.Mehadia, 241d);
        heuristicMap.put(City.Drobeta, 242d);
        heuristicMap.put(City.Craiova, 160d);
        heuristicMap.put(City.Rimnicu_Vilcea, 193d);
        heuristicMap.put(City.Pitesti, 100d);
        heuristicMap.put(City.Sibiu, 253d);
        heuristicMap.put(City.Fagaras, 176d);
        heuristicMap.put(City.Giurgiu, 77d);
        heuristicMap.put(City.Urziceni, 80d);
        heuristicMap.put(City.Hirsova, 151d);
        heuristicMap.put(City.Eforie, 161d);
        heuristicMap.put(City.Vaslui, 199d);
        heuristicMap.put(City.Iasi, 226d);
        heuristicMap.put(City.Neamt, 234d);
        heuristicMap.put(City.Bucharest, 0d);

        graph = GraphBuilder.<City,Double>create()
                .connect(City.Arad).to(City.Zerind).withEdge(75d)
                .connect(City.Arad).to(City.Timisoara).withEdge(118d)
                .connect(City.Arad).to(City.Sibiu).withEdge(140d)
                .connect(City.Bucharest).to(City.Giurgiu).withEdge(90d)
                .connect(City.Bucharest).to(City.Urziceni).withEdge(85d)
                .connect(City.Bucharest).to(City.Fagaras).withEdge(211d)
                .connect(City.Bucharest).to(City.Pitesti).withEdge(101d)
                .connect(City.Craiova).to(City.Drobeta).withEdge(120d)
                .connect(City.Craiova).to(City.Rimnicu_Vilcea).withEdge(146d)
                .connect(City.Craiova).to(City.Pitesti).withEdge(138d)
                .connect(City.Drobeta).to(City.Mehadia).withEdge(75d)
                .connect(City.Eforie).to(City.Hirsova).withEdge(86d)
                .connect(City.Fagaras).to(City.Sibiu).withEdge(99d)
                .connect(City.Hirsova).to(City.Urziceni).withEdge(98d)
                .connect(City.Iasi).to(City.Neamt).withEdge(87d)
                .connect(City.Iasi).to(City.Vaslui).withEdge(92d)
                .connect(City.Lugoj).to(City.Timisoara).withEdge(111d)
                .connect(City.Lugoj).to(City.Mehadia).withEdge(70d)
                .connect(City.Oradea).to(City.Zerind).withEdge(71d)
                .connect(City.Oradea).to(City.Sibiu).withEdge(151d)
                .connect(City.Pitesti).to(City.Rimnicu_Vilcea).withEdge(97d)
                .connect(City.Rimnicu_Vilcea).to(City.Sibiu).withEdge(80d)
                .connect(City.Urziceni).to(City.Vaslui).withEdge(142d)
                .createUndirectedGraph();


    }

    /**
     * Returns a {@link es.usc.citius.hipster.graph.HipsterGraph} that represents the map of Romania.
     * @return graph with the cities and costs.
     */
    public static HipsterGraph<City, Double> graph(){
        return graph;
    }

    /**
     * Heuristics definition for the Romania problem. Goal is considered Bucharest.
     * @return map with the heuristics definition for the Romania problem.
     */
    public static Map<City, Double> heuristics(){
        return heuristicMap;
    }

    /**
     * Heuristic function required to define search problems to be used with Hipster.
     * @see es.usc.citius.hipster.model.problem.SearchProblem
     * @return {@link es.usc.citius.hipster.model.function.HeuristicFunction} with the {@link #heuristics()} values.
     */
    public static HeuristicFunction<City, Double> heuristicFunction(){
        return new HeuristicFunction<City, Double>() {
            @Override
            public Double estimate(City state) {
                return heuristics().get(state);
            }
        };
    }

}
