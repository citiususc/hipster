/**
 * Copyright (C) 2013-2018 Centro de Investigación en Tecnoloxías da Información (CITIUS) (http://citius.usc.es)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * <p>
 * Util classes to create a new search problem. Use {@link es.usc.citius.hipster.model.problem.ProblemBuilder}
 * to define a search problem required to instantiate a new algorithm with {@link es.usc.citius.hipster.algorithm.Hipster}
 * class. Example:
 * </p>
 * <pre class="prettyprint">
 *     {@code
 *     Hipster.SearchProblem p =
 *          ProblemBuilder.create()
 *              .initialState(initialState)
 *              .defineProblemWithExplicitActions()
 *                  .useActionFunction(af)
 *                  .useTransitionFunction(atf)
 *                  .useCostFunction(cf)
 *                  .useHeuristicFunction(hf)
 *                  .build();
 *
 *     Hipster.createAStar(p).search(goalState);
 *     }
 * </pre>
 */
package es.usc.citius.hipster.model.problem;