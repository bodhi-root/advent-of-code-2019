package day24;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class State {

	public static final char BUG = '#';
	public static final char EMPTY = '.';
	
	char [][] state;
	int height;
	int width;
	
	public State(int height, int width) {
		this.state = new char[height][width];
		this.height = height;
		this.width = width;
	}
	
	public void step() {
		int [][] counts = new int[height][width];
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				counts[i][j] = getAdjacentBugCount(i, j);
			}
		}
		
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				if (state[i][j] == BUG) {
					if (counts[i][j] != 1)
						state[i][j] = EMPTY;
				} else {
					if (counts[i][j] == 1 || counts[i][j] == 2)
						state[i][j] = BUG;
				}
			}
		}
	}
	
	public int getAdjacentBugCount(int i, int j) {
		int count = 0;
		count += addBugIfValid(i-1,j);
		count += addBugIfValid(i+1, j);
		count += addBugIfValid(i, j-1);
		count += addBugIfValid(i, j+1);
		
		return count;
	}
	
	public int addBugIfValid(int i, int j) {
		if (i >= 0 && i < height &&
			j >= 0 && j < width &&
			state[i][j] == BUG) {
				return 1;
		} else {
			return 0;
		}
	}
	
	public void print() {
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++)
				System.out.print(state[i][j]);
			System.out.println();
		}
	}
	
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				s.append(state[i][j]);
			}
		}
		return s.toString();
	}
	
	public void loadFromFile(File file) throws IOException {
		List<String> lines = new ArrayList<>();
		
		BufferedReader in = new BufferedReader(new FileReader(file));
		try {
			String line;
			while ((line = in.readLine()) != null) {
				if (line.trim().isEmpty())
					continue;
				
				lines.add(line);
			}
		}
		finally {
			in.close();
		}
		
		for (int i=0; i<lines.size(); i++) {
			for (int j=0; j<5; j++)
				this.state[i][j] = lines.get(i).charAt(j);
		}
	}
	
	public int getBiodiversityRating() {
		int count = 0;
		
		int placeValue = 1;
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				if (state[i][j] == BUG)
					count+= placeValue;
				placeValue *= 2;
			}
		}
		
		return count;
	}
	
}
