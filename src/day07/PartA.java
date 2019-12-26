package day07;

import java.io.File;
import java.util.Arrays;

public class PartA {
	
	public static void main(String [] args) {
		//test1();
		//test2();
		//test3();
		solvePartA();
	}
	
	
	/**
	 * Best output: 92663
     * Input: 31420
	 */
	public static void solvePartA() {
		try {
			int [] program = ProgramIO.loadProgramFromFile(new File("files/day07/input.txt"));
			findMax(program);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void findMax(int [] program) {
		
		int [] bestInput = null;
		int bestOutput = Integer.MIN_VALUE;
		
		InputIterator iInput = new InputIterator();
		int [] input;
		while ((input = iInput.next()) != null) {
			Amplifier amplifier = new Amplifier(program);
			int output = amplifier.run(input);
			
			if (output > bestOutput) {
				bestOutput = output;
				bestInput = input;
			}	
		}
		
		System.out.println("Best output: " + bestOutput);
		System.out.println("Input: " + Arrays.toString(bestInput));
	}
	
	/**
	 * Iterator that iterates over every possible valid input.  This is a bit of a hack.  We loop
	 * through the numbers 01234 to 43210, convert each to a string and then to an array and then
	 * check if they are valid.  It works though.
	 */
	static class InputIterator {
		
		int inputNumber = 01234;
		
		public int [] next() {
			int [] input = null;
			while (inputNumber <= 43210) {
				input = toInputArray(inputNumber);
				inputNumber++;
				
				if (input != null)
					return input;
			}
			
			return null;
		}
			
		protected int [] toInputArray(int inputNumber) {
			String inputText = String.valueOf(inputNumber);
			if (inputText.length() < 4)
				return null;
			
			if (inputText.length() == 4)
				inputText = "0" + inputText;
			
			int [] input = new int[5];
			for (int i=0; i<5; i++)
				input[i] = Integer.parseInt(inputText.substring(i, i+1));
			
			if (isValidPhaseSetting(input))
				return input;
			else
				return null;
		}
		
		/**
		 * Determine if the given phase setting is valid.  A valid setting uses the numbers 0, 1, 2, 3, 4
		 * each exactly once.
		 */
		public static boolean isValidPhaseSetting(int [] input) {
			if (input.length != 5)
				return false;
			
			for (int i=0; i<=4; i++) {
				boolean hasNumber = false;
				for (int j=0; j<input.length; j++) {
					if (input[j] == i) {
						hasNumber = true;
						break;
					}
				}
				if (!hasNumber)
					return false;
			}
			return true;
		}
		
	}
	
	public static void test1() {
		int [] program = ProgramIO.loadProgramFromString("3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0");
		Amplifier amplifier = new Amplifier(program);
		int output = amplifier.run(new int [] {4,3,2,1,0});
		System.out.println(output);  //43210
	}
	
	public static void test2() {
		int [] program = ProgramIO.loadProgramFromString("3,23,3,24,1002,24,10,24,1002,23,-1,23,101,5,23,23,1,24,23,23,4,23,99,0,0");
		Amplifier amplifier = new Amplifier(program);
		int output = amplifier.run(new int [] {0, 1, 2, 3, 4});
		System.out.println(output);	//54321
	}
	
	public static void test3() {
		int [] program = ProgramIO.loadProgramFromString("3,31,3,32,1002,32,10,32,1001,31,-2,31,1007,31,0,33,1002,33,7,33,1,33,31,31,1,32,31,31,4,31,99,0,0,0");
		Amplifier amplifier = new Amplifier(program);
		int output = amplifier.run(new int [] {1, 0, 4, 3, 2});
		System.out.println(output);	//65210
		
		findMax(program);
	}
	
}
