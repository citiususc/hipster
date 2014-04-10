package es.usc.citius.hipster.util.examples;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;
import es.usc.citius.hipster.util.graph.GraphBuilder;
import es.usc.citius.hipster.util.graph.HipsterGraph;

import java.util.*;

/**
 * Definition of the states, transitions, costs and heuristics for the Romania Problem
 * as described in http://www.pearsonhighered.com/assets/hip/us/hip_us_pearsonhighered/samplechapter/0136042597.pdf.
 *
 *
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @author Pablo Rodríguez Mier
 */
public class RomanianProblem {

    public enum City{
        Arad, Bucharest, Craiova, Drobeta, Eforie, Fagaras, Giurgiu,
        Hirsova, Iasi, Lugoj, Mehadia, Neamt, Oradea, Pitesti, Rimnicu_Vilcea,
        Sibiu, Timisoara,  Urziceni, Vaslui, Zerind;
    }

    public static GraphBuilder.MutableHashBasedGraph<City, Double> graph(){
        return GraphBuilder.<City,Double>newGraph()
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
                    .connect(City.Urziceni).to(City.Vaslui).withEdge(142d);
    }

    /**
     * Heuristics definition for the Romania problem. Goal is considered Bucharest.
     *
     * @return map with the heuristics definition for the Romania problem.
     */
    public static Map<City, Double> heuristics(){
        Map<City, Double> map = new HashMap<City, Double>();
        map.put(City.Oradea, 380d);
        map.put(City.Zerind, 374d);
        map.put(City.Arad, 366d);
        map.put(City.Timisoara, 329d);
        map.put(City.Lugoj, 244d);
        map.put(City.Mehadia, 241d);
        map.put(City.Drobeta, 242d);
        map.put(City.Craiova, 160d);
        map.put(City.Rimnicu_Vilcea, 193d);
        map.put(City.Pitesti, 100d);
        map.put(City.Sibiu, 253d);
        map.put(City.Fagaras, 176d);
        map.put(City.Giurgiu, 77d);
        map.put(City.Urziceni, 80d);
        map.put(City.Hirsova, 151d);
        map.put(City.Eforie, 161d);
        map.put(City.Vaslui, 199d);
        map.put(City.Iasi, 226d);
        map.put(City.Neamt, 234d);
        map.put(City.Bucharest, 0d);
        return map;
    }

}
