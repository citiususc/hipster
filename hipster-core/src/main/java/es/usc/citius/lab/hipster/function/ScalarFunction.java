/*
 * Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS), University of Santiago de Compostela (USC).
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
package es.usc.citius.lab.hipster.function;

/**
 * This interface is used to define abstract scalar functions.Example:
 * <pre>
 *     {@code new ScalarFunction<Double>(){
 *              Double scale(Double a, double b){
 *                  return a*b;
 *              }
 *          }
 *     }
 * </pre>
 *
 * @param <A> type of the domain of the function.
 * @author Pablo Rodríguez Mier
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
