/*
 * Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.usc.citius.hipster.util.examples.maze;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * <p>
 * This class defines a 2D ASCII Maze used to easily validate the search algorithms.
 * Example usage:</p>
 *
 * <pre>
 *     {@code public static String[] example = new String[]{
 *                  "XXSXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
 *                  "XX XXXXXXXXXXXXX     XXXXXXXXXXX",
 *                  "XX    XXXXXXXXXX XXX XX     XXXX",
 *                  "XXXXX  XXXXXX    XXX XX XXX XXXX",
 *                  "XXX XX XXXXXX XX XXX XX  XX XXXX",
 *                  "XXX     XXXXX XXXXXX XXXXXX XXXX",
 *                  "XXXXXXX       XXXXXX        XXXX",
 *                  "XXXXXXXXXX XXXXX XXXXXXXXXXXXXXX",
 *                  "XXXXXXXXXX XX    XXXXX      XXXX",
 *                  "XXXXXXXXXX    XXXXXXXX XXXX XXXX",
 *                  "XXXXXXXXXXX XXXXXXXXXX XXXX XXXX",
 *                  "XXXXXXXXXXX            XXXX XXXX",
 *                  "XXXXXXXXXXXXXXXXXXXXXXXX XX XXXX",
 *                  "XXXXXX              XXXX XX XXXX",
 *                  "XXXXXX XXXXXXXXXXXX XX      XXXX",
 *                  "XXXXXX XXG   XXXXXX XXXX XXXXXXX",
 *                  "XXXXXX XXXXX   XXX            XX",
 *                  "XXXXXX XXXXXXX XXXXXXXXXXX XXXXX",
 *                  "XXXXXX XXXXXXX XXXXXXXXXXXXXXXXX",
 *                  "XXXXXX            XXXXXXXXXXXXXX"};
 *
 *            Maze2D maze = new Maze2D(example);
 *     }
 * </pre>
 *
 * Symbol table used by Maze2D:
 * <ul>
 *     <li>"X": occupied tile</li>
 *     <li>" ": empty tile</li>
 *     <li>"S": initial state (starting point)</li>
 *     <li>"G": goal state</li>
 *     <li>".": visited tile</li>
 * </ul>
 *
 */
public class Maze2D {

    private char maze[][];
    private Point initialLoc;
    private Point goalLoc;
    private int rows;
    private int columns;
    private static Set<Character> FREE_TILES = Sets.newHashSet(Arrays.asList(' ', 'S', 'G', '.'));

    /**
     * Symbols allowed to create a maze
     */
    public static enum Symbol {
        OCCUPIED('X'),
        EMPTY(' '),
        START('S'),
        GOAL('G'),
        VISITED('.');
        public final char character;

        Symbol(char symbol) {
            this.character = symbol;
        }

        public char value() {
            return character;
        }

        public static Symbol parse(char c) {
            for (Symbol s : Symbol.values()) {
                if (s.character == c) {
                    return s;
                }
            }
            // If the symbol is not recognized, it is considered as an occupied tile
            return OCCUPIED;
        }
    }

    /**
     * Creates a new 2D ASCII Maze.
     *
     * @param maze    2D Byte array of that represents the maze using {@link Symbol}
     */
    public Maze2D(char maze[][]) {
        this.maze = maze;
        this.rows = maze.length;
        this.columns = findMaxRowLength(maze);
        this.initialLoc = charToPoint(Symbol.START.value());
        this.goalLoc = charToPoint(Symbol.GOAL.value());
    }

    private Point charToPoint(char c){
        for(int row = 0; row < this.rows; row++){
            for(int column = 0; row < this.columns; column++){
                if (maze[row][column] == c) return new Point(column, row);
            }
        }
        return null;
    }

    /**
     * Creates a new 2D ASCII Maze from a array of Strings.
     *
     * @param maze2D Array of strings representing the maze. Use symbols from {@code Symbol}
     */
    public Maze2D(String[] maze2D) throws IllegalFormatException {
        // Initialize maze
        this.rows = maze2D.length;            // y axis (rows)
        this.columns = findMaxRowLength(maze2D);  // x axis (columns)

        maze = new char[rows][columns];
        // Define valid cells
        for (int row = 0; row < this.rows; row++) {
            // if the current row has less than 'columns' characters, fill with empty tiles
            if (maze2D[row].length() < this.columns) {
                maze2D[row] = maze2D[row].concat(Strings.repeat(String.valueOf(Symbol.EMPTY.value()), this.columns - maze2D[row].length()));
            }
            for (int column = 0; column < this.columns; column++) {
                char charPoint = maze2D[row].charAt(column);
                // Parse character
                maze[row][column] = charPoint;
                // Note that point(x=2,y=1) is located in maze[1][2]
                if (maze[row][column] == Symbol.GOAL.value()) {
                    this.goalLoc = new Point(column, row);
                } else if (maze[row][column] == Symbol.START.value()) {
                    this.initialLoc = new Point(column, row);
                }
            }
        }

        if (this.getInitialLoc() == null){
            throw new IllegalArgumentException("No initial location. Use the symbol S");
        }

        if (this.getGoalLoc() == null){
            throw new IllegalArgumentException("No goal location. Use the symbol G");
        }
    }

    private int findMaxRowLength(String maze[]){
        int max = 0;
        for (String rowMaze : maze) {
            if (rowMaze.length() > max) max = rowMaze.length();
        }
        return max;
    }

    private int findMaxRowLength(char maze[][]){
        int max = 0;
        for (int row = 0; row < maze.length; row++) {
            if (maze[row].length > max) max = maze[row].length;
        }
        return max;
    }

    /**
     * Read a maze from a file in plain text.
     * @param file file with the plain ascii text.
     * @return a new Maze2D.
     * @throws IOException
     */
    public static Maze2D read(File file) throws IOException {
        ArrayList<String> array = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            array.add(line);
        }
        br.close();
        return new Maze2D((String[]) array.toArray());
    }

    /**
     * Check if the point {@code p} in the maze is empty or occupied.
     *
     * @param p Point to check
     * @return True if is free, false if is not empty.
     */
    public boolean isFree(Point p) {
        return FREE_TILES.contains(this.maze[p.y][p.x]);
    }

    /**
     * Return all tiles (i,j) of the maze
     *
     * @return List of points, from (0,0) to (m,n).
     */
    public List<Point> getMazePoints() {
        List<Point> points = new ArrayList<Point>();
        for (int row = 0; row < this.rows; row++) {
            for (int column = 0; column < this.columns; column++) {
                points.add(new Point(column, row));
            }
        }
        return points;
    }

    /**
     * Replace a tile in the maze
     *
     * @param p      Point of the tile to be replaced
     * @param symbol New symbol
     */
    public void updateLocation(Point p, Symbol symbol) {
        int row = p.y;
        int column = p.x;
        this.maze[row][column] = symbol.value();
    }

    /**
     * Replace all tiles inside the rectangle with the provided symbol.
     * @param a point a of the rectangle.
     * @param b point b of the rectangle.
     * @param symbol symbol to be inserted in each tile.
     */
    public void updateRectangle(Point a, Point b, Symbol symbol) {
        int xfrom = (a.x < b.x) ? a.x : b.x;
        int xto = (a.x > b.x) ? a.x : b.x;
        int yfrom = (a.y < b.y) ? a.y : b.y;
        int yto = (a.y > b.y) ? a.y : b.y;
        for (int x = xfrom; x <= xto; x++) {
            for (int y = yfrom; y <= yto; y++) {
                updateLocation(new Point(x, y), symbol);
            }
        }
    }

    /**
     * Puts a {@link Symbol#OCCUPIED} in the indicated point.
     * @param p point to put an obstacle in.
     */
    public void putObstacle(Point p) {
        updateLocation(p, Symbol.OCCUPIED);
    }

    /**
     * Puts a {@link Symbol#EMPTY} character in the indicated point.
     * @param p point to be free.
     */
    public void removeObstacle(Point p) {
        updateLocation(p, Symbol.EMPTY);
    }

    /**
     * Fill a rectangle defined by points a and b with occupied tiles.
     * @param a point a of the rectangle.
     * @param b point b of the rectangle.
     */
    public void putObstacleRectangle(Point a, Point b) {
        updateRectangle(a, b, Symbol.OCCUPIED);
    }

    /**
     * Fill a rectangle defined by points a and b with empty tiles.
     * @param a point a of the rectangle.
     * @param b point b of the rectangle.
     */
    public void removeObstacleRectangle(Point a, Point b) {
        updateRectangle(a, b, Symbol.EMPTY);
    }

    /**
     * Generates a string representation of this maze but replacing all the indicated
     * points with the characters provided.
     * @param replacements list with maps of point-character replacements.
     * @return String representation of the maze with the replacements.
     */
    public String getReplacedMazeString(List<Map<Point, Character>> replacements) {
        String[] stringMaze = toStringArray();
        for (Map<Point, Character> replacement : replacements) {
            for (Point p : replacement.keySet()) {
                int row = p.y;
                int column = p.x;
                char c = stringMaze[row].charAt(column);
                if (c != Symbol.START.value() && c != Symbol.GOAL.value()) {
                    stringMaze[row] = replaceChar(stringMaze[row], column, replacement.get(p));
                }
            }
        }
        String output = "";
        for (String line : stringMaze) {
            output += String.format("%s%n", line);
        }
        return output;
    }

    /**
     * Generates a string representation of this maze, with the indicated points replaced
     * with the symbol provided.
     * @param points points of the maze.
     * @param symbol symbol to be inserted in each point.
     * @return the string representation of the maze with the points changed.
     */
    public String getStringMazeFilled(Collection<Point> points, char symbol) {
        Map<Point, Character> replacements = new HashMap<Point, Character>();
        for (Point p : points) {
            replacements.put(p, symbol);
        }
        return getReplacedMazeString(Collections.singletonList(replacements));
    }

    private static String replaceChar(String line, int position, char c) {
        StringBuilder l = new StringBuilder(line);
        l.setCharAt(position, c);
        return l.toString();
    }

    /**
     * Calculates whether a location is empty or not.
     * @param loc point location to be tested.
     * @return true if point is filled with {@link Symbol#EMPTY}. False otherwise.
     */
    public boolean validLocation(Point loc) {
        try {
            return isFree(loc);
        } catch (ArrayIndexOutOfBoundsException ex) {
            return false;
        }
    }

    /**
     * Check if the provided point is in the maze bounds or outside.
     * @param loc point to be tested.
     * @return true if the point is in the maze.
     */
    public boolean pointInBounds(Point loc) {
        return loc.x < this.columns && loc.y < this.rows;
    }

    /**
     * Return all neighbor empty points from a specific location point.
     * @param loc source point
     * @return collection of empty neighbor points.
     */
    public Collection<Point> validLocationsFrom(Point loc) {
        Collection<Point> validMoves = new HashSet<Point>();
        // Check for all valid movements
        for (int row = -1; row <= 1; row++) {
            for (int column = -1; column <= 1; column++) {
                try {
                    if (isFree(new Point(loc.x + column, loc.y + row))) {
                        validMoves.add(new Point(loc.x + column, loc.y + row));
                    }
                } catch (ArrayIndexOutOfBoundsException ex) {
                    // Invalid move!
                }
            }
        }
        validMoves.remove(loc);

        return validMoves;
    }

    public char[][] getMazeCharArray() {
        return maze;
    }

    public String[] toStringArray() {
        char[][] chars = getMazeCharArray();
        String[] str = new String[chars.length];
        for (int i = 0; i < chars.length; i++) {
            str[i] = String.copyValueOf(chars[i]);
        }
        return str;
    }

    @Override
    public String toString() {
        String output = "";
        String[] stringArray = toStringArray();
        for (int i = 0; i < maze.length; i++) {
            output += String.format("%s%n", stringArray[i]);
        }
        return output;
    }

    /**
     * Returns a set of points that are different with respect this maze.
     * Both mazes must have same size.
     * @param to maze to be compared.
     * @return set of different points.
     */
    public Set<Point> diff(Maze2D to) {
        char[][] maze1 = this.getMazeCharArray();
        char[][] maze2 = to.getMazeCharArray();
        Set<Point> differentLocations = new HashSet<Point>();
        for (int row = 0; row < this.rows; row++) {
            for (int column = 0; column < this.columns; column++) {
                if (maze1[row][column] != maze2[row][column]) {
                    differentLocations.add(new Point(column, row));
                }
            }
        }
        return differentLocations;
    }

    /**
     * Get this maze as a byte array.
     * @return
     */
    public char[][] getMaze() {
        return maze;
    }

    /**
     * Get the initial point (the point with the symbol {@link Symbol#START}) in this maze.
     * @return
     */
    public Point getInitialLoc() {
        return initialLoc;
    }

    /**
     * Get the goal point (the point with the symbol {@link Symbol#GOAL}) in this maze.
     * @return
     */
    public Point getGoalLoc() {
        return goalLoc;
    }

    /**
     * Generate an empty squared maze of the indicated size.
     * @param size maze size.
     * @return empty maze.
     */
    public static Maze2D empty(int size) {
        char[][] maze = new char[size][size];
        for (int i = 0; i < size; i++) {
            Arrays.fill(maze[i], Symbol.EMPTY.value());
        }
        maze[0][0] = Symbol.START.value();
        maze[size][size] = Symbol.GOAL.value();
        return new Maze2D(maze);
    }
}
