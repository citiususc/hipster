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

package es.usc.citius.lab.hipster.node.adstar;

import java.util.Map;

import es.usc.citius.lab.hipster.node.Node;
import es.usc.citius.lab.hipster.node.Transition;

/**
 *
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @since 01-04-2013
 * @version 1.0
 */
public interface ADStarNodeUpdater<S, N extends Node<S>> {

    public boolean updateConsistent(N node, N parent, Transition<S> transition);
    
    public boolean updateInconsistent(N node, Map<Transition<S>, N> predecessorsNodes);
    
    public void setMaxV(N node);
    
    public void setEpsilon(Double epsilon);
}
