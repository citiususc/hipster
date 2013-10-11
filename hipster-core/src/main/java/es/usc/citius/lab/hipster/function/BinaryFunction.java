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
 * Defines a binary operation. The binary operation interface is
 * useful for defining abstract functions such as arithmetic functions
 * for the search algorithms. It takes two elements
 * and obtains a result of the same type.
 *
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @author Pablo Rodríguez Mier <pablo.rodriguez.mier@usc.es>
 *
 * @param <T> operator class
 */
public interface BinaryFunction<T> {
    T apply(T a, T b);
}
