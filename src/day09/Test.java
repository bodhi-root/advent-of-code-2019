package day09;

import java.io.File;

public class Test {

	public static void main(String [] args) {
		//test1();
		//test2();
		//test3();
		//partA();
		partB();
	}
	
	public static void partA() {
		Computer computer = new Computer(1024 * 1024);
		try {
			computer.loadProgramFromFile(new File("files/day09/input.txt"));
		}
		catch(Exception e) {
			e.printStackTrace();
			return;
		}
		computer.setInput(new ComputerInput.Array(new long [] {1}));
		computer.runProgram();
	}
	
	public static void partB() {
		Computer computer = new Computer(1024 * 1024);
		try {
			computer.loadProgramFromFile(new File("files/day09/input.txt"));
		}
		catch(Exception e) {
			e.printStackTrace();
			return;
		}
		computer.setInput(new ComputerInput.Array(new long [] {2}));
		computer.runProgram();
	}
	
	public static void test1() {
		Computer computer = new Computer(1024);
		computer.loadProgramFromString("109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99");
		computer.runProgram();
	}
	
	public static void test2() {
		Computer computer = new Computer(1024);
		computer.loadProgramFromString("1102,34915192,34915192,7,4,7,99,0");
		computer.runProgram();
	}
	
	public static void test3() {
		Computer computer = new Computer(1024);
		computer.loadProgramFromString("104,1125899906842624,99");
		computer.runProgram();
	}
	
}
