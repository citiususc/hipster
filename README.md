![Hipster](src/main/doclava/custom/assets/hipster-template/assets/images/header-logo.png?raw=true)

A powerful, easy to use heuristic search library implemented in pure java.

## Goals

The aim of Hipster is to provide an easy to use yet powerful and flexible Java library for heuristic search. 
Hipster relies on a flexible model with generic operators to change the behavior without modifying the internals. All algorithms are also implemented in an iterative way, avoiding recursion. This has many benefits: full control over the search, access to the internals at runtime or a better and clear scale-out for large search spaces using the heap memory.

## Algorithms

The current version of the library comes with some very well-known and wide used search algorithms. Note that this list may not be exhaustive:

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

If you don't find the algorithm you are looking for, please consider contributing to Hipster!. You can open a new issue with the details of the algorithm or better fork this repository and create a pull request with your contribution. If you go for this second option, please follow the contribution guidelines.

## Getting started
TODO;

## Usage

The easiest way to use Hipster is adding it as a dependency with your favourite dependency manager.
Maven users can include the library using the following snippet:

````xml
<dependency>
    <groupId>es.usc.citius</groupId>
    <artifactId>hipster</artifactId>
    <version>${hipster.version}</version>
</dependency>
````

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
    
    

