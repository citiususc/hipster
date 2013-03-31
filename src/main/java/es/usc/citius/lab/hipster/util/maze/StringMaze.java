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

package es.usc.citius.lab.hipster.util.maze;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class StringMaze {

    private boolean maze[][];
    private String strMaze[];
    private Point initialLoc;
    private Point goalLoc;

    public StringMaze(String[] maze2D) {
        this.strMaze = maze2D;
        // Initialize maze
        maze = new boolean[maze2D[0].length()][maze2D.length];
        // Define valid cells
        for (int y = 0; y < maze2D.length; y++) {
            for (int x = 0; x < maze2D[y].length(); x++) {
                char cell = maze2D[y].charAt(x);
                // Parse cell
                switch (cell) {
                    case ' ':
                        maze[x][y] = true;
                        break;
                    case '@':
                        maze[x][y] = true;
                        initialLoc = new Point(x, y);
                        break;
                    case 'O':
                        maze[x][y] = true;
                        goalLoc = new Point(x, y);
                        break;
                    default:
                        maze[x][y] = false;
                }

            }
        }
    }

    public static StringMaze random(int size, double spaceProb) {
        String[] maze = new String[size];
        Random r = new Random(System.nanoTime());
        // Create #size files of #size columns
        for (int j = 0; j < size; j++) {
            String row = "";
            for (int i = 0; i < size; i++) {
                row += (r.nextDouble() > (1.0d - spaceProb)) ? " " : "X";
            }
            maze[j] = row;
        }
        // Set source and dest
        maze[0] = replaceChar(maze[0], 0, '@');
        maze[size - 1] = replaceChar(maze[size - 1], size - 1, 'O');
        return new StringMaze(maze);
    }

    public List<Point> getMazePoints() {
        List<Point> points = new ArrayList<Point>();
        for (int y = 0; y < maze.length; y++) {
            for (int x = 0; x < maze[y].length; x++) {
                points.add(new Point(y, x));
            }
        }
        return points;
    }

    public String[] getMazePath(List<Point> path) {
        String[] copyMaze = strMaze.clone();
        for (Point p : path) {
            if (validLocation(p)) {
                //StringBuilder line = new StringBuilder(copyMaze[p.y]);
                //line.setCharAt(p.x, '.');
                //copyMaze[p.y] = line.toString();
                copyMaze[p.y] = replaceChar(copyMaze[p.y], p.x, '.');
            }

        }
        return copyMaze;
    }

    private static String replaceChar(String line, int position, char c) {
        StringBuilder l = new StringBuilder(line);
        l.setCharAt(position, c);
        return l.toString();
    }

    public boolean validLocation(Point loc) {
        try {
            return maze[loc.x][loc.y];
        } catch (ArrayIndexOutOfBoundsException ex) {
            return false;
        }
    }

    public Collection<Point> validLocationsFrom(Point loc) {
        Collection<Point> validMoves = new HashSet<Point>();
        // Check for all valid movements
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                try {
                    if (maze[loc.x + i][loc.y + j]) {
                        validMoves.add(new Point(loc.x + i, loc.y + j));
                    }
                } catch (ArrayIndexOutOfBoundsException ex) {
                    // Invalid move!
                }
            }
        }
        validMoves.remove(loc);

        return validMoves;
    }

    public boolean[][] getMaze() {
        return maze;
    }

    public Point getInitialLoc() {
        return initialLoc;
    }

    public Point getGoalLoc() {
        return goalLoc;
    }

    @Override
    public String toString() {
        String output = "";
        for (int i = 0; i < strMaze.length; i++) {
            output += strMaze[i] + "\r\n";
        }
        return output;
    }
    
    public String getMazeForPath(List<Point> points) {
        String s = "";
        for (String line : this.getMazePath(points)) {
            s = s.concat(line).concat("\n");
        }
        return s;
    }
}
