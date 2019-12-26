package day10;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpaceMap {

	char [][] data;
	int height;
	int width;
	
	public SpaceMap(char [][] data) {
		this.data = data;
		this.height = data.length;
		this.width = data[0].length;
	}
	
	public void print() {
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				System.out.print(data[i][j]);
			}
			System.out.println();
		}
	}
	
	public static SpaceMap loadFromFile(File file) throws IOException {
		List<String> lines = new ArrayList<>();
		int width = 0;
		
		BufferedReader in = new BufferedReader(new FileReader(file));
		try {
			String line;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.isEmpty())
					continue;
				
				lines.add(line);
				width = Math.max(width, line.length());
			}
		}
		finally {
			in.close();
		}
		
		int height = lines.size();
		char [][] data = new char[height][width];
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				data[i][j] = lines.get(i).charAt(j);
			}
		}
		
		return new SpaceMap(data);
	}
	
	public int getAsteroidCount() {
		int count = 0;
		
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				if (data[i][j] == '#')
					count++;
			}
		}
		
		return count;
	}
	
	public void findLocationWithMostVisibleAsteroids() {
		int bestRow = 0, bestCol = 0;
		int bestCount = 0;
		
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				if (data[i][j] == '#') {
					int count = getVisibleAsteroidCountFrom(i,j);
					if (count > bestCount) {
						bestCount = count;
						bestRow = i;
						bestCol = j;
					}
				}
			}
		}
		
		System.out.println("Best Location: (" + bestRow + ", " + bestCol + ")");
		System.out.println("Count = " + bestCount);
	}
	
	public int getVisibleAsteroidCountFrom(int row, int col) {
		int count = 0;
		
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				//skip origin location:
				if (i == row && j == col)
					continue;
				
				if (data[i][j] == '#' && isVisible(row, col, i, j))
					count++;
			}
		}
		
		return count;
	}
	
	/**
	 * Returns TRUE if the asteroid at (srcRow, srcCol) can see the asteroid at
	 * (dstRow, dstCol).  This calculates the distance between the two points
	 * (dstRow - srcRow, dstCol - srcCol) and then breaks this up into line
	 * segments that each have integer width and height.  For horizontal and
	 * vertical checks this is easy.  For others we find the greatest common
	 * divisor to find how many such line segments there are and then step through
	 * all these to see if anything is in our way.
	 */
	protected boolean isVisible(int srcRow, int srcCol, int dstRow, int dstCol) {
		return findAsteroidBetween(srcRow, srcCol, dstRow, dstCol) == null;
	}

	/**
	 * Searches for an asteroid between the two points.  If one is found, the Point 
	 * location of it will be returned.  If there are multiple, the point nearest
	 * to (srcRow, srcCol) will be returned.
	 */
	protected Point findAsteroidBetween(int srcRow, int srcCol, int dstRow, int dstCol) {
		
		int di = dstRow - srcRow;
		int dj = dstCol - srcCol;
		
		int gcd;
		if (di == 0) {
			gcd = Math.abs(dj);
		} else if (dj == 0) {
			gcd = Math.abs(di);
		} else {
			gcd = gcd(Math.abs(di), Math.abs(dj));
		}
		
		if (gcd == 1)
			return null;
		
		for (int i=1; i<gcd; i++) {
			int tmpRow = srcRow + (di / gcd * i);
			int tmpCol = srcCol + (dj / gcd * i);
			if (isAsteroid(tmpRow, tmpCol))
				return new Point(tmpRow, tmpCol);
		}
		
		return null;
	}
	
	/**
	 * Returns the greatest common divisor of two numbers
	 * source: https://beginnersbook.com/2018/09/java-program-to-find-gcd-of-two-numbers/
	 */
	protected int gcd(int num1, int num2) {
		int gcd = 1;
		
		for (int i = 1; i <= num1 && i <= num2; i++) {
			if (num1 % i == 0 && num2 % i == 0)
				gcd = i;
		}

		return gcd;
	}
	
	public boolean isAsteroid(int row, int col) {
		return data[row][col] == '#';
	}
	public void removeAsteroid(int row, int col) {
		data[row][col] = '.';
	}
	
	/**
	 * Simulates firing of the laser as described in part B.  The list of asteroids returned
	 * in the order they are returned is given.
	 * 
	 * NOTE: This will alter the state of SpaceMap.  As asteroids are destroyed they will be
	 * removed from the map.
	 */
	public List<Point> fireTheLaser(int srcRow, int srcCol) {
		
		List<Point> points = new ArrayList<>();
		Point laserPointedAt = new Point(srcRow-1, srcCol);
		
		//look for first target on vertical axis
		for (int row=srcRow-1; row>=0; row--) {
			if (isAsteroid(row, srcCol)) {
				Point point = new Point(row, srcCol);
				points.add(point);
				laserPointedAt = point;
				removeAsteroid(row, srcCol);
				break;
			}
		}
		
		//look for next point
		Point point;
		while ((point = findNextTarget(srcRow, srcCol, laserPointedAt)) != null) {
			points.add(point);
			removeAsteroid(point.row, point.col);
			laserPointedAt = point;
		}
		
		return points;
	}
	
	public static final double TWO_PI = Math.PI * 2;
	
	protected Point findNextTarget(int srcRow, int srcCol, Point laserPointedAt) {
		
		Point bestPoint = null;
		double bestRotation = 0;
		
		double atanPointedAt = Math.atan2(srcRow - laserPointedAt.row, laserPointedAt.col - srcCol);
		
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				if (i == srcRow && j == srcCol)
					continue;
				
				if (isAsteroid(i, j) && isVisible(srcRow, srcCol, i, j)) {
					
					double atanTest = Math.atan2(srcRow - i, j - srcCol);
					double rotation = atanPointedAt - atanTest;
					if (rotation <= 0)
						rotation += TWO_PI;
					if (rotation > TWO_PI)
						rotation -= TWO_PI;
					
					if (bestPoint == null || rotation < bestRotation) {
						bestPoint = new Point(i, j);
						bestRotation = rotation;
					}
				}
			}
		}
		
		return bestPoint;
	}
	
}
