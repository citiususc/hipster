package es.usc.citius.lab.hipster.util;

import es.usc.citius.lab.hipster.node.Node;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * From a list of {@link Node} elements obtain the associated states.
 *
 * @author Adrián González Sieira
 * @since 26-03-2013
 * @version 1.0
 */
public class NodeToStateListConverter<S> {

    /**
     * Obtains a List of states from the list of nodes that forms a path.
     * @param nodeList list of {@link Node}
     * @return list of states
     */
    public List<S> convert(List<Node<S>> nodeList) {
        List<S> stateList = new ArrayList<S>(nodeList.size());
        for (Iterator<Node<S>> it = nodeList.iterator(); it.hasNext();) {
            Node<S> current = it.next();
            stateList.add(current.transition().state());
        }
        return stateList;
    }
}
