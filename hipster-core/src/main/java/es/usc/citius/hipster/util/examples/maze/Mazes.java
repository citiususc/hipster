/**
 * Copyright (C) 2013-2018 Centro de Investigación en Tecnoloxías da Información (CITIUS) (http://citius.usc.es)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package es.usc.citius.hipster.util.examples.maze;


/**
 * Class containing a set of {@link es.usc.citius.hipster.util.examples.maze.Maze2D} to execute in the tests
 * of the search iterators.
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public final class Mazes {

    private Mazes(){

    }


    /**
     * 6x9 small maze with a shortest path distance of 5.656854249492381
     */
    public static String[] testMaze1 = new String[]{
            "        ",
            "    X   ",
            "  S X G ",
            "    X   ",
            "        ",
            "        "};


    /**
     * 20x32 maze with a shortest path distance of 81.69848480983497
     */
    public static String[] testMaze2 = new String[]{
            "XXSXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
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
            "XXXXXX XXG   XXXXXX XXXX XXXXXXX",
            "XXXXXX XXXXX   XXX            XX",
            "XXXXXX XXXXXXX XXXXXXXXXXX XXXXX",
            "XXXXXX XXXXXXX XXXXXXXXXXXXXXXXX",
            "XXXXXX            XXXXXXXXXXXXXX"};


    /**
     * 13x33 maze with a shortest path distance of 15.55634918610405
     */
    public static String[] testMaze3 = new String[]{
            "                      G          ",
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
            "           S                     ",
            "                                 "};


    /**
     * 13x33 maze with a shortest path distance of 27.07106781186548
     */
    public static String[] testMaze4 = new String[]{
            "                      G          ",
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
            "           S                     ",
            "                                 "};

    /**
     * 13x33 maze with a shortest path distance of 34.14213562373095
     */
    public static String[] testMaze5 = new String[]{
            "                  X   G          ",
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
            "           S                     ",
            "                                 "};


    public static String[] exampleMaze1 = new String[]{
            "|             _________                      |",
            "|             |       |                      |",
            "|             |   G                          |",
            "|             |_______|                      |",
            "|                                            |",
            "| ||_____________________||  ||___|| ||_     |",
            "| ||                                         |",
            "| //___________   ____________   _______     |",
            "|            //  //         //  //           |",
            "|           //  //                           |",
            "|          //  //                            |",
            "|         //  //                             |",
            "|                                            |",
            "|                                            |",
            "|                   S                        |",
            "|                                            |"
    };

    /**
     * ASCII Maze examples with the shortest path distance.
     */
    public enum TestMaze {
        MAZE1(new Maze2D(testMaze1),5.656854249492381),
        MAZE2(new Maze2D(testMaze2),81.69848480983497),
        MAZE3(new Maze2D(testMaze3),15.55634918610405),
        MAZE4(new Maze2D(testMaze4),27.07106781186548),
        MAZE5(new Maze2D(testMaze5),34.14213562373095);

        double minimalPathCost;
        Maze2D maze;


        TestMaze(Maze2D maze, double minimalPathCost){
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
