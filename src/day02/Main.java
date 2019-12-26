package day02;

import java.io.File;
import java.util.Arrays;

public class Main {

	public static void main(String [] args) {
		//runTests();
		
		Computer computer = new Computer();
		try {
			computer.loadProgramFromFile(new File("files/day02/input.txt"));
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		//first task:
		/*
		computer.set(1, 12);
		computer.set(2, 2);
		
		computer.runProgram();
		
		System.out.println(computer.get(0));
		 */
		
		//second task:
		int [] originalInput = Arrays.copyOf(computer.memory, computer.memory.length);
				
		for (int noun = 0; noun < 100; noun++) {
			for (int verb = 0; verb < 100; verb++) {
				System.arraycopy(originalInput, 0, computer.memory, 0, originalInput.length);
				
				computer.set(1, noun);
				computer.set(2, verb);
				
				computer.runProgram();
				
				int output = computer.get(0);
				if (output == 19690720) {
					System.out.println("Noun = " + noun);
					System.out.println("Verb = " + verb);
					System.out.println("Answer = " + (100 * noun + verb));
					break;
				}
			}
		}
		
	}
	
	public static void runTests() {
		runAndTest("1,0,0,0,99", "2,0,0,0,99");
		runAndTest("2,3,0,3,99", "2,3,0,6,99");
		runAndTest("2,4,4,5,99,0", "2,4,4,5,99,9801");
		runAndTest("1,1,1,4,99,5,6,0,99", "30,1,1,4,2,5,6,0,99");
	}
	
	protected static void runAndTest(String input, String output) {
		Computer computer = new Computer();
		computer.loadProgramFromString(input);
		computer.runProgram();
		assertEquals(computer.writeProgramToString(), output);
	}
	
	protected static void assertEquals(String txt1, String txt2) {
		if (!txt1.equals(txt2))
			throw new IllegalStateException("Assertion failed.  '" + txt1 + "' != '" + txt2 + "'");
	}
	
	
}
