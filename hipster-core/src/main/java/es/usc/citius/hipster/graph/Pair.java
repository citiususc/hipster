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
package es.usc.citius.hipster.graph;


public class Pair<E> {
    private E e1, e2;

    public Pair(E e1, E e2) {
        if (e1 == null) throw new IllegalArgumentException("First element cannot be null");
        this.e1 = e1;
        if (e2 == null) throw new IllegalArgumentException("Second element cannot be null");
        this.e2 = e2;
    }

    public E _1() {
        return e1;
    }

    public E _2() {
        return e2;
    }

    public E getE1() {
        return e1;
    }

    public void setE1(E e1) {
        this.e1 = e1;
    }

    public E getE2() {
        return e2;
    }

    public void setE2(E e2) {
        this.e2 = e2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair<?> pair = (Pair<?>) o;

        if (!e1.equals(pair.e1)) return false;
        return e2.equals(pair.e2);

    }

    @Override
    public int hashCode() {
        int result = e1.hashCode();
        result = 31 * result + e2.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "(" + e1 + ", " + e2 + ")";
    }
}
