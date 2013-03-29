package es.usc.citius.lab.hipster.algorithm;

import es.usc.citius.lab.hipster.testutils.JungEdge;
import edu.uci.ics.jung.graph.DirectedGraph;
import es.usc.citius.lab.hipster.testutils.AStarIteratorFromMazeCreator;
import es.usc.citius.lab.hipster.testutils.JungDirectedGraphFromMazeCreator;
import es.usc.citius.lab.hipster.testutils.MazeSearch;
import es.usc.citius.lab.hipster.util.maze.StringMaze;
import java.awt.Point;
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

    public AStarMazeTest() {
    }


    @Test
    public void AStar_Maze1() throws InterruptedException {
        StringMaze maze = new StringMaze(MazeSearch.getTestMaze1());
        execute(maze, true);
    }

    @Test
    public void Dijkstra_Maze1() throws InterruptedException {
        StringMaze maze = new StringMaze(MazeSearch.getTestMaze1());
        execute(maze, false);
    }

    @Test
    public void AStar_Maze2() throws InterruptedException {
        StringMaze maze = new StringMaze(MazeSearch.getTestMaze2());
        execute(maze, true);
    }

    @Test
    public void Dijkstra_Maze2() throws InterruptedException {
        StringMaze maze = new StringMaze(MazeSearch.getTestMaze2());
        execute(maze, false);
    }

    @Test
    public void AStar_Maze3() throws InterruptedException {
        StringMaze maze = new StringMaze(MazeSearch.getTestMaze3());
        execute(maze, true);
    }

    @Test
    public void Dijkstra_Maze3() throws InterruptedException {
        StringMaze maze = new StringMaze(MazeSearch.getTestMaze3());
        execute(maze, false);
    }

    @Test
    public void AStar_Maze4() throws InterruptedException {
        StringMaze maze = new StringMaze(MazeSearch.getTestMaze4());
        execute(maze, true);
    }

    @Test
    public void Dijkstra_Maze4() throws InterruptedException {
        StringMaze maze = new StringMaze(MazeSearch.getTestMaze4());
        execute(maze, false);
    }

    @Test
    public void AStar_Maze5() throws InterruptedException {
        StringMaze maze = new StringMaze(MazeSearch.getTestMaze5());
        execute(maze, true);
    }

    @Test
    public void Dijkstra_Maze5() throws InterruptedException {
        StringMaze maze = new StringMaze(MazeSearch.getTestMaze5());
        execute(maze, false);
    }

    private void execute(StringMaze maze, boolean heuristic) throws InterruptedException {
        AstarIterator<Point> it = AStarIteratorFromMazeCreator.create(maze, heuristic);
        DirectedGraph<Point, JungEdge> graph = JungDirectedGraphFromMazeCreator.create(maze);
        MazeSearch.Result resultJung = MazeSearch.executeJungSearch(graph, maze);
        MazeSearch.Result resultIterator = MazeSearch.executePrintIteratorSearch(it, maze);
        assertEquals(resultIterator.getCost(), resultJung.getCost(), 0.001);
    }
    
    
}