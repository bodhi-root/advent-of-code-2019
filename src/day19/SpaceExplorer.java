package day19;

import java.util.Arrays;
import common.Location;

public class SpaceExplorer {
	
	int [][] space;
	int height;
	int width;
	
	Drone drone = new Drone();
	
	public SpaceExplorer(int height, int width) {
		this.space = new int[height][width];
		this.height = height;
		this.width = width;
		
		for (int i=0; i<height; i++)
			Arrays.fill(space[i], Drone.UNKNOWN);
		
		space[0][0] = Drone.PULL;
	}

	public int getUnknownSpaceCount() {
		return getSpaceCount(-1);
	}
	public int getSpaceCount(int target) {
		int count = 0;
		
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				if (space[i][j] == target)
					count++;
			}
		}
		
		return count;
	}
	
	public Location getNextUnknownLocation() {
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				if (space[i][j] < 0)
					return new Location(i, j);
			}
		}
		
		return null;
	}
	
	public void test(Location location) {	
		this.space[location.row][location.col] = drone.test(location); 
	}
	public void testAll() {
		Location nextLoc;
		while ((nextLoc = getNextUnknownLocation()) != null)
			test(nextLoc);
	}
	
	public void printSpace() {
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				char ch;
				if (space[i][j] == 0)
					ch = '.';
				else if (space[i][j] == 1)
					ch = '#';
				else
					ch = '?';
				
				System.out.print(ch);
			}
			System.out.println();
		}
	}
	
	
	
}
