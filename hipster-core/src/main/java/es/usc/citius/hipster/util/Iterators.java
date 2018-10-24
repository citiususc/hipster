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
package es.usc.citius.hipster.util;


import java.util.Iterator;
import java.util.NoSuchElementException;

public final class Iterators {

    public static abstract class AbstractIterator<E> implements Iterator<E> {
        protected E current;

        protected AbstractIterator(){}

        protected abstract E computeNext();

        @Override
        public boolean hasNext() {
            if (current == null){
                current = computeNext();
            }
            return current != null;
        }

        @Override
        public E next() {
            E next;
            if (current != null) {
                next = current;
                current = null;
                return next;
            } else {
                next = computeNext();
                if (next == null) throw new NoSuchElementException("next");
                return next;
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove");
        }
    }

    public static <E> Iterator<E> empty() {
        return new AbstractIterator<E>() {
            @Override
            protected E computeNext() {
                return null;
            }
        };
    }
}
