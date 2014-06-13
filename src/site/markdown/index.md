<div class="jumbotron">
    <div class="container">
        <center>
            <p><img alt="" src="img/hipster-no-text.png"></img></p>
            <p>An Open Source Java Library for Heuristic Search</p>
            <p>
                <a class="btn btn-blue btn-lg" href="http://goo.gl/9GCdtd" role="button">Download Hipster</a>
                <a class="btn btn-primary btn-lg" href="http://goo.gl/KRwd50" role="button">Download Source</a>
            </p>
        </center>
    </div>
</div>

---

## <i class="fa fa-rocket"></i> Powerful

Hipster is an easy to use yet powerful and flexible type-safe library for
heuristic search, written in pure Java. Hipster relies on a flexible model with generic operators that allow you to
reuse and change the behavior of the algorithms very easily. Just define how your problem
is and let Hipster do the job.

## <i class="fa fa-cogs"></i> Modular

All the components to define a search problem are clearly
defined and separated across different [Maven modules](http://maven.apache.org/). Just pick up
the components you need and create your algorithm from scratch or
by reusing other algorithms included in Hipster.

## <i class="fa fa-github-alt"></i> Open Source

Hipster comes with a permissive [Apache 2.0 license](license.html) that allows you
to use the library in a wide variety of commercial and
non-commercial projects. All the source code of this library
is hosted in [GitHub](http://www.github.com/citiususc/hipster).
Please feel free to contribute to this project by
reporting issues, requesting new features or by developing your own
algorithms.

---

## Quick Start

The easiest way to use Hipster is adding it as a dependency with your favourite dependency manager.
Maven users can include the library using the following snippet:

<script src="https://gist.github.com/pablormier/11350229.js"></script>

This gives you the full features of Hipster to start using, extending and improving search
algorithms. Here you have a simple example of how to find the shortest path in a directed graph with Hipster:

<script src="https://gist.github.com/pablormier/10107318.js"></script>

Hipster comes also with different adapters to work with [Blueprints](https://github.com/tinkerpop/blueprints)
and [JUNG](http://jung.sourceforge.net/) graph libraries. However,
Hipster is not limited to graph search. You can do much more!. Define your problem
in terms of actions, states and transitions and apply different search strategies to solve them fast!.
Check out some examples included in Hipster, like the N-Puzzle, N-Queens or Maze Search.

---

### About this project

During our PhD thesis, we have found that there is a lack of Java libraries
that implement search algorithms with an extensible, flexible and simple to use model.
Moreover, most of the libraries rely on graph structures or recursive implementations 
which do not offer fine-grained control over the
algorithm. This is why we decided to make our little contribution in this field.
Although Hipster is far from being perfect, we hope you find it useful.

Hipster was originally created and is currently maintained by:

-   **Pablo Rodriguez-Mier** - Computer Science PhD student at CITIUS.
-   **Adrian Gonzalez-Sieira** - Computer Science PhD student at CITIUS.

The scientific supervisors of this project are listed below:

-   **Dr. Manuel Mucientes**
-   **Dr. Manuel Lama**
-   **Dr. Alberto Bugarín**

The project was also accepted in the
[_9th Iberian Conference on Information Systems and Technologies (CISTI), 2014_](http://www.aisti.eu/cisti2014).
If you use this library in your research projects, we encourage you to please cite our
work :). Check the [citation](citation.html) page for more information.

### Related projects

Here you have a list of similar or complementary open source projects for Java which
you might find useful:

-   [**AIMA-JAVA**](https://code.google.com/p/aima-java/) - A comprehensive collection of different algorithms from the book _"Artificial Intelligence, A Modern Approach"_.
-   [**JUNG**](http://jung.sourceforge.net/) - A Java library for graph processing. It also includes shortest path algorithms such as Dijkstra or A\*.