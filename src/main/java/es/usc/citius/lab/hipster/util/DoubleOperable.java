/*
 * Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.usc.citius.lab.hipster.util;

/**
 * Implementation of interface Operable for Double values.
 *
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @since 16-04-2013
 * @version 1.0
 */
public class DoubleOperable implements Scalable<DoubleOperable>{

    protected double value;
    public static final DoubleOperable MAX = new DoubleOperable(Double.POSITIVE_INFINITY);
    public static final DoubleOperable MIN = new DoubleOperable(0);

    public DoubleOperable(double value) {
        this.value = value;
    }
    
    public DoubleOperable add(DoubleOperable operator) {
        return new DoubleOperable(value + operator.value);
    }

    public DoubleOperable scale(double operator) {
        return new DoubleOperable(value * operator);
    }

    public int compareTo(DoubleOperable o) {
        return Double.compare(value, o.value);
    }
    
    public double getValue(){
    	return this.value;
    }

    @Override
    public String toString() {
        return new Double(value).toString();
    }

    public DoubleOperable neutral() {
        return MIN;
    }

    public DoubleOperable max() {
        return MAX;
    }
}
