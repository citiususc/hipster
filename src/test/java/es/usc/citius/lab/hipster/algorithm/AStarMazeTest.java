package es.usc.citius.lab.hipster.algorithm;

import es.usc.citius.lab.hipster.testutils.JungEdge;
import edu.uci.ics.jung.graph.DirectedGraph;
import es.usc.citius.lab.hipster.node.NumericNodeBuilder;
import es.usc.citius.lab.hipster.testutils.AStarIteratorFromMazeCreator;
import es.usc.citius.lab.hipster.testutils.JungDirectedGraphFromMazeCreator;
import es.usc.citius.lab.hipster.testutils.MazeSearch;
import es.usc.citius.lab.hipster.util.maze.StringMaze;
import java.awt.Point;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Executes tests over predefined maze strings, comparing the results between
 * Jung and AD* iterator.
 *
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @author Pablo Rodríguez Mier <pablo.rodriguez.mier@usc.es>
 * @since 26/03/2013
 * @version 1.0
 */
public class AStarMazeTest {

    private static String[] testMaze1;
    private static String[] testMaze2;
    private static String[] testMaze3;
    private static String[] testMaze4;
    private static String[] testMaze5;

    public AStarMazeTest() {
    }

    @BeforeClass
    public static void tearUp() {
        testMaze1 = new String[]{
            "        ",
            "    X   ",
            "  @ X O ",
            "    X   ",
            "        ",
            "        "};

        testMaze2 = new String[]{
            "XX@XXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
            "XX XXXXXXXXXXXXX     XXXXXXXXXXX",
            "XX    XXXXXXXXXX XXX XX     XXXX",
            "XXXXX  XXXXXX    XXX XX XXX XXXX",
            "XXX XX XXXXXX XX XXX XX  XX XXXX",
            "XXX     XXXXX XXXXXX XXXXXX XXXX",
            "XXXXXXX       XXXXXX        XXXX",
            "XXXXXXXXXX XXXXX XXXXXXXXXXXXXXX",
            "XXXXXXXXXX XX    XXXXX      XXXX",
            "XXXXXXXXXX    XXXXXXXX XXXX XXXX",
            "XXXXXXXXXXX XXXXXXXXXX XXXX XXXX",
            "XXXXXXXXXXX            XXXX XXXX",
            "XXXXXXXXXXXXXXXXXXXXXXXX XX XXXX",
            "XXXXXX              XXXX XX XXXX",
            "XXXXXX XXXXXXXXXXXX XX      XXXX",
            "XXXXXX XXO   XXXXXX XXXX XXXXXXX",
            "XXXXXX XXXXX   XXX            XX",
            "XXXXXX XXXXXXX XXXXXXXXXXX XXXXX",
            "XXXXXX XXXXXXX XXXXXXXXXXXXXXXXX",
            "XXXXXX            XXXXXXXXXXXXXX"};

        testMaze3 = new String[]{
            "                      O          ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "           @                     ",
            "                                 "};

        testMaze4 = new String[]{
            "                      O          ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "     XXXXXXXXXXXXXXXXXXXXX       ",
            "     XXXXXXXXXXXXXXXXXXXXX       ",
            "                       XXX       ",
            "                       XXX       ",
            "                       XXX       ",
            "                       XXX       ",
            "           @                     ",
            "                                 "};

        testMaze5 = new String[]{
            "                  X   O          ",
            "                  X              ",
            "                  XXXXXXXX       ",
            "       XXXXXXXXXX  XXXXX         ",
            "                X    XXXXXXXXXX  ",
            "     XXXXXX  XXXXXXX  XXXX       ",
            "     XXXXXX XXXXXXX  XXXXX       ",
            "                       XXX       ",
            "                       XXX       ",
            "                       XXX       ",
            "                       XXX       ",
            "           @                     ",
            "                                 "};
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void AStar_Maze1() throws InterruptedException {
        StringMaze maze = new StringMaze(testMaze1);
        execute(maze, true);
    }

    @Test
    public void Dijkstra_Maze1() throws InterruptedException {
        StringMaze maze = new StringMaze(testMaze1);
        execute(maze, false);
    }

    @Test
    public void AStar_Maze2() throws InterruptedException {
        StringMaze maze = new StringMaze(testMaze2);
        execute(maze, true);
    }

    @Test
    public void Dijkstra_Maze2() throws InterruptedException {
        StringMaze maze = new StringMaze(testMaze2);
        execute(maze, false);
    }

    @Test
    public void AStar_Maze3() throws InterruptedException {
        StringMaze maze = new StringMaze(testMaze3);
        execute(maze, true);
    }

    @Test
    public void Dijkstra_Maze3() throws InterruptedException {
        StringMaze maze = new StringMaze(testMaze3);
        execute(maze, false);
    }

    @Test
    public void AStar_Maze4() throws InterruptedException {
        StringMaze maze = new StringMaze(testMaze4);
        execute(maze, true);
    }

    @Test
    public void Dijkstra_Maze4() throws InterruptedException {
        StringMaze maze = new StringMaze(testMaze4);
        execute(maze, false);
    }

    @Test
    public void AStar_Maze5() throws InterruptedException {
        StringMaze maze = new StringMaze(testMaze5);
        execute(maze, true);
    }

    @Test
    public void Dijkstra_Maz51() throws InterruptedException {
        StringMaze maze = new StringMaze(testMaze5);
        execute(maze, false);
    }

    private void execute(StringMaze maze, boolean heuristic) throws InterruptedException {
        AstarIterator<Point> it = AStarIteratorFromMazeCreator.create(maze, heuristic);
        DirectedGraph<Point, JungEdge> graph = JungDirectedGraphFromMazeCreator.create(maze);
        List<Point> solutionIterator = MazeSearch.executePrintIteratorSearch(it, maze);
        List<Point> solutionJung = MazeSearch.executeJungSearch(graph, maze);
        assertEquals(solutionJung, solutionIterator);
    }
    
    
}