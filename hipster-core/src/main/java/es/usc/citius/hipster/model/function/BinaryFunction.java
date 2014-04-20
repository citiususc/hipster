/*
 * Copyright 2014 CITIUS <http://citius.usc.es>, University of Santiago de Compostela.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package es.usc.citius.hipster.model.function;

/**
 * A binary operation takes two elements of the same type
 * and combines them returning an element of the same type. 
 * </p>
 * This interface is useful to define abstract arithmetic functions 
 * used by the search algorithms (i. e. cost addition) and avoid the explicit
 * definition of the operations in the algorithm. When the cost definition changes,
 * the binary function can be changed without modifying the implementation
 * of the algorithms.
 * </p>
 * The abstract definition of binary operations requires to complete the method
 * {@link #apply(Object, Object)}, which returns an object of the same type. Here is 
 * an example for the {@link Double} addition operation:
 *
 * <pre class="prettyprint">
 * new BinaryFunction<Double>(){
 *          Double apply(Double a, Double b){
 *              return a + b;
 *          }
 *     }
 * </pre>
 * 
 * @param <T> type of the domain of the function
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 * @author Adrián González Sieira <<a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 * @since 0.1.0
 */
public interface BinaryFunction<T> {
	
	/**
	 * Combination of two elements of the same type,
	 * returning an element of the same type.
	 * 
	 * @param a first element
	 * @param b second element
	 * @return result of the combination
	 */
    T apply(T a, T b);
    
}
