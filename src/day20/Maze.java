package day20;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import common.Location;

public class Maze {

	public static final char NOTHING = ' ';
	public static final char WALL = '#';
	public static final char FLOOR = '.';
	
	char [][] map;
	int height;
	int width;
	
	String [][] labels;
	
	public Maze(char [][] map, int height, int width) {
		this.map = map;
		this.height = height;
		this.width = width;
		
		this.labels = new String[height][width];
	}
	
	public char get(int row, int col) {
		return map[row][col];
	}
	public char get(Location loc) {
		return get(loc.row, loc.col);
	}
	public void set(int row, int col, char value) {
		map[row][col] = value;
	}
	public void set(Location loc, char value) {
		set(loc.row, loc.col, value);
	}
	
	public String getLabel(int row, int col) {
		return labels[row][col];
	}
	public String getLabel(Location loc) {
		return getLabel(loc.row, loc.col);
	}
	public void setLabel(int row, int col, String value) {
		this.labels[row][col] = value;
	}
	public void setLabel(Location loc, String value) {
		setLabel(loc.row, loc.col, value);
	}
	
	public List<String> getAllLabels() {
		List<String> labelList = new ArrayList<>();
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				if (labels[i][j] != null)
					labelList.add(labels[i][j]);
			}
		}
		return labelList;
	}
	public List<Location> getAllLabeledLocations() {
		List<Location> locations = new ArrayList<>();
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				if (labels[i][j] != null)
					locations.add(new Location(i, j));
			}
		}
		return locations;
	}
	
	public Location findLocationWithLabel(String label) {
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				if (labels[i][j] != null && labels[i][j].equals(label))
					return new Location(i, j);
			}
		}
		return null;
	}
	public List<Location> findAllLocationsWithLabel(String label) {
		List<Location> locations = new ArrayList<>();
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				if (labels[i][j] != null && labels[i][j].equals(label))
					locations.add(new Location(i, j));
			}
		}
		return locations;
	}
	
	public void print() {
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				System.out.print(map[i][j]);
			}
			System.out.println();
		}
	}
	
	public static Maze loadFromFile(File file) throws IOException {
		List<String> lines = new ArrayList<>();
		int width = 0;
		
		//load lines from file into List
		BufferedReader in = new BufferedReader(new FileReader(file));
		try {
			String line;
			while ((line = in.readLine()) != null) {
				if (line.trim().isEmpty())
					continue;
				
				lines.add(line);
				width = Math.max(width, line.length());
			}
		}
		finally {
			in.close();
		}
		
		//convert to char array for maze map
		int height = lines.size();
		char [][] map = new char[height][width];
		for (int i=0; i<map.length; i++) {
			Arrays.fill(map[i], ' ');
			char [] chars = lines.get(i).toCharArray();
			System.arraycopy(chars, 0, map[i], 0, chars.length);
		}
		
		Maze maze = new Maze(map, height, width);
		
		//add exterior labels
		for (int j=0; j<width; j++) {
			if (map[0][j] != ' ') {
				String label = new String(new char [] {map[0][j], map[1][j]});
				maze.setLabel(1, j, label);
			}
			if (map[height-1][j] != ' ') {
				String label = new String(new char [] {map[height-2][j], map[height-1][j]});
				maze.setLabel(height-2, j, label);
			}
		}
		for (int i=0; i<height; i++) {
			if (map[i][0] != ' ') {
				String label = new String(new char [] {map[i][0], map[i][1]});
				maze.setLabel(i, 1, label);
			}
			if (map[i][width-1] != ' ') {
				String label = new String(new char [] {map[i][width-2], map[i][width-1]});
				maze.setLabel(i, width-2, label);
			}
		}
		
		//add interior labels
		Location interiorUpperLeft = null;
		Location interiorLowerRight = null;
		
		for (int i=2; i<height-2; i++) {
			for (int j=2; j<width-2; j++) {
				if (map[i][j] == ' ') {
					//System.out.println("Empty space @ (" + i + ", " + j + ")");
					if (interiorUpperLeft == null) {
						interiorUpperLeft = new Location(i, j);
						interiorLowerRight = new Location(i, j);
					}
					if (i >= interiorLowerRight.row &&
						j >= interiorLowerRight.col) {
						interiorLowerRight = new Location(i, j);
					}
				}
			}
		}
		//System.out.println("Interior: " + interiorUpperLeft + ", " + interiorLowerRight);
		
		for (int j=interiorUpperLeft.col; j<=interiorLowerRight.col; j++) {
			int i = interiorUpperLeft.row;
			if (map[i][j] != ' ') {
				String label = new String(new char [] {map[i][j], map[i+1][j]});
				maze.setLabel(i, j, label);
				//System.out.println("Found label: " + label + " @ (" + i + ", " + j + ")");
			}
			i = interiorLowerRight.row;
			//System.out.println()
			if (map[i][j] != ' ') {
				String label = new String(new char [] {map[i-1][j], map[i][j]});
				maze.setLabel(i, j, label);
				//System.out.println("Found label: " + label + " @ (" + i + ", " + j + ")");
			}
		}
		for (int i=interiorUpperLeft.row; i<=interiorLowerRight.row; i++) {
			int j = interiorUpperLeft.col;
			if (map[i][j] != ' ') {
				String label = new String(new char [] {map[i][j], map[i][j+1]});
				maze.setLabel(i, j, label);
			}
			j = interiorLowerRight.col;
			if (map[i][j] != ' ') {
				String label = new String(new char [] {map[i][j-1], map[i][j]});
				maze.setLabel(i, j, label);
			}
		}
		
		//label START and END
		maze.labelAdjacentFloor("AA", "START");
		maze.labelAdjacentFloor("ZZ", "END");
		
		return maze;
	}
	
	public Location findAdjacentFloor(Location loc) {
		if (get(loc.row+1, loc.col) == FLOOR)
			return new Location(loc.row+1, loc.col);
		
		if (get(loc.row-1, loc.col) == FLOOR)
			return new Location(loc.row-1, loc.col);
		
		if (get(loc.row, loc.col+1) == FLOOR)
			return new Location(loc.row, loc.col+1);
		
		if (get(loc.row, loc.col-1) == FLOOR)
			return new Location(loc.row, loc.col-1);
		
		return null;
	}
	
	protected void labelAdjacentFloor(String label, String adjacentLabel) {
		Location loc = findLocationWithLabel(label);
		setLabel(findAdjacentFloor(loc), adjacentLabel);
	}
	
}
