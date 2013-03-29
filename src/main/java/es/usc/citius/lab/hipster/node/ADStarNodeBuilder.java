package es.usc.citius.lab.hipster.node;

import java.util.HashMap;
import java.util.Map;

/**
 * Builder to create instances of {@link ADStarNode}.
 *
 * @author Adrián González Sieira
 * @since 27-03-2013
 * @version 1.0
 */
public class ADStarNodeBuilder<S> implements NodeBuilder<S, ADStarNode<S>> {

    private Map<S, ADStarNode<S>> instances;
    
    /**
     * Default constructor for this class: initializes the instances map.
     */
    public ADStarNodeBuilder() {
        this.instances = new HashMap<S, ADStarNode<S>>();
    }
    
    /**
     * Method to return node instances: if the node is visited before,
     * the previous instance is returned; else, a new one is created.
     * @param from parent node
     * @param transition incoming transition
     * @return instance of {@link ADStarNode}
     */
    public ADStarNode<S> node(ADStarNode<S> from, Transition<S> transition) {
        if (this.instances.containsKey(transition.to())) {
            /*Node visited before: returning instance created.*/
            return this.instances.get(transition.to());
        } else {
            /*Node never visited: returning new instance.*/
            ADStarNode<S> node = new ADStarNode<S>(transition, from);
            node.setG(Double.POSITIVE_INFINITY);
            node.setV(Double.POSITIVE_INFINITY);
            node.previousNode = null;
            return node;
        }
    }
}
