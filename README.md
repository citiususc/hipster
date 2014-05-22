![Hipster](src/main/doclava/custom/assets/hipster-template/assets/images/header-logo.png?raw=true)

A powerful, easy to use heuristic search library implemented in pure java.

## What's Hipster?

The aim of Hipster is to provide an easy to use yet powerful and flexible type-safe Java library for heuristic search. 
Hipster relies on a flexible model with generic operators that allow you to reuse and change the behavior of the algorithms very easily. Algorithms are also implemented in an iterative way, avoiding recursion. This has many benefits: full control over the search, access to the internals at runtime or a better and clear scale-out for large search spaces using the heap memory.

You can use Hipster to solve from simple graph search problems to more advanced state-space search problems where the state space is complex and weights are not just double values but custom defined costs.

## Features

The current version of the library comes with some very well-known and wide used search algorithms. Note that this list may not be exhaustive:

* Search algorithms:
    * Uninformed search:
        * Depth-First-Search (DFS).
        * Breadth-First-Search (BFS).
        * Dijkstra.
        * Bellman-Ford.
    * Informed search:
        * A\* algorithm.
        * Iterative Deepening A\* (IDA\*).
        * Anytime D\* (AD\*).
    * Local search:
        * Hill-Climbing.
        * Enforced-Hill-Climbing.
    * Other (experimental implementations)
        * Multiobjective LS algorithm. Original paper: Martins, E. D. Q. V., & Santos, J. L. E. (1999). *"The labeling            algorithm for the multiobjective shortest path problem"*. <i>Departamento de Matematica, Universidade de                Coimbra, Portugal, Tech. Rep. TR-99/005
* 3rd party adapters:
    * [Java Universal/Graph (JUNG)](http://jung.sourceforge.net/) adapter.

If you don't find the algorithm or the feature you are looking for, please consider contributing to Hipster!. You can open a new issue or better fork this repository and create a pull request with your contribution. If you go for this second option, please follow the contribution guidelines.

## Getting started

The easiest way to use Hipster is adding it as a dependency with your favourite dependency manager.
Maven users can include the library using the following snippet:

#### Snapshots

You can use the latest (unstable) version of Hipster under development. Just add the following dependency into your pom.xml:

```xml
   <!-- 
      Add this dependency into your <dependencies> section to add
      all the dependencies of Hipster to your project. Add hipster-core
      instead of hipster-all for basic functionality.
   -->
   <dependency>
      <groupId>es.usc.citius.hipster</groupId>
      <artifactId>hipster-all</artifactId>
      <version>1.0.0-SNAPSHOT</version>
   </dependency>
```

#### Releases

Stable releases are still not generated. See the [milestones](https://github.com/citiususc/hipster/issues/milestones) to check the current development status.

#### Quick Example

Here is a quick example of how to search a shortest path in a graph with Dijkstra's algorithm:

```java
// Create a simple weighted directed graph with Hipster where
// vertices are Strings and edge values are just doubles
HipsterDirectedGraph<String,Double> graph = GraphBuilder.newGraph()
     .from("A").to("B").withEdge(4d)
     .from("A").to("C").withEdge(2d)
     .from("B").to("C").withEdge(5d)
     .from("B").to("D").withEdge(10d)
     .from("C").to("E").withEdge(3d)
     .from("D").to("F").withEdge(11d)
     .from("E").to("D").withEdge(4d)
     .newDirectedGraph();

// Create the search problem. For graph problems, just use
// the GraphSearchProblem util class to generate the problem with ease.
SearchProblem p = GraphSearchProblem
                           .startingFrom("A")
                           .in(graph)
                           .takeCostsFromEdges()
                           .build();
                           
// Search the shortest path from "A" to "F" using Dijkstra
System.out.println(Hipster.createDijkstra(p).search("F"));
```
But that's not all. Hipster comes with different problem examples that illustrate how Hipster can be used to solve a wide variety of problems such as the eight puzzle problem or the N-Queens problem.

## What's next?

If you want to learn how to solve a problem by searching with Hipster, check the wiki to [learn the basics](https://github.com/citiususc/hipster/wiki/Getting-Started) and the [JavaDoc documentation](http://citiususc.github.io/hipster/documentation/javadoc/1.0.0-SNAPSHOT). There are also a few implemented examples here.

## License

This software is licensed under the Apache 2 license, quoted below.

    Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS),
    University of Santiago de Compostela (USC).

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    
    

