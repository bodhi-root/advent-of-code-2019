package day11;

import java.io.File;

import day09.Computer;

public class Main {

	public static void main(String [] args) {
		//test();
		//solvePartA();
		solvePartB();
	}
	
	public static void solvePartA() {
		World world = new World(100, 100);
		world.setRobot(new Robot(50, 50));
		
		Computer computer = new Computer(1024 * 1024);
		try {
			computer.loadProgramFromFile(new File("files/day11/input.txt"));
		}
		catch(Exception e) {
			e.printStackTrace();
			return;
		}
		
		World.WorldIO io = world.getWorldIO();
		computer.setInput(io);
		computer.setOutput(io);
		
		computer.runProgram();
		
		System.out.println(io.getPaintLocations().size());
	}
	
	public static void solvePartB() {
		World world = new World(100, 100);
		world.setRobot(new Robot(50, 50));
		world.paint(50, 50, 1);	//paint first space
		
		Computer computer = new Computer(1024 * 1024);
		try {
			computer.loadProgramFromFile(new File("files/day11/input.txt"));
		}
		catch(Exception e) {
			e.printStackTrace();
			return;
		}
		
		World.WorldIO io = world.getWorldIO();
		computer.setInput(io);
		computer.setOutput(io);
		
		computer.runProgram();
		
		//System.out.println(io.getPaintLocations().size());
		world.print();
	}
	
	public static void test() {
		World world = new World(5, 5);
		world.setRobot(new Robot(2, 2));
		world.print();
		System.out.println();
		
		World.WorldIO output = world.getWorldIO();
		long [] outputs = new long [] {
				1, 0,
				0, 0,
				1, 0,
				1, 0,
				0, 1, 
				1, 0, 
				1, 0
		};
		for (int i=0; i<outputs.length; i++)
			output.write(outputs[i]);
		world.print();
	}
	
}
