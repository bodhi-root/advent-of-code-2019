package day15;

import common.Location;

public class Main {

	public static void main(String [] args) {
		RepairDroid droid = new RepairDroid(50, 50, new Location(25, 25));
		droid.getComputer().runProgram();
		//change the input object in RepairDroid to switch between part A and part B
	}
	
}
