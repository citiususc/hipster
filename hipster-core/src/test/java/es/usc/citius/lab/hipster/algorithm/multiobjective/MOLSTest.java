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
package es.usc.citius.lab.hipster.algorithm.multiobjective;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import es.usc.citius.lab.hipster.algorithm.multiobjective.example.QoSObjectiveNode;
import es.usc.citius.lab.hipster.algorithm.multiobjective.example.QoSObjectiveNodeFactory;
import es.usc.citius.lab.hipster.algorithm.multiobjective.example.QoSObjectives;
import es.usc.citius.lab.hipster.function.CostFunction;
import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.node.NodeFactory;
import es.usc.citius.lab.hipster.node.Transition;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertTrue;


public class MOLSTest {

    @Test
    public void testMultiobjectiveSearch() {
        // Create a MultiObjective graph with
        // two attributes: Response time and Throughput
        final Multimap<String, String> graph = HashMultimap.create();
        // Map with the QoS properties
        final Map<String, QoSObjectives> qos = new HashMap<String, QoSObjectives>();
        // Configure the graph
        graph.get("X").addAll(Arrays.asList("A", "B", "C", "D"));
        graph.get("A").add("E");
        graph.get("B").add("E");
        graph.get("C").add("E");
        graph.get("D").add("E");
        graph.get("E").addAll(Arrays.asList("F", "G"));
        graph.get("F").add("Y");
        graph.get("G").add("Y");
        //System.out.println(graph.toString());
        // Set the QoS attributes for each node
        qos.put("A", new QoSObjectives(60, 400));
        qos.put("B", new QoSObjectives(5, 50));
        qos.put("C", new QoSObjectives(80, 500));
        qos.put("D", new QoSObjectives(10, 60));
        qos.put("E", new QoSObjectives(50, 500));
        qos.put("F", new QoSObjectives(80, 50));
        qos.put("G", new QoSObjectives(80, 400));
        qos.put("X", new QoSObjectives(0, Integer.MAX_VALUE));
        qos.put("Y", new QoSObjectives(0, Integer.MAX_VALUE));

        // Create the MOLS
        TransitionFunction<String> t = new TransitionFunction<String>() {
            public Iterable<Transition<String>> from(String current) {
                return Transition.map(current, graph.get(current));
            }
        };
        CostFunction<String, QoSObjectives> evaluator = new CostFunction<String, QoSObjectives>() {
            public QoSObjectives evaluate(Transition<String> transition) {
                return qos.get(transition.to());
            }

        };
        NodeFactory<String, MultiObjectiveNode<String>> factory = new QoSObjectiveNodeFactory<String>(evaluator);
        MultiObjectiveLS<String> algorithm = new MultiObjectiveLS<String>("X", t, factory);
        Map<String, Collection<MultiObjectiveNode<String>>> solution = algorithm.search();

        // Non-dominated solutions (expected)
        Set<QoSObjectives> expected = new HashSet<QoSObjectives>();
        expected.add(new QoSObjectives(190d, 400));
        expected.add(new QoSObjectives(135d, 50));
        expected.add(new QoSObjectives(140d, 60));
        expected.add(new QoSObjectives(135d, 50));

        // Read all non dominated paths to Y
        for (MultiObjectiveNode<String> n : solution.get("Y")) {
            QoSObjectiveNode<String> node = (QoSObjectiveNode<String>) n;
            assertTrue("Solution not expected - " + node.objectives, expected.contains(node.objectives));
            //System.out.println(node.objectives.responseTime + " RT / " + node.objectives.throughput + " TH - " + node.path());
        }
    }

}
