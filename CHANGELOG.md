# Change Log

## [1.0.1](https://github.com/citiususc/hipster/tree/1.0.1) (2016-05-16)
[Full Changelog](https://github.com/citiususc/hipster/compare/v1.0.0...v1.0.1)

**Fixed bugs:**

- open queue does not manage repeated elements [\#170](https://github.com/citiususc/hipster/issues/170)

**Closed issues:**

- Add heuristic to maze example [\#165](https://github.com/citiususc/hipster/issues/165)
- Implementation of Annealing localsearch [\#158](https://github.com/citiususc/hipster/issues/158)

## [v1.0.0](https://github.com/citiususc/hipster/tree/v1.0.0) (2016-02-22)
[Full Changelog](https://github.com/citiususc/hipster/compare/v1.0.0-rc2...v1.0.0)

**Implemented enhancements:**

- Complete javadoc documentation for hipster.algorithm.problem \(core\) [\#5](https://github.com/citiususc/hipster/issues/5)
- Complete javadoc documentation for hipster.algorithm.multiobjective \(core\) [\#4](https://github.com/citiususc/hipster/issues/4)
- Add a depth or path size attribute to nodes [\#140](https://github.com/citiususc/hipster/issues/140)
- Replace Stack with ArrayDeque [\#137](https://github.com/citiususc/hipster/issues/137)
- Detect negative cycle conditions in BellmanFord [\#136](https://github.com/citiususc/hipster/issues/136)
- Update scripts to auto-generate javadocs for snapshot versions [\#121](https://github.com/citiususc/hipster/issues/121)

**Fixed bugs:**

- When Search Result printed Action list is now in reverse order \(in v1.0.0.rc2 vs v1.0.0.rc1\) [\#141](https://github.com/citiususc/hipster/issues/141)
- Fix coveralls maven plugin [\#131](https://github.com/citiususc/hipster/issues/131)

**Closed issues:**

- Update gitignore file to include Eclipse editor files [\#146](https://github.com/citiususc/hipster/issues/146)
- When checking if point in bounds, also check lower bounds. [\#144](https://github.com/citiususc/hipster/issues/144)
- Integration with codecov.io [\#152](https://github.com/citiususc/hipster/issues/152)
- Replace Cobertura with JaCoCo [\#151](https://github.com/citiususc/hipster/issues/151)
- Change maven compiler version to 1.6 [\#150](https://github.com/citiususc/hipster/issues/150)
- Fix incorrect URL for javadoc publication [\#149](https://github.com/citiususc/hipster/issues/149)
- Remove unused citius-nexus-snapshot in maven-settings.xml [\#148](https://github.com/citiususc/hipster/issues/148)
- Upgrade config to deploy on bintray [\#143](https://github.com/citiususc/hipster/issues/143)
- Show unit time \(ms\) in algorithm result toString\(\) method [\#139](https://github.com/citiususc/hipster/issues/139)
- Detect "NoSuchElementException" situations in AbstractIterator [\#138](https://github.com/citiususc/hipster/issues/138)

**Merged pull requests:**

- Implementation of the Annealing search algorithm [\#168](https://github.com/citiususc/hipster/pull/168) ([cmoins](https://github.com/cmoins))
- Semicolon bug [\#167](https://github.com/citiususc/hipster/pull/167) ([andyg7](https://github.com/andyg7))
- Contrib/165 [\#166](https://github.com/citiususc/hipster/pull/166) ([PaulJackson123](https://github.com/PaulJackson123))
- Implementation of Depth Limited Search \#157 [\#164](https://github.com/citiususc/hipster/pull/164) ([gabizekany](https://github.com/gabizekany))
- Contrib/issue 64 [\#160](https://github.com/citiususc/hipster/pull/160) ([michaelhaaf](https://github.com/michaelhaaf))
- Contrib/issue 137 [\#156](https://github.com/citiususc/hipster/pull/156) ([michaelhaaf](https://github.com/michaelhaaf))
- fix \#146 : Update .gitignore to include Eclipse specific files. [\#147](https://github.com/citiususc/hipster/pull/147) ([gahrae](https://github.com/gahrae))
- fix \#144 : Make Maze2D.pointInBounds\(\) check lower bounds too. [\#145](https://github.com/citiususc/hipster/pull/145) ([gahrae](https://github.com/gahrae))

## [v1.0.0-rc2](https://github.com/citiususc/hipster/tree/v1.0.0-rc2) (2015-08-12)
[Full Changelog](https://github.com/citiususc/hipster/compare/v1.0.0-rc1...v1.0.0-rc2)

**Implemented enhancements:**

- Fix deploy script to deploy only SNAPSHOT versions [\#111](https://github.com/citiususc/hipster/issues/111)
- Update travis.yml to use the new container-based CI [\#135](https://github.com/citiususc/hipster/issues/135)
- StateTransitionFunction is not lazy [\#134](https://github.com/citiususc/hipster/issues/134)
- LazyActionStateTransitionFunction is not lazy [\#133](https://github.com/citiususc/hipster/issues/133)
- LazyNodeExpander is not lazy [\#132](https://github.com/citiususc/hipster/issues/132)
- Create a branch whitelist in travis.yml [\#129](https://github.com/citiususc/hipster/issues/129)
- Refactor the basic \(non-guava\) graph implementations and improve unit tests [\#128](https://github.com/citiususc/hipster/issues/128)
- Confusing usage of the GraphBuilder [\#119](https://github.com/citiususc/hipster/issues/119)
- Remove dependencies with Guava from hipster-core [\#113](https://github.com/citiususc/hipster/issues/113)

**Fixed bugs:**

- Java \<1.8 Incompatibility [\#130](https://github.com/citiususc/hipster/issues/130)
- BellmanFord iterator search method fails [\#127](https://github.com/citiususc/hipster/issues/127)
- NullPointerException using Bellman-Ford algorithm [\#124](https://github.com/citiususc/hipster/issues/124)
- RomaniaaProblemOptimalSearchTest does not detect solutions with different length [\#123](https://github.com/citiususc/hipster/issues/123)
- AD\* node expander check isConsistent\(\) after node.g and node.v changed [\#122](https://github.com/citiususc/hipster/issues/122)
- assert check in BinaryOperation constructor done with `equals\(\)` instead of `compare\(\)` [\#120](https://github.com/citiususc/hipster/issues/120)
- restrictive type in ProblemBuilder\(...\)defineProblemWithoutExplicitActions\(\).useTransitionFunction\(\) [\#115](https://github.com/citiususc/hipster/issues/115)
- change visibility of attributes and methods in Algorithm subclasses [\#114](https://github.com/citiususc/hipster/issues/114)
- AbstractNode.path\(\) in reversed order [\#72](https://github.com/citiususc/hipster/issues/72)
- Method search\(SearchListener\) from Algorithms does not stop [\#49](https://github.com/citiususc/hipster/issues/49)

**Closed issues:**

- Create new module "hipster-extensions" with classes depending on Guava [\#125](https://github.com/citiususc/hipster/issues/125)
- Update `site.url` in parent pom.xml [\#126](https://github.com/citiususc/hipster/issues/126)
- hipster.version in branch development should not be the same than last release [\#118](https://github.com/citiususc/hipster/issues/118)
- hipster.version defined in root pom.xmi but not used [\#117](https://github.com/citiususc/hipster/issues/117)
- Implement common tests for heuristic search algorithms [\#71](https://github.com/citiususc/hipster/issues/71)

## [v1.0.0-rc1](https://github.com/citiususc/hipster/tree/v1.0.0-rc1) (2014-12-10)
[Full Changelog](https://github.com/citiususc/hipster/compare/v1.0.0-alpha-1...v1.0.0-rc1)

**Implemented enhancements:**

- Complete package-index descriptions [\#86](https://github.com/citiususc/hipster/issues/86)
- Bump doxia version to 1.6 [\#85](https://github.com/citiususc/hipster/issues/85)
- Add DFS cycle support [\#84](https://github.com/citiususc/hipster/issues/84)
- Add a status bar at the bottom of the ASCII Visualizer [\#79](https://github.com/citiususc/hipster/issues/79)
- Add option to disable the ASCII Visualizer realtime printing [\#76](https://github.com/citiususc/hipster/issues/76)
- Add support for custom goal test conditions \[CISTI2014\] [\#70](https://github.com/citiususc/hipster/issues/70)
- Modify action/state function interfaces to use nodes to compute successors [\#68](https://github.com/citiususc/hipster/issues/68)
- Search Problems should provide an initial node and the node expander [\#67](https://github.com/citiususc/hipster/issues/67)
- Remove redundant algorithm factories [\#60](https://github.com/citiususc/hipster/issues/60)
- Make Nodes generic in each algorithm implementation [\#57](https://github.com/citiususc/hipster/issues/57)
- Implement abstract definition for graph-based problems [\#56](https://github.com/citiususc/hipster/issues/56)
- Create a node transition function [\#53](https://github.com/citiususc/hipster/issues/53)
- Clean algorithm factory duplications [\#50](https://github.com/citiususc/hipster/issues/50)
- Rename Algorithms class to Hipster [\#48](https://github.com/citiususc/hipster/issues/48)
- Cannot access the heuristic from HeuristicNode [\#45](https://github.com/citiususc/hipster/issues/45)
- Change factory.node to factory.makeNode [\#42](https://github.com/citiususc/hipster/issues/42)
- Reorganize maven modules [\#39](https://github.com/citiususc/hipster/issues/39)
- Pretty 8-puzzle string representation [\#26](https://github.com/citiususc/hipster/issues/26)
- Complete javadoc documentation for hipster.util.parallel \(core\) [\#14](https://github.com/citiususc/hipster/issues/14)
- Complete javadoc documentation for hipster.util.maze \(core\) [\#13](https://github.com/citiususc/hipster/issues/13)
- Complete javadoc documentation for hipster.algorithm.node.impl \(core\) [\#12](https://github.com/citiususc/hipster/issues/12)
- Complete javadoc documentation for hipster.algorithm.node.adstar \(core\) [\#11](https://github.com/citiususc/hipster/issues/11)
- Complete javadoc documentation for hipster.algorithm.node \(core\) [\#10](https://github.com/citiususc/hipster/issues/10)
- Complete javadoc documentation for hipster.algorithm.function.impl [\#9](https://github.com/citiususc/hipster/issues/9)
- Complete javadoc documentation for hipster.algorithm.impl \(core\) [\#8](https://github.com/citiususc/hipster/issues/8)
- Complete javadoc documentation for hipster.function \(core\) [\#7](https://github.com/citiususc/hipster/issues/7)
- Complete javadoc documentation for hipster.collection [\#6](https://github.com/citiususc/hipster/issues/6)
- Complete javadoc documentation for hipster.algorithm.factory \(core\) [\#3](https://github.com/citiususc/hipster/issues/3)
- Complete javadoc documentation for hipster.algorithm.combinatorial \(core\) [\#2](https://github.com/citiususc/hipster/issues/2)
- Complete javadoc documentation for hipster.algorithm \(core\) [\#1](https://github.com/citiususc/hipster/issues/1)

**Fixed bugs:**

- Fix google analytics tracking code [\#90](https://github.com/citiususc/hipster/issues/90)
- Fix download links in the main web page [\#89](https://github.com/citiususc/hipster/issues/89)
- Fix link to milestones in README.md [\#88](https://github.com/citiususc/hipster/issues/88)
- Twitter icon is missing in the website [\#80](https://github.com/citiususc/hipster/issues/80)
- IDA\* minFLimit inconsistent updates [\#74](https://github.com/citiususc/hipster/issues/74)
- Fix NQueens.java getLineSeparator\(\) incompatible with jdk 6 [\#69](https://github.com/citiususc/hipster/issues/69)
- Variable num of iters with Dijkstra/A\*/IDA\* after refactor [\#63](https://github.com/citiususc/hipster/issues/63)
- Bad value of HeuristicNode.getScore\(\) when the initial node is instantiated by HeuristicNodeImplFactory [\#52](https://github.com/citiususc/hipster/issues/52)
- Fix A\* cost comparator [\#43](https://github.com/citiususc/hipster/issues/43)
- SetCoverIterator fails when there is only one element [\#35](https://github.com/citiususc/hipster/issues/35)

**Closed issues:**

- Generate maven site with markdown with reflow maven skin [\#31](https://github.com/citiususc/hipster/issues/31)
- Update bash scripts for automatic deployment [\#112](https://github.com/citiususc/hipster/issues/112)
- collections / adapters package - Javadoc documentation [\#110](https://github.com/citiususc/hipster/issues/110)
- util.graph package - Javadoc documentation [\#109](https://github.com/citiususc/hipster/issues/109)
- util.examples / util.examples.maze - Javadoc documentation [\#108](https://github.com/citiususc/hipster/issues/108)
- thirdparty package - Javadoc documentation [\#107](https://github.com/citiususc/hipster/issues/107)
- model.problem package - Javadoc documentation [\#106](https://github.com/citiususc/hipster/issues/106)
- model.impl package - Javadoc documentation [\#105](https://github.com/citiususc/hipster/issues/105)
- model.function.impl package - Javadoc documentation [\#104](https://github.com/citiususc/hipster/issues/104)
- model.function package - Javadoc documentation [\#103](https://github.com/citiususc/hipster/issues/103)
- model package - Javadoc documentation [\#102](https://github.com/citiususc/hipster/issues/102)
- examples.problem package - Javadoc documentation [\#101](https://github.com/citiususc/hipster/issues/101)
- examples package - Javadoc documentation [\#100](https://github.com/citiususc/hipster/issues/100)
- localsearch package - Javadoc documentation [\#99](https://github.com/citiususc/hipster/issues/99)
- MultiobjectiveLS / Iterator - Javadoc documentation \(package Algorithm\) [\#98](https://github.com/citiususc/hipster/issues/98)
- IDAStar / Iterator - Javadoc documentation \(package Algorithm\) [\#97](https://github.com/citiususc/hipster/issues/97)
- Hipster class - Javadoc documentation \(package Algorithm\) [\#96](https://github.com/citiususc/hipster/issues/96)
- DFS Algorithm - Javadoc documentation \(package Algorithm\) [\#95](https://github.com/citiususc/hipster/issues/95)
- BFS Algorithm - Javadoc documentation \(package Algorithm\) [\#94](https://github.com/citiususc/hipster/issues/94)
- BellmanFord / Iterator - Javadoc documentation \(package Algorithm\) [\#93](https://github.com/citiususc/hipster/issues/93)
- ADStarForward / Iterator - Javadoc documentation \(package Algorithm\) [\#92](https://github.com/citiususc/hipster/issues/92)
- Fix SearchResult toString method [\#87](https://github.com/citiususc/hipster/issues/87)
- Add an example using the ProblemBuilder in README.md [\#82](https://github.com/citiususc/hipster/issues/82)
- Create a Swing-based Maze search visualizer [\#73](https://github.com/citiususc/hipster/issues/73)
- Adapt AD\* to the new generic action node model [\#66](https://github.com/citiususc/hipster/issues/66)
- Auto deploy javadoc to gh-pages with Maven [\#61](https://github.com/citiususc/hipster/issues/61)
- Enforced Hill Climbing [\#55](https://github.com/citiususc/hipster/issues/55)
- Create N-queen example [\#54](https://github.com/citiususc/hipster/issues/54)
- Publish 1.0.0-rc1 artifacts to the central repository [\#40](https://github.com/citiususc/hipster/issues/40)
- IterativeSetCover does not fulfill the iterator contract [\#38](https://github.com/citiususc/hipster/issues/38)
- Insert pluginManagement in parent pom [\#37](https://github.com/citiususc/hipster/issues/37)
- Move examples to a hipster-examples package [\#36](https://github.com/citiususc/hipster/issues/36)
- Implement IDA\* algorithm [\#34](https://github.com/citiususc/hipster/issues/34)
- Customize doclava css for javadoc [\#30](https://github.com/citiususc/hipster/issues/30)
- Add test for HashQueue [\#29](https://github.com/citiususc/hipster/issues/29)
- Prepare the release of the 0.1.0 version [\#28](https://github.com/citiususc/hipster/issues/28)
- Maze search examples with realtime output printing [\#27](https://github.com/citiususc/hipster/issues/27)
- Create README.md with markdown [\#21](https://github.com/citiususc/hipster/issues/21)
- 8-Puzzle example with different heuristics [\#16](https://github.com/citiususc/hipster/issues/16)
- Implementation of the breadth-first-search algorithm [\#15](https://github.com/citiususc/hipster/issues/15)

## [v1.0.0-alpha-1](https://github.com/citiususc/hipster/tree/v1.0.0-alpha-1) (2014-05-21)


\* *This Change Log was automatically generated by [github_changelog_generator](https://github.com/skywinder/Github-Changelog-Generator)*