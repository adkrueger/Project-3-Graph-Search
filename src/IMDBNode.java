import java.util.Collection;
import java.util.HashMap;

public class IMDBNode implements Node {
    private String _name;
    private HashMap<String, IMDBNode> _IMDBNodes = new HashMap<>();

    /**
     * Creates an Actor object with a name and a list of movies they starred in
     * @param name the name of the actor/movie
     */
    IMDBNode (String name) {
        _name = name;
    }

    /**
     * Returns the name of the node (e.g., "Judy Garland").
     * @return the name of the Node.
     */
    public String getName() {
        return _name;
    }

    /**
     * Returns the Collection of neighbors of the node.
     * @return the Collection of all the neighbors of this Node.
     */
    public Collection<? extends Node> getNeighbors() {
        return _IMDBNodes.values();
    }

    /**
     * Adds a node to our nodes, if not already preset
     * @param IMDBnode the Node to be added
     */
    void addNode(IMDBNode IMDBnode) {
        _IMDBNodes.putIfAbsent(IMDBnode.getName(), IMDBnode);
    }
}
