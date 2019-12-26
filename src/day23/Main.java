package day23;

import java.io.File;

import day09.ProgramIO;

public class Main {

	public static void main(String [] args) {
		//solvePartA();
		solvePartB();
	}
	
	public static void solvePartA() {
		long [] program;
		try {
			program = ProgramIO.loadProgramFromFile(new File("files/day23/input.txt"));
		}
		catch(Exception e) {
			e.printStackTrace();
			return;
		}
		
		PartANetwork network = new PartANetwork(program);
		network.runNetwork();
	}
	
	public static void solvePartB() {
		long [] program;
		try {
			program = ProgramIO.loadProgramFromFile(new File("files/day23/input.txt"));
		}
		catch(Exception e) {
			e.printStackTrace();
			return;
		}
		
		PartBNetwork network = new PartBNetwork(program);
		network.runNetwork();
	}
	
}
