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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Maze2D {

    private byte maze[][];
    private Point initialLoc;
    private Point goalLoc;
    private int rows;
    private int columns;
    
    public static enum Symbol {
		OCCUPIED('X'), 	// 0
		EMPTY(' '), 	// 1
		START('@'), 	// 2
		GOAL('O'),		// 3
		VISITED('.');
		public final char character;
		
		Symbol(char symbol){
			this.character = symbol;
		}
		
		public byte value(){
			return (byte)this.ordinal();
		}
		
		public static Symbol parse(char c){
			for(Symbol s : Symbol.values()){
				if (s.character == c){
					return s;
				}
			}
			throw new IllegalArgumentException("Unrecognized char " + c);
		}
	};
    
    public Maze2D(byte maze[][], Point initial, Point goal){
    	this.maze = maze;
    	this.rows = maze.length;
    	this.columns = maze[0].length;
    	this.initialLoc = initial;
    	this.goalLoc = goal;
    }

    public Maze2D(String[] maze2D) {
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
        		maze[row][column] = Symbol.parse(charPoint).value();
        		if (maze[row][column]==Symbol.GOAL.value()){
        			this.goalLoc = new Point(x,y);
        		} else if (maze[row][column]==Symbol.START.value()){
        			this.initialLoc = new Point(x,y);
        		}
        	}
        }
    }
    
    public boolean isFree(Point p){
    	return this.maze[p.y][p.x]>Symbol.OCCUPIED.value();
    }

    public static Maze2D random(int size, double spaceProb) {
        byte[][] maze = new byte[size][size];
        Random r = new Random(System.nanoTime());
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                maze[row][column]= (r.nextDouble() > (1.0d - spaceProb))?Symbol.EMPTY.value():Symbol.OCCUPIED.value();
            }
        }
        return new Maze2D(maze, new Point(0,0), new Point(size-1,size-1));
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
    
    public void updateLocation(Point p, Symbol symbol){
    	int row = p.y;
    	int column = p.x;
    	this.maze[row][column]=symbol.value();
    }
    
    public void updateRectangle(Point a, Point b, Symbol symbol){
    	int xfrom = (a.x < b.x)?a.x:b.x;
    	int xto = (a.x > b.x)?a.x:b.x;
    	int yfrom = (a.y < b.y)?a.y:b.y;
    	int yto = (a.y > b.y)?a.y:b.y;
    	for(int x = xfrom; x<=xto; x++){
    		for(int y=yfrom; y<=yto; y++){
    			updateLocation(new Point(x,y), symbol);
    		}
    	}	
    }
    
    public void putObstacle(Point p){
    	updateLocation(p, Symbol.OCCUPIED);
    }
    
    public void removeObstacle(Point p){
    	updateLocation(p, Symbol.EMPTY);
    }
    
    public void putObstacleRectangle(Point a, Point b){
    	updateRectangle(a, b, Symbol.OCCUPIED);
    }
    
    public void removeObstacleRectangle(Point a, Point b){
    	updateRectangle(a, b, Symbol.EMPTY);
    }
    
    public String getReplacedMazeString(List<Map<Point, Character>> replacements){
    	String[] stringMaze = toStringArray();
    	for(Map<Point, Character> replacement : replacements){
	    	for(Point p : replacement.keySet()){
	    		int row = p.y;
	    		int column = p.x;
	    		stringMaze[row] = replaceChar(stringMaze[row], column, replacement.get(p));
	    	}
    	}
    	String output = "";
    	for(String line : stringMaze){
    		output += String.format("%s%n", line);
    	}
    	return output;
    }
    
    public String getStringMazeFilled(Collection<Point> points, char symbol) {
    	Map<Point,Character> replacements = new HashMap<Point, Character>();
    	for(Point p : points){
    		replacements.put(p, symbol);
    	}
    	return getReplacedMazeString(Collections.singletonList(replacements));
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
    
    public boolean pointInBounds(Point loc){
    	return loc.x<this.columns && loc.y < this.rows;
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
				chars[row][column]= Symbol.values()[maze[row][column]].character;
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

	public Set<Point> diff(Maze2D to){
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
    
    public static Maze2D empty(int size){
    	byte[][] maze = new byte[size][size];
    	for(int i=0;i<size;i++){
    		Arrays.fill(maze[i], Symbol.EMPTY.value());	
    	}
    	return new Maze2D(maze, new Point(0, 0), new Point(size-1,size-1));
    }
}
