![Hipster](src/main/doclava/custom/assets/hipster-template/assets/images/header-logo.png?raw=true)

A powerful, easy to use heuristic search library implemented in pure java.

## Goals

The aim of Hipster is to provide an easy to use yet powerful and flexible Java library for heuristic search. 
Hipster relies on a flexible model with generic operators to change the behavior without modifying the internals. All algorithms are also implemented in an iterative way, avoiding recursion. This has many benefits: full control over the search, access to the internals at runtime or a better and clear scale-out for large search spaces using the heap memory. 

## Usage

Maven users can include Hipster as a dependency using the following snippet:

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
    
    

