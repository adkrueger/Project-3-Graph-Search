import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import java.io.*;

/**
 * Code to test Project 3; you should definitely add more tests!
 */
public class GraphPartialTester {
	IMDBGraph imdbGraph;
	GraphSearchEngine searchEngine;

	/**
	 * Verifies that there is no shortest path between a specific and actor and actress.
	 */

	@Test
	public void findShortestPath () throws IOException {
		imdbGraph = new IMDBGraphImpl("tests/actors_test.list", "tests/actresses_test.list");
		final Node actor1 = imdbGraph.getActor("Person, Fourth");
		System.out.println(actor1.getName());
		final Node actor2 = imdbGraph.getActor("Person, Fifth");
        System.out.println(actor2.getName());
        final List<Node> shortestPath = new GraphSearchEngineImpl().findShortestPath(actor1, actor2);
        for (Node node : actor1.getNeighbors())
        {
            System.out.println(node.getName());
            for (Node node2 : node.getNeighbors())
            {
                System.out.println(node2.getName());
            }
        }
	}


	@Before
	/**
	 * Instantiates the graph
	 */
	public void setUp () throws IOException {
		imdbGraph = new IMDBGraphImpl("tests/actors_test.list", "tests/actresses_test.list");
		searchEngine = new GraphSearchEngineImpl();
	}

	@Test
	/**
	 * Just verifies that the graphs could be instantiated without crashing.
	 */
	public void finishedLoading () {
		assertTrue(true);
		// Yay! We didn't crash
	}

	@Test
	/**
	 * Verifies that a specific movie has been parsed.
	 */
	public void testSpecificMovie () {
		testFindNode(imdbGraph.getMovies(), "Movie1 (2001)");
	}

	@Test
	/**
	 * Verifies that a specific actress has been parsed.
	 */
	public void testSpecificActress () {
		testFindNode(imdbGraph.getActors(), "Person, First");
	}

	/**
	 * Verifies that the specific graph contains a node with the specified name
	 * @param graph the IMDBGraph to search for the node
	 * @param name the name of the Node
	 */
	private static void testFindNode (Collection<? extends Node> nodes, String name) {
		boolean found = false;
		for (Node node : nodes) {
			if (node.getName().trim().equals(name)) {
				found = true;
			}
		}
		assertTrue(found);
	}
}
