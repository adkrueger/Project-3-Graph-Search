import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IMDBGraphImpl implements IMDBGraph {
    private final HashMap<String, IMDBNode> movies = new HashMap<>();
    private final HashMap<String, IMDBNode> actors = new HashMap<>();
    private Pattern p;


    IMDBGraphImpl(String actorsFilename, String actressesFilename) throws IOException {
        p = Pattern.compile("([^\\t]+) [(][\\d+/?]{4}([)]|[/])([\\w]?)+[)]?");
        Scanner scanner = new Scanner(new FileInputStream(actressesFilename), "ISO-8859-1");
        iterateScanner(scanner);
        scanner.close();
        scanner = new Scanner(new FileInputStream(actorsFilename), "ISO-8859-1");
        iterateScanner(scanner);
        scanner.close();
    }

    private void iterateScanner(Scanner scanner) {
        String currentActor = null;
        boolean readyToScan = false;
        ArrayList<String> moviesToBeAdded = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (!readyToScan) {
                if (line.equals("THE ACTORS LIST") || line.equals("THE ACTRESSES LIST")) {
                    for (int i = 0; i < 5; i++) {
                        scanner.nextLine();
                    }
                    readyToScan = true;
                }
            } else if (!line.isEmpty()) {
                if (line.matches("[-]+")) {
                    // Reached end of file
                    break;
                }
                // Get actor's name and preliminary movie name (will have to be edited later)
                else if (!line.substring(0, 1).contains("\t")) {
                    currentActor = line.substring(0, line.indexOf("\t")).trim();
                }
                String movie = getMovieName(line.substring(line.indexOf("\t")));
                if (movie != null ) {
                    if(moviesToBeAdded == null) {
                        moviesToBeAdded = new ArrayList<>();
                    }
                    moviesToBeAdded.add(movie);
                }
            } else {
                if (moviesToBeAdded != null && !moviesToBeAdded.isEmpty()) {
                    addMoviesToActor(currentActor, moviesToBeAdded);
                    moviesToBeAdded = null;
                }
            }
        }
        System.gc();
    }

    private String getMovieName(String movieName) {
        movieName = movieName.trim();
        if (movieName.charAt(0) != '"' && !movieName.contains(" (TV) ")) {
            return cleanMovie(movieName);
        } else {
            return null;
        }
    }

    private void addMoviesToActor(String actor, ArrayList<String> movieNames) {
        IMDBNode newActor = new IMDBNode(actor);
        actors.putIfAbsent(actor, newActor);
        for (String movie : movieNames) {
            if (movies.containsKey(movie)) {
                // Old node, already exists
                IMDBNode movieNode = movies.get(movie);
                movieNode.addNode(newActor);
                newActor.addNode(movieNode);
                movies.putIfAbsent(movie, movieNode);
            } else {
                IMDBNode movieNode = new IMDBNode(movie);
                movieNode.addNode(newActor);
                newActor.addNode(movieNode);
                movies.putIfAbsent(movie, movieNode);
            }
        }
    }

    private String cleanMovie(String movieName) {
        String cleanedMovie = movieName;
        Matcher matcher = p.matcher(cleanedMovie);
        if (matcher.find()) {
            cleanedMovie = matcher.group();
        }
        return cleanedMovie;
    }

    /**
     * Gets all the actor nodes in the graph.
     *
     * @return a collection of all the actor and actress nodes in the graph.
     */
    public Collection<? extends Node> getActors() {
        return actors.values();
    }

    /**
     * Gets all the movie nodes in the graph.
     *
     * @return a collection of all the movie nodes in the graph.
     */
    public Collection<? extends Node> getMovies() {
        return movies.values();
    }

    /**
     * Returns the movie node having the specified name.
     *
     * @param name the name of the requested movie
     * @return the movie node associated with the specified name or null
     * if no such movie exists.
     */
    public Node getMovie(String name) {
        return movies.get(name);
    }

    /**
     * Returns the actor node having the specified name.
     *
     * @param name the name of the requested actor
     * @return the actor node associated with the specified name or null
     * if no such actor exists.
     */
    public Node getActor(String name) {
        return actors.get(name);
    }
}