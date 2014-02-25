package es.usc.citius.lab.hipster.examples;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;
import es.usc.citius.lab.hipster.algorithm.Algorithms;
import es.usc.citius.lab.hipster.algorithm.problem.HeuristicSearchProblem;
import es.usc.citius.lab.hipster.util.map.MapBasedGraphSearchProblem;

import java.util.*;
import java.util.List;

/**
 * Implementation of the Romania problem as described in
 * http://www.pearsonhighered.com/assets/hip/us/hip_us_pearsonhighered/samplechapter/0136042597.pdf.
 *
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @since 24/02/2014
 */
public class RomaniaProblem {

    /**
     * Enumeration for our type of nodes.
     */
    private enum City{
        Oradea("Oradea"), Zerind("Zerind"), Arad("Arad"), Timisoara("Timisoara"), Lugoj("Lugoj"),
        Mehadia("Mehadia"), Drobeta("Drobeta"), Craiova("Craiova"), Rimnicu_Vilcea("Rimnicu Vilcea"),
        Pitesti("Pitesti"), Sibiu("Sibiu"), Fagaras("Fagaras"), Bucharest("Bucharest"), Giurgiu("Giurgiu"),
        Urziceni("Urziceni"), Hirsova("Hirsova"), Eforie("Eforie"), Vaslui("Vaslui"), Iasi("Iasi"),
        Neamt("Neamt");

        /**
         * Default constructor for the City type.
         *
         * @param name name of the city
         */
        private City(final String name){
            this.name = name;
        }

        private final String name;

        @Override
        public String toString() {
            return name;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        //This example is based in the graph definition of the Romania problem, which is a graph where the
        //nodes are the main cities of Romania, connected by arcs that represent the roads of the country, whose
        //weight is the distance in km between the cities.
        //The Goal of the problem is always the city of Bucharest. The heuristic of the problem is the distance
        //in km of the straight line between each city and Bucharest.
        City begin = City.Lugoj;
        City goal = City.Bucharest;
        //We instantiate the search problem using the graph-based implementation provided in Hipster.
        HeuristicSearchProblem<City, Double> problem = new MapBasedGraphSearchProblem<City>(begin, goal, transitions(), costs(), heuristics());
        List<City> path = Algorithms.createAStar(problem).getOptimalPath();
        System.out.println("Solution path: " + path);
    }

    /**
     * Transitions definition for the Romania problem. Considered bidirectional arcs.
     *
     * @return map with the transitions definition for the Romania problem.
     */
    public static Map<City, Collection<City>> transitions(){
        Multimap<City, City> map = ArrayListMultimap.create();
        map.put(City.Oradea, City.Zerind);
        map.put(City.Oradea, City.Sibiu);
        map.put(City.Zerind, City.Oradea);
        map.put(City.Zerind, City.Arad);
        map.put(City.Arad, City.Zerind);
        map.put(City.Arad, City.Sibiu);
        map.put(City.Arad, City.Timisoara);
        map.put(City.Timisoara, City.Arad);
        map.put(City.Timisoara, City.Lugoj);
        map.put(City.Lugoj, City.Timisoara);
        map.put(City.Lugoj, City.Mehadia);
        map.put(City.Mehadia, City.Lugoj);
        map.put(City.Mehadia, City.Drobeta);
        map.put(City.Drobeta, City.Mehadia);
        map.put(City.Drobeta, City.Craiova);
        map.put(City.Craiova, City.Drobeta);
        map.put(City.Craiova, City.Rimnicu_Vilcea);
        map.put(City.Craiova, City.Pitesti);
        map.put(City.Rimnicu_Vilcea, City.Sibiu);
        map.put(City.Rimnicu_Vilcea, City.Pitesti);
        map.put(City.Rimnicu_Vilcea, City.Craiova);
        map.put(City.Sibiu, City.Oradea);
        map.put(City.Sibiu, City.Arad);
        map.put(City.Sibiu, City.Rimnicu_Vilcea);
        map.put(City.Sibiu, City.Fagaras);
        map.put(City.Pitesti, City.Rimnicu_Vilcea);
        map.put(City.Pitesti, City.Craiova);
        map.put(City.Pitesti, City.Bucharest);
        map.put(City.Fagaras, City.Sibiu);
        map.put(City.Fagaras, City.Bucharest);
        map.put(City.Bucharest, City.Fagaras);
        map.put(City.Bucharest, City.Giurgiu);
        map.put(City.Bucharest, City.Pitesti);
        map.put(City.Bucharest, City.Urziceni);
        map.put(City.Urziceni, City.Bucharest);
        map.put(City.Urziceni, City.Hirsova);
        map.put(City.Urziceni, City.Vaslui);
        map.put(City.Hirsova, City.Urziceni);
        map.put(City.Hirsova, City.Eforie);
        map.put(City.Eforie, City.Hirsova);
        map.put(City.Vaslui, City.Urziceni);
        map.put(City.Vaslui, City.Iasi);
        map.put(City.Iasi, City.Vaslui);
        map.put(City.Iasi, City.Neamt);
        map.put(City.Neamt, City.Iasi);
        return map.asMap();
    }

    /**
     * Costs definition for the Romania problem. Added costs for each direction of the arcs.
     *
     * @return map with the costs definition for the Romania problem.
     */
    public static Map<City, Map<City, Double>> costs(){
        Table<City, City, Double> table = HashBasedTable.create();
        table.put(City.Oradea, City.Zerind, 71d);
        table.put(City.Oradea, City.Sibiu, 151d);
        table.put(City.Zerind, City.Arad, 75d);
        table.put(City.Zerind, City.Oradea, 71d);
        table.put(City.Arad, City.Sibiu, 140d);
        table.put(City.Arad, City.Zerind, 75d);
        table.put(City.Arad, City.Timisoara, 118d);
        table.put(City.Timisoara, City.Arad, 118d);
        table.put(City.Timisoara, City.Lugoj, 111d);
        table.put(City.Lugoj, City.Timisoara, 111d);
        table.put(City.Lugoj, City.Mehadia, 70d);
        table.put(City.Mehadia, City.Lugoj, 70d);
        table.put(City.Mehadia, City.Drobeta, 75d);
        table.put(City.Drobeta, City.Mehadia, 75d);
        table.put(City.Drobeta, City.Craiova, 120d);
        table.put(City.Craiova, City.Drobeta, 120d);
        table.put(City.Craiova, City.Rimnicu_Vilcea, 146d);
        table.put(City.Craiova, City.Pitesti, 138d);
        table.put(City.Rimnicu_Vilcea, City.Sibiu, 80d);
        table.put(City.Rimnicu_Vilcea, City.Craiova, 146d);
        table.put(City.Rimnicu_Vilcea, City.Pitesti, 97d);
        table.put(City.Sibiu, City.Oradea, 151d);
        table.put(City.Sibiu, City.Arad, 140d);
        table.put(City.Sibiu, City.Fagaras, 99d);
        table.put(City.Sibiu, City.Rimnicu_Vilcea, 80d);
        table.put(City.Pitesti, City.Rimnicu_Vilcea, 97d);
        table.put(City.Pitesti, City.Craiova, 138d);
        table.put(City.Pitesti, City.Bucharest, 101d);
        table.put(City.Bucharest, City.Fagaras, 211d);
        table.put(City.Bucharest, City.Giurgiu, 90d);
        table.put(City.Bucharest, City.Pitesti, 101d);
        table.put(City.Bucharest, City.Urziceni, 85d);
        table.put(City.Urziceni, City.Bucharest, 85d);
        table.put(City.Urziceni, City.Hirsova, 98d);
        table.put(City.Urziceni, City.Vaslui, 142d);
        table.put(City.Hirsova, City.Urziceni, 98d);
        table.put(City.Hirsova, City.Eforie, 86d);
        table.put(City.Eforie, City.Hirsova, 86d);
        table.put(City.Vaslui, City.Urziceni, 142d);
        table.put(City.Vaslui, City.Iasi, 92d);
        table.put(City.Iasi, City.Vaslui, 92d);
        table.put(City.Iasi, City.Neamt, 87d);
        table.put(City.Neamt, City.Iasi, 87d);
        return table.rowMap();
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
