package day18;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import common.Location;

public class Maze {

	public static final char WALL = '#';
	public static final char FLOOR = '.';
	
	char [][] map;
	int height;
	int width;
	
	public Maze(char [][] map, int height, int width) {
		this.map = map;
		this.height = height;
		this.width = width;
	}
	
	public Maze copy() {
		char [][] copy = new char[height][width];
		for (int i=0; i<height; i++)
			System.arraycopy(map[i], 0, copy[i], 0, width);
		
		return new Maze(copy, height, width);
	}
	
	public boolean isValidLocation(int row, int col) {
		return (row >= 0 && row < height) &&
				(col >= 0 && col < width);
	}
	public boolean isValidLocation(Location loc) {
		return isValidLocation(loc.row, loc.col);
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
	
	public Location find(char value) {
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				if (map[i][j] == value)
					return new Location(i, j);
			}
		}
		return null;
	}
	public List<Location> findAll(char value) {
		List<Location> locations = new ArrayList<>();
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				if (map[i][j] == value)
					locations.add(new Location(i, j));
			}
		}
		return locations;
	}
	
	public boolean isKey(char ch) {
		return Character.isLetter(ch) && Character.isLowerCase(ch);
	}
	public boolean isKey(Location loc) {
		return isKey(get(loc));
	}
	public boolean isDoor(char ch) {
		return Character.isLetter(ch) && Character.isUpperCase(ch);
	}
	public boolean isDoor(Location loc) {
		return isDoor(get(loc));
	}
	public Location findDoorForKey(char ch) {
		char doorValue = Character.toUpperCase(ch);
		return find(doorValue);
	}
	
	public String getAllKeyAndDoorSymbolsAsString() {
		StringBuilder s = new StringBuilder();
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				if (map[i][j] != WALL && map[i][j] != FLOOR && map[i][j] != '@')
					s.append(map[i][j]);
			}
		}
		return s.toString();
	}
	public String getAllKeySymbolsAsString() {
		StringBuilder s = new StringBuilder();
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				if (isKey(map[i][j]))
					s.append(map[i][j]);
			}
		}
		return s.toString();
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
		return maze;
	}
	
}

