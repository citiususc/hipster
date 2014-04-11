///*
// * Copyright 2014 CITIUS <http://citius.usc.es>, University of Santiago de Compostela.
// *
// *    Licensed under the Apache License, Version 2.0 (the "License");
// *    you may not use this file except in compliance with the License.
// *    You may obtain a copy of the License at
// *
// *        http://www.apache.org/licenses/LICENSE-2.0
// *
// *    Unless required by applicable law or agreed to in writing, software
// *    distributed under the License is distributed on an "AS IS" BASIS,
// *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *    See the License for the specific language governing permissions and
// *    limitations under the License.
// */
//
//package es.usc.citius.lab.hipster.examples;
//
//
//import es.usc.citius.hipster.util.examples.maze.Maze2D;
//import es.usc.citius.hipster.util.examples.maze.MazeSearch;
//import es.usc.citius.hipster.util.examples.maze.Mazes;
//
//import java.awt.*;
//import java.util.Collection;
//import java.util.List;
//
//public class MazeShortestPath {
//
//    public static void main(String[] args) throws InterruptedException {
//        // First, create a maze. There is a class called Maze2D
//        // that allows the creation of labyrinths using ascii.
//        // For this example, we use the MAZE5 example defined in
//        // class Mazes.
//        Mazes.Example example = Mazes.Example.MAZE5;
//        // Print the maze
//        System.out.println("Maze example:");
//        System.out.println(example.getMaze());
//        // Now create the search problem. We have to define
//        // how to move from one tile to another tile, what's the
//        // cost of each movement (top/down/left/right and diagonal movements)
//        // the heuristic to estimate the distance to the goal (optional).
//        // There is a good A* tutorial that explains some important concepts
//        // here http://www.policyalmanac.org/games/aStarTutorial.htm
//        final Maze2D maze = example.getMaze();
//        // In order to search, we need at least the origin and the goal destination.
//        // We can take these two points from the default maze:
//        Point origin = maze.getInitialLoc();
//        Point goal = maze.getGoalLoc();
//        // The maze is a 2D map, where each tile defined by 2D coordinates x and y
//        // can be empty or occupied by an obstacle. We have to define de transition
//        // function that tells the algorithm which are the available movements from
//        // a concrete tile point.
//        TransitionFunction<Point> transition = new TransitionFunction<Point>() {
//            @Override
//            public Iterable<? extends Transition<Point>> from(Point current) {
//                // The transition function returns a collection of transitions.
//                // A transition is basically a class Transition with two attributes:
//                // source point (from) and destination point (to). Our source point
//                // is the current point argument. We have to compute which are the
//                // available movements (destination points) from the current point.
//                // Class Maze has a helper method that tell us the empty points
//                // (where we can move) available:
//                Collection<Point> destinations = maze.validLocationsFrom(current);
//                // Now we have to create the transitions. For each destination point,
//                // we have to create a new transition: new Transition(current, destination).
//                // To avoid this step, we can use the method map from Transition which creates
//                // all the transitions for us:
//                Iterable<Transition<Point>> availableTransitions = Transition.map(current, destinations);
//                // That's all.
//                return availableTransitions;
//            }
//        };
//        // We know now how to move (transitions) from each tile. We need to define the cost
//        // of each movement. A diagonal movement (for example, from (0,0) to (1,1)) is longer
//        // than a top/down/left/right movement. Although this is straightforward, if you don't
//        // know why, read this http://www.policyalmanac.org/games/aStarTutorial.htm.
//        // For this purpose, we define a CostFunction that computes the cost of each movement.
//        // The CostFunction is an interface with two generic types: S - the state, and T - the cost
//        // type. We use Points as states in this problem, and for example doubles to compute the distances:
//        CostFunction<Point, Double> costFunction = new CostFunction<Point, Double>() {
//            @Override
//            public Double evaluate(Transition<Point> transition) {
//                Point source = transition.from();
//                Point destination = transition.to();
//                // The distance from the source to de destination is the euclidean
//                // distance between these two points http://en.wikipedia.org/wiki/Euclidean_distance
//                return source.distance(destination);
//            }
//        };
//
//        // With these elements, we can create the search problem.
//        DefaultSearchProblem<Point> problem = new DefaultSearchProblem<Point>(origin, goal, transition, costFunction);
//        // Now, just create the A* algorithm to search and to obtain the shortest path
//        List<Point> path = Algorithms.createAStar(problem).getOptimalPath();
//        System.out.println("Solution path: " + path);
//
//        // -------------- Realtime printing -------------------
//        // There are some methods in class MazeSearch to print the evolution of the search process:
//        MazeSearch.executePrintIteratorSearch(Algorithms.createAStar(problem).iterator(), maze);
//    }
//}
