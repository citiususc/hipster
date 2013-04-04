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
import java.util.Set;

public class StringMaze {

    private byte maze[][];
    private Point initialLoc;
    private Point goalLoc;
    private int rows;
    private int columns;
    
    public static enum Symbols {
		OCCUPIED('X'), 	// 0
		EMPTY(' '), 	// 1
		START('@'), 	// 2
		GOAL('O');		// 3
		public final char symbol;
		
		Symbols(char symbol){
			this.symbol = symbol;
		}
		
		public byte value(){
			return (byte)this.ordinal();
		}
	};
    
    public StringMaze(byte maze[][], Point initial, Point goal){
    	this.maze = maze;
    	this.rows = maze.length;
    	this.columns = maze[0].length;
    	this.initialLoc = initial;
    	this.goalLoc = goal;
    }

    public StringMaze(String[] maze2D) {
        // Initialize maze
        this.rows = maze2D.length;     		// y axis (rows)
        this.columns = maze2D[0].length();  // x axis (columns)
        
        maze = new byte[rows][columns];
        // Define valid cells
        for(int row = 0; row < this.rows; row++){
        	for(int column = 0; column < this.columns; column++){
        		// Note that, point(x=2,y=1) is located in map[1][2]
        		int x = column;
        		int y = row;
        		char charPoint = maze2D[row].charAt(column);
        		// Parse
        		switch (charPoint){
        			case ' ':
        				maze[row][column]=Symbols.EMPTY.value();
        				break;
                    case '@':
                        maze[row][column] = Symbols.START.value();
                        initialLoc = new Point(x, y);
                        break;
                    case 'O':
                        maze[row][column] = Symbols.GOAL.value();
                        goalLoc = new Point(x, y);
                        break;
                    default:
                        maze[row][column] = Symbols.OCCUPIED.value();
        		}
        	}
        }
    }
    
    public boolean isFree(Point p){
    	return this.maze[p.y][p.x]>Symbols.OCCUPIED.value();
    }

    public static StringMaze random(int size, double spaceProb) {
        byte[][] maze = new byte[size][size];
        Random r = new Random(System.nanoTime());
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                maze[row][column]= (r.nextDouble() > (1.0d - spaceProb))?Symbols.EMPTY.value():Symbols.OCCUPIED.value();
            }
        }
        return new StringMaze(maze, new Point(0,0), new Point(size-1,size-1));
    }

    public List<Point> getMazePoints() {
        List<Point> points = new ArrayList<Point>();
        for (int row = 0; row < this.rows; row++) {
            for (int column = 0; column < this.columns; column++) {
                points.add(new Point(column, row));
            }
        }
        return points;
    }
    
    public void updateLocation(Point p, Symbols symbol){
    	int row = p.y;
    	int column = p.x;
    	this.maze[row][column]=symbol.value();
    }
    
    public void putObstacle(Point p){
    	updateLocation(p, Symbols.OCCUPIED);
    }
    
    public void removeObstacle(Point p){
    	updateLocation(p, Symbols.EMPTY);
    }
    
    public void putObstacle(Point x, Point y){
    	
    }
    
    public void removeObstacle(Point x, Point y){
    	
    }

    
    public String[] getMazePath(List<Point> path) {
        String[] copyMaze = toStringArray();
        for (Point p : path) {
        	int row = p.y;
        	int column = p.x;
            if (validLocation(p)) {
                copyMaze[row] = replaceChar(copyMaze[row], column, '.');
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
            return isFree(loc);
        } catch (ArrayIndexOutOfBoundsException ex) {
            return false;
        }
    }

    public Collection<Point> validLocationsFrom(Point loc) {
        Collection<Point> validMoves = new HashSet<Point>();
        // Check for all valid movements
        for (int row = -1; row <= 1; row++) {
            for (int column = -1; column <= 1; column++) {
                try {
                	if (isFree(new Point(loc.x+column, loc.y+row))){
                		validMoves.add(new Point(loc.x+column, loc.y+row));	
                	}
                } catch (ArrayIndexOutOfBoundsException ex) {
                    // Invalid move!
                }
            }
        }
        validMoves.remove(loc);

        return validMoves;
    }
    
    public char[][] toCharArray() {
		char[][] chars = new char[this.rows][this.columns];
		for (int row = 0; row < this.rows; row++) {
			for (int column = 0; column < this.columns; column++) {
				chars[row][column]= Symbols.values()[maze[row][column]].symbol;
			}
		}
		return chars;
	}

	public String[] toStringArray() {
		char[][] chars = toCharArray();
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

	public Set<Point> diff(StringMaze to){
		char[][] maze1 = this.toCharArray();
		char[][] maze2 = to.toCharArray();
		Set<Point> differentLocations = new HashSet<Point>();
		for (int row = 0; row < this.rows; row++) {
			for (int column = 0; column < this.columns; column++) {
				if (maze1[row][column]!=maze2[row][column]){
					differentLocations.add(new Point(column, row));
				}
			}
		}
		return differentLocations;
	}

    public byte[][] getMaze() {
        return maze;
    }

    public Point getInitialLoc() {
        return initialLoc;
    }

    public Point getGoalLoc() {
        return goalLoc;
    }
    
    public String getMazeForPath(List<Point> points) {
        String s = "";
        for (String line : this.getMazePath(points)) {
            s = s.concat(line).concat("\n");
        }
        return s;
    }
}
