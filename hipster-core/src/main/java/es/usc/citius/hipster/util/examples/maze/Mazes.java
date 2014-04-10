package es.usc.citius.hipster.util.examples.maze;


/**
 * Defines a set of maze examples.
 *
 * @author Pablo Rodríguez Mier
 */
public final class Mazes {

    private Mazes(){

    }

    // Minimal path: 5.656854249492381
    /**
     * 6x9 small maze with a shortest path distance of 5.656854249492381
     */
    public static String[] testMaze1 = new String[]{
            "        ",
            "    X   ",
            "  @ X O ",
            "    X   ",
            "        ",
            "        "};

    // Minimal path: 81.69848480983497
    /**
     * 20x32 maze with a shortest path distance of 81.69848480983497
     */
    public static String[] testMaze2 = new String[]{
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

    // Minimal path: 15.55634918610405
    /**
     * 13x33 maze with a shortest path distance of 15.55634918610405
     */
    public static String[] testMaze3 = new String[]{
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

    // Minimal cost: 27.07106781186548
    /**
     * 13x33 maze with a shortest path distance of 27.07106781186548
     */
    public static String[] testMaze4 = new String[]{
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
    // Minimal cost: 34.14213562373095
    /**
     * 13x33 maze with a shortest path distance of 34.14213562373095
     */
    public static String[] testMaze5 = new String[]{
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

    /**
     * ASCII Maze examples with the shortest path distance.
     */
    public enum Example {
        MAZE1(new Maze2D(testMaze1),5.656854249492381),
        MAZE2(new Maze2D(testMaze2),81.69848480983497),
        MAZE3(new Maze2D(testMaze3),15.55634918610405),
        MAZE4(new Maze2D(testMaze4),27.07106781186548),
        MAZE5(new Maze2D(testMaze5),34.14213562373095);

        double minimalPathCost;
        Maze2D maze;


        Example(Maze2D maze, double minimalPathCost){
            this.maze = maze;
            this.minimalPathCost = minimalPathCost;
        }

        public double getMinimalPathCost() {
            return minimalPathCost;
        }

        public Maze2D getMaze() {
            return maze;
        }

    }
}
