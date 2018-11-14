import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IMDBGraphImpl implements IMDBGraph {
    private final HashMap<String, IMDBNode> movies = new HashMap<>();
    private final HashMap<String, IMDBNode> actors = new HashMap<>();

    private String _actorsFileName, _actressesFileName;

    IMDBGraphImpl (String actorsFilename, String actressesFilename) throws IOException {
        _actorsFileName = actorsFilename;
        _actressesFileName = actressesFilename;
        Scanner scanner = new Scanner(new FileInputStream(_actorsFileName), "ISO-8859-1");
        beginScanner(scanner);
        Scanner scanner2 = new Scanner(new FileInputStream(_actressesFileName), "ISO-8859-1");
        beginScanner(scanner2);
    }


    private void beginScanner(Scanner scanner) {
        while (scanner.hasNextLine()) {
            // do your stuff...
            final String line = scanner.nextLine();
            if (line.equals("THE ACTORS LIST") || line.equals("THE ACTRESSES LIST")) {
                for (int i = 0; i < 4; i++) {
                    scanner.nextLine();
                }
                break;
            }
        }
        makeMovies(scanner);
        scanner.close();
    }

    private void makeMovies(Scanner scanner)
    {
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            if(currentLine.isEmpty()) {
                // Do nothing
            }
            else if(currentLine.matches("[-]+")) {
                // Reached end of file
                break;
            }
            // If we aren't at eof, then carry on
            else {
                boolean isMovieActor = false;
                ArrayList<String> moviesToBeAdded = new ArrayList<>();
                // Get actor's name and preliminary movie name (will have to be edited later)
                String actorName = currentLine.substring(0, currentLine.indexOf("\t")).trim();
                String firstMovie = currentLine.substring(currentLine.indexOf("\t")).trim();
                if(isNotTV(firstMovie)) {
                    isMovieActor = true;
                    String cleanedMovie = cleanMovie(firstMovie);
                    moviesToBeAdded.add(cleanedMovie);
                }
                String nextMovie = scanner.nextLine();
                while (!nextMovie.isEmpty()) {
                    nextMovie = nextMovie.substring(nextMovie.indexOf("\t")).trim();
                    if (isNotTV(nextMovie)) {
                        isMovieActor = true;
                        String cleanedMovie = cleanMovie(nextMovie);
                        moviesToBeAdded.add(cleanedMovie);
                    }
                    nextMovie = scanner.nextLine();
                }

                // If they act in movies...
                if(isMovieActor) {
                    System.out.println(actorName);
                    IMDBNode actor = new IMDBNode(actorName);
                    actors.put(actorName, actor);
                    for(String movieName : moviesToBeAdded) {
                        checkMovieAndAdd(actor, movieName);
                    }
                }
            }
        }
    }

    private boolean isNotTV(String movieName)
    {
        return !movieName.substring(0, 1).contains("\"") && !movieName.contains(" (TV) ");
    }

    private void checkMovieAndAdd(IMDBNode actor, String movieName) {
        IMDBNode movieObject = movies.get(movieName);
        if(movieObject != null)
        {
            movieObject.addNode(actor);
            actor.addNode(movieObject);
        }
        else {
            // Need to make the movie, and add the actor to it
            IMDBNode movie = new IMDBNode(movieName);
            movie.addNode(actor);
            actor.addNode(movie);
            movies.put(movieName, movie);
            actors.put(actor.getName(), actor);
        }
    }

    private String cleanMovie(String movieName) {
        String cleanedMovie = movieName;
        Pattern p = Pattern.compile("([^\\t]+) [(][\\d+/?]{4}([)]|[/])([\\w]?)+[)]?");
        Matcher matcher = p.matcher(cleanedMovie);
        if (matcher.find()){
            cleanedMovie = matcher.group();
        }
        return cleanedMovie;
    }

    /**
     * Gets all the actor nodes in the graph.
     * @return a collection of all the actor and actress nodes in the graph.
     */
    public Collection<? extends Node> getActors () {
        return actors.values();
    }

    /**
     * Gets all the movie nodes in the graph.
     * @return a collection of all the movie nodes in the graph.
     */
    public Collection<? extends Node> getMovies () {
        return movies.values();
    }

    /**
     * Returns the movie node having the specified name.
     * @param name the name of the requested movie
     * @return the movie node associated with the specified name or null
     * if no such movie exists.
     */
    public Node getMovie (String name) {
        return movies.get(name);
    }

    /**
     * Returns the actor node having the specified name.
     * @param name the name of the requested actor
     * @return the actor node associated with the specified name or null
     * if no such actor exists.
     */
    public Node getActor (String name) {
        return actors.get(name);
    }
}