package day11;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import day09.ComputerInput;
import day09.ComputerOutput;

import common.Location;

public class World {

	int [][] data;
	int height = 0;
	int width = 0;
	
	Robot robot;
	
	public World(int height, int width) {
		this.data = new int[height][width];
		this.height = height;
		this.width = width;
		
		for (int i=0; i<height; i++)
			Arrays.fill(data[i], 0);
	}
	
	public void setRobot(Robot robot) {
		this.robot = robot;
	}
	public void paint(int row, int col, int value) {
		this.data[row][col] = value;
	}
	
	public void print() {
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				char ch = '?';
				if (data[i][j] == 0)
					ch = '.';
				else if (data[i][j] == 1)
					ch = '#';
				
				System.out.print(ch);
			}
			System.out.println();
		}
	}
	
	public WorldIO getWorldIO() {
		return new WorldIO(this);
	}
	
	static class WorldIO implements ComputerOutput, ComputerInput {
		
		World world;
		boolean paintNext = true;
		
		Set<Location> paintLocations = new HashSet<>(1000);
		
		public WorldIO(World world) {
			this.world = world;
		}
		
		public Set<Location> getPaintLocations() {
			return paintLocations;
		}
		
		public void write(long value) {
			if (paintNext) {
				
				world.paint(world.robot.row, world.robot.col, (int)value); 
				paintLocations.add(new Location(world.robot.row, world.robot.col));
				
			} else {
				
				if (value == 0) {
					world.robot.turnLeft();
				} else if (value == 1) {
					world.robot.turnRight();
				} else {
					throw new IllegalArgumentException("Robot turn instructions should only be 0 (LEFT) or 1 (RIGHT)");
				}
				
				world.robot.advance();
			}
			
			paintNext = !paintNext;
		}

		/**
		 * Returns the color of the space the robot occupies
		 */
		public long getInput() {
			return world.data[world.robot.row][world.robot.col];
		}
		
	}
	
}
