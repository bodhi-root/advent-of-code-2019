package day19;

import java.io.File;
import java.util.Arrays;

import common.Location;
import day09.Computer;
import day09.ComputerInput;
import day09.ComputerOutput;

/**
 * This program was a mistake. I thought the drone would run in a loop to
 * repeatedly provide input and output.  This was not the case.
 */
public class Drone_Mistake implements ComputerInput, ComputerOutput {
	
	int [][] space;
	int height;
	int width;
	
	Location currentLocation = new Location(0, 0);
	Location nextLocation = new Location(0, 1);
	
	public static final int WRITE_X = 0;
	public static final int WRITE_Y = 1;
	public static final int READ_STATE = 2;
	
	int state = WRITE_X;
	
	public Drone_Mistake(int height, int width) {
		this.space = new int[height][width];
		this.height = height;
		this.width = width;
		
		for (int i=0; i<height; i++)
			Arrays.fill(space[i], -1);
		space[0][0] = 1;
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
	
	@Override
	public long getInput() {
		System.out.println("Read");
		if (state == WRITE_X) {
			this.state = WRITE_Y;
			return nextLocation.col;
		} 
		else if (state == WRITE_Y) {
			this.state = READ_STATE;
			return nextLocation.row;
		}
		else {
			throw new IllegalStateException("State should be WRITE_X or WRITE_Y when getInput() is called");
		}
	}
	
	@Override
	public void write(long value) {
		System.out.println("Write");
		if (state == READ_STATE) {
			this.state = WRITE_X;
			this.currentLocation = nextLocation;	//we should be here now
			
			this.space[currentLocation.row][currentLocation.col] = (int)value;
			
			this.nextLocation = getNextUnknownLocation();
			if (nextLocation == null)
				System.out.println("Tractor beam spaces = " + getSpaceCount(1));
		}
		else {
			throw new IllegalStateException("State should be READ_STATE when write() is called");
		}
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
	
	public static void main(String [] args) {
		
		Computer computer = new Computer();
		try {
			computer.loadProgramFromFile(new File("files/day19/input.txt"));
		}
		catch(Exception e) {
			e.printStackTrace();
			return;
		}
		
		Drone_Mistake drone = new Drone_Mistake(50, 50);
		computer.setInput(drone);
		computer.setOutput(drone);
		computer.runProgram();
		drone.printSpace();
	}
	
}
