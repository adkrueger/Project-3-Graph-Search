import java.util.*;

public class GraphSearchEngineImpl implements GraphSearchEngine {

    /**
     * Finds a shortest path, if one exists, between nodes s and t. The path will be a list: (s, ..., t). If no
     * path exists, then this method will return null.
     *
     * @param s the start node.
     * @param t the target node.
     * @return a shortest path in the form of a List of Node objects or null if no path exists.
     */
    public List<Node> findShortestPath(Node s, Node t) {
        HashMap<Node, Node> parentNode = new HashMap<>();
        HashMap<String, Integer> distanceFromS = new HashMap<>();
        LinkedList<Node> nodesToVisit = new LinkedList<>();
        ArrayList<Node> shortestPath = new ArrayList<>();

        nodesToVisit.add(s);
        distanceFromS.putIfAbsent(s.getName(), 0);

        while (nodesToVisit.size() > 0) {
            Node n = nodesToVisit.pollFirst();
            for (Node neighbor : n.getNeighbors()) {
                if (!parentNode.containsKey(neighbor) && !nodesToVisit.contains(neighbor)) {
                    nodesToVisit.add(neighbor);
                    parentNode.putIfAbsent(neighbor, n);
                    distanceFromS.putIfAbsent(neighbor.getName(), distanceFromS.get(n.getName()) + 1);
                    if (neighbor.equals(t)) {
                        nodesToVisit.clear();
                        break;
                    }
                }
            }
        }

        if (distanceFromS.get(t.getName()) != null) {
            parentNode.remove(s);
            distanceFromS.remove(s.getName());
            shortestPath.add(t);
            if (distanceFromS.containsKey(t.getName())) {
                int k = distanceFromS.get(t.getName());
                backTrackSearch(parentNode, distanceFromS, k, t, shortestPath);
            }
            shortestPath.add(s);
            Collections.reverse(shortestPath);
            return shortestPath;
        } else {
            return null;
        }
    }

    private void backTrackSearch(HashMap<Node, Node> parentNode, HashMap<String, Integer> distanceFromS, int k, Node t, ArrayList<Node> shortestPath) {
        for (int i = k - 1; i > 0; i--) {
            Node n = parentNode.get(t);
            if (distanceFromS.get(n.getName()) == i) {
                if (n.getNeighbors().contains(t)) {
                    shortestPath.add(n);
                    t = n;
                }
            }
        }
    }
}