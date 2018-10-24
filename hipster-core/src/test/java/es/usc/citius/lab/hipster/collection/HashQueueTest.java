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
package es.usc.citius.lab.hipster.collection;


import es.usc.citius.lab.hipster.collections.HashQueue;
import org.junit.Test;

import java.util.Iterator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class HashQueueTest {
    @Test
    public void testOffer() throws Exception {
        HashQueue<String> queue = new HashQueue<String>();
        queue.offer("element");
        assertEquals(1, queue.size());
    }

    @Test
    public void testPoll() throws Exception {
        HashQueue<String> queue = new HashQueue<String>();
        queue.offer("element");
        assertEquals("element", queue.poll());
        assertEquals(0, queue.size());
    }

    @Test
    public void testPeek() throws Exception {
        HashQueue<String> queue = new HashQueue<String>();
        queue.offer("element");
        assertEquals("element", queue.peek());
        assertEquals(1, queue.size());
    }

    @Test
    public void testIterator() throws Exception {
        HashQueue<String> queue = new HashQueue<String>();
        String[] elements = new String[]{"element-1","element-2","element-3"};
        queue.offer(elements[0]);
        queue.offer(elements[1]);
        queue.offer(elements[2]);
        Iterator<String> it = queue.iterator();
        assertTrue(it.hasNext());
        int i=0;
        while(it.hasNext()){
            assertEquals(elements[i++],it.next());
        }
        assertEquals(3, queue.size());
    }

    @Test
    public void testContains() throws Exception {
        HashQueue<String> queue = new HashQueue<String>();
        String[] elements = new String[]{"element-1","element-2","element-3"};
        queue.offer(elements[0]);
        queue.offer(elements[1]);
        queue.offer(elements[2]);
        assertTrue(queue.contains("element-1"));
        assertTrue(queue.contains("element-2"));
        assertTrue(queue.contains("element-3"));
    }
}
