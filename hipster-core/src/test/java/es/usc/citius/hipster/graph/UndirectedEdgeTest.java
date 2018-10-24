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

import org.junit.Test;

import static org.junit.Assert.*;

public class UndirectedEdgeTest {

    @Test
    public void testEqualsWithUndirectedEdgeSwapped() throws Exception {
        UndirectedEdge e1 = new UndirectedEdge("a", "b", 1);
        UndirectedEdge e2 = new UndirectedEdge("b", "a", 1);
        assertEquals(e1, e2);
    }

    @Test
    public void testEqualsWithUndirectedEdge() throws Exception {
        UndirectedEdge e1 = new UndirectedEdge("a", "b", 1);
        UndirectedEdge e2 = new UndirectedEdge("a", "b", 1);
        assertEquals(e1, e2);
    }

    @Test
    public void testNotEqualsWithUndirectedEdge() throws Exception {
        UndirectedEdge e1 = new UndirectedEdge("a", "b", 1);
        UndirectedEdge e2 = new UndirectedEdge("a", "c", 1);
        assertNotEquals(e1, e2);
    }
}