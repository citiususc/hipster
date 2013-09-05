/*
 * Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.usc.citius.lab.hipster.algorithm;

import static org.junit.Assert.assertEquals;

import java.awt.Point;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import es.usc.citius.lab.hipster.testutils.*;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.base.Stopwatch;

import edu.uci.ics.jung.graph.DirectedGraph;
import es.usc.citius.lab.hipster.function.impl.Product;
import es.usc.citius.lab.hipster.node.informed.CostNode;
import es.usc.citius.lab.hipster.testutils.JungMazeComponentFactory;
import es.usc.citius.lab.hipster.testutils.MazeSearch.Result;
import es.usc.citius.lab.hipster.algorithm.multiobjective.maze.Maze2D;

/**
 * This class executes a benchmark to compare the performance of
 * different path search algorithms.
 *
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @author Pablo Rodríguez Mier <pablo.rodriguez.mier@usc.es>
 * @since 26/03/2013
 * @version 1.0
 */
public class BenchmarkTest {

    public BenchmarkTest() {
    }
    
    interface Algorithm {
    	void initialize(Maze2D maze);
    	MazeSearch.Result evaluate();
    }
    
    class Benchmark {
    	Map<String, Algorithm> algorithms = new TreeMap<String, BenchmarkTest.Algorithm>();
    	int times = 5;
    	
    	Benchmark(){}
    	
    	Benchmark(int times){
    		this.times = times;
    	}
    	
    	void add(String name, Algorithm alg){
    		this.algorithms.put(name, alg);
    	}
    	
    	class Score {
    		Result result;
    		long time;
    		
    		Score(Result result, long time){
    			this.result = result;
    			this.time = time;
    		}
    	}
    	
    	Map<String, Score> run(Maze2D maze){
    		Map<String, Score> results = new HashMap<String, Score>();
    		
    		for(String algName : algorithms.keySet()){
    			Algorithm alg = algorithms.get(algName);
    			Result result = null;
    			long bestTime = Long.MAX_VALUE;
    			for(int i=0; i < times; i++){
	    			alg.initialize(maze);
	    			Stopwatch w = new Stopwatch().start();
	    			result = alg.evaluate();
	    			long time = w.stop().elapsed(TimeUnit.MILLISECONDS);
	    			if (time < bestTime){
	    				bestTime = time;
	    			}
    			}
    			// Record
    			results.put(algName, new Score(result, bestTime));
    		}
    		return results;
    	}
    }
    
    private static SearchComponentFactory<Point,Double> createComponentFactory(Maze2D maze){
    	//return new MazeSearchComponentFactory(maze,false);
    	return new JungMazeComponentFactory(maze, false);
    }

    @Test
    @Ignore("Benchmark test disabled")
    public void benchmark() throws InterruptedException {
        Benchmark bench = new Benchmark();
        
        // JUNG-Dijkstra
        bench.add("JUNG-Dijkstra", new Algorithm() {
			Maze2D maze;DirectedGraph<Point, JungEdge<Point>> graph;
			public void initialize(Maze2D maze) {
				this.maze = maze;
				this.graph = JungMazeComponentFactory.createGraphFrom(maze);
			}
			public Result evaluate() {
				return MazeSearch.executeJungSearch(graph, maze.getInitialLoc(), maze.getGoalLoc());
			}
		});
        
     // Hipster-Dijkstra
        bench.add("Hipster-Dijkstra", new Algorithm() {	
			Iterator<? extends CostNode<Point, Double>> it; Point goal;
        	public void initialize(Maze2D maze) {
				it= SearchIterators.createAStar(createComponentFactory(maze));
				goal = maze.getGoalLoc();
			}
			public Result evaluate() {
				return MazeSearch.executeIteratorSearch(it, goal);
			}
		});
        
        // Bellman-Ford
        bench.add("Hipster-Bellman-Ford", new Algorithm() {
        	Iterator<? extends CostNode<Point, Double>> it; Point goal;
        	public void initialize(Maze2D maze) {
        		it = SearchIterators.createBellmanFord(createComponentFactory(maze));
        		goal = maze.getGoalLoc();
			}
			public Result evaluate() {
				return MazeSearch.executeIteratorSearch(it, goal);
			}
		});
        
        // ADStar
        bench.add("Hipster-ADStar", new Algorithm() {
        	Iterator<? extends CostNode<Point, Double>> it; Point goal;
        	public void initialize(Maze2D maze) {
        		it = SearchIterators.createADStar(createComponentFactory(maze), new Product(), 1.0d, Double.MIN_VALUE, Double.MAX_VALUE);
				//it= MazeUtils.adstar(maze, false);
				goal = maze.getGoalLoc();
			}
			public Result evaluate() {
				return MazeSearch.executeIteratorSearch(it, goal);
			}
		});

        int index = 0;
        for(String algName : bench.algorithms.keySet()){
        	System.out.println((++index) + " = " + algName);
        }
        
        for (int i = 10; i < 300; i += 10) {
        	Maze2D maze = Maze2D.empty(i);
            // Test over an empty maze
            Map<String, Benchmark.Score> results = bench.run(maze);
            // Check results and print scores. We take JUNG as baseline
            Benchmark.Score jungScore = results.get("JUNG-Dijkstra");
            String scores = "";
            for(String algName : bench.algorithms.keySet()){
            	Benchmark.Score score = results.get(algName);
            	assertEquals(jungScore.result.getCost(),score.result.getCost(), 0.0001);
            	scores += score.time + " ms\t";
            }
            System.out.println(scores);
        }
    }
}
