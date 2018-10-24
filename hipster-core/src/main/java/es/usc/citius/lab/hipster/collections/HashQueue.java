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
package es.usc.citius.lab.hipster.collections;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Implementation of a {@link java.util.Queue} backed by a {@link java.util.LinkedHashSet}
 *
 * @param <S> the type of elements held in this collection
 */
public class HashQueue<S> extends AbstractQueue<S> {

    private final Set<S> elements = new LinkedHashSet<S>();
    private S first = null;

    @Override
    public boolean offer(S e) {
        elements.add(e);
        if (first == null) {
            first = e;
        }
        return true;
    }

    @Override
    public S poll() {
        // Remove the first element
        elements.remove(first);
        S out = first;
        // Reasign first
        first = (elements.isEmpty()) ? null : elements.iterator().next();
        return out;
    }

    @Override
    public S peek() {
        return first;
    }

    @Override
    public Iterator<S> iterator() {
        return elements.iterator();
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public boolean contains(Object o) {
        return this.elements.contains(o);
    }

}
