import java.util.*;

public class GraphSearchEngineImpl implements GraphSearchEngine {

    /**
     * Finds a shortest path, if one exists, between nodes s and t. The path will be a list: (s, ..., t). If no
     * path exists, then this method will return null.
     * @param s the start node.
     * @param t the target node.
     * @return a shortest path in the form of a List of Node objects or null if no path exists.
     */
    public List<Node> findShortestPath(Node s, Node t) {
        ArrayList<Node> visitedNodes = new ArrayList<>();
        LinkedList<Node> nodesToVisit = new LinkedList<>();

        HashMap<Node, Integer> distanceFromS = new HashMap<>();
        ArrayList<Node> shortestPath = new ArrayList<>();

        nodesToVisit.add(s);
        distanceFromS.put(s, 0);

        while (nodesToVisit.size() > 0) {
            Node n = nodesToVisit.pollFirst();
            visitedNodes.add(n);
            if(n.equals(t)) {
                break;
            }
            for (Node neighbor : n.getNeighbors()) {
                if (!visitedNodes.contains(neighbor) && !nodesToVisit.contains(neighbor)) {
                    nodesToVisit.add(neighbor);
                    distanceFromS.put(neighbor, distanceFromS.get(n) + 1);
                    if(neighbor.equals(t))
                    {
                        nodesToVisit.clear();
                        break;
                    }
                }
            }
        }

        if(distanceFromS.get(t) != null) {
            visitedNodes.remove(s);
            int k = distanceFromS.get(t);
            shortestPath.add(t);
            backTrackSearch(visitedNodes, k - 1, t, distanceFromS, shortestPath);
            shortestPath.add(s);
            Collections.reverse(shortestPath);
            return shortestPath;
        }
        else {
            return null;
        }
    }

    private void backTrackSearch(ArrayList<Node> visited, int k, Node t, HashMap<Node, Integer> distanceFromS, ArrayList<Node> shortestPath)
    {
        while(k > 0)
        {
            System.out.println("-------------");
            for (Node n : visited)
            {
                if(distanceFromS.get(n) == k) {
                    if(n.getNeighbors().contains(t)) {
                        shortestPath.add(n);
                        t = n;
                        k--;
                        break;
                    }
                }
            }
        }
    }
}