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
 * A scalar function takes an object and modifies its magnitude by a 
 * numeric factor without changing its type.
 * </p>
 * This interface can be used to do abstract definitions of the scaling
 * operation for algorithms that need it without explicitly defining them
 * inside the algorithms, making them easily replaceable when the cost definition
 * changes.
 * </p>
 * The definition of scalar functions requires the implementation of the {@link #scale(Object, double)}.
 * Here is an example for the {@link Double} scaling operation:
 * <pre>
 *     {@code 
 *     new ScalarFunction<Double>(){
 *              Double scale(Double a, double b){
 *                  return a*b;
 *              }
 *          }
 *     }
 * </pre>
 *
 * To create a default scalar function to operate with doubles,
 * simple use {@link es.usc.citius.hipster.model.function.impl.Product}.
 *
 * @param <T> type of the domain of the function
 * 
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 * @author Adrián González Sieira <<a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 * @since 0.1.0
 */
public interface ScalarFunction<T> {
    /**
     * Scale operation.
     *
     * @param a value to be scaled
     * @param b scale factor
     * @return {@literal a} scaled by a factor of {@literal b}.
     */
    T scale(T a, double b);
}
