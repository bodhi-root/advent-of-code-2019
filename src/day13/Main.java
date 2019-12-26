package day13;

import java.io.File;

import day09.Computer;

public class Main {

	public static void main(String [] args) {
		//solvePartA();
		solvePartB();
	}
	
	public static void solvePartA() {
		Computer computer = new Computer();
		try {
			computer.loadProgramFromFile(new File("files/day13/input.txt"));
		}
		catch(Exception e) {
			e.printStackTrace();
			return;
		}
		
		Game game = new Game(23, 44);
		computer.setOutput(game.getIO());
		computer.runProgram();
		
		game.print();
		System.out.println(game.getTileCount(2));
	}


	public static void solvePartB() {
		Computer computer = new Computer();
		try {
			computer.loadProgramFromFile(new File("files/day13/input.txt"));
		}
		catch(Exception e) {
			e.printStackTrace();
			return;
		}
		computer.set(0, 2);
		
		Game game = new Game(23, 44);
		Game.GameIO io = game.getIO();
		io.autoPlayer = true;
		computer.setOutput(io);
		computer.setInput(io);
		computer.runProgram();
		
		game.print();
		System.out.println(game.getTileCount(2));
	}

	
}
