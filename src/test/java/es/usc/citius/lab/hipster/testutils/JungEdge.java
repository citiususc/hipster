package es.usc.citius.lab.hipster.testutils;

import edu.uci.ics.jung.graph.DirectedGraph;
import java.awt.Point;

/**
 * Class only for test purposes: definition of a JungEdge to create a
 * {@link DirectedGraph}.
 *
 * @author Adrián González Sieira
 * @since 26-03-2013
 * @version 1.0
 */
public class JungEdge {

    private Point source, dest;
    private Double cost;

    public JungEdge(Point source, Point dest, Double cost) {
        this.source = source;
        this.dest = dest;
        this.cost = cost;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + (this.source != null ? this.source.hashCode() : 0);
        hash = 17 * hash + (this.dest != null ? this.dest.hashCode() : 0);
        hash = 17 * hash + (this.cost != null ? this.cost.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final JungEdge other = (JungEdge) obj;
        if (this.source != other.source && (this.source == null || !this.source.equals(other.source))) {
            return false;
        }
        if (this.dest != other.dest && (this.dest == null || !this.dest.equals(other.dest))) {
            return false;
        }
        if (this.cost != other.cost && (this.cost == null || !this.cost.equals(other.cost))) {
            return false;
        }
        return true;
    }

    public Double getCost() {
        return cost;
    }

    public Point getSource() {
        return source;
    }

    public Point getDest() {
        return dest;
    }
}
