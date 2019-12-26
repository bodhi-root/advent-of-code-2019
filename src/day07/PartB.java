package day07;

import java.io.File;
import java.util.Arrays;


public class PartB {

	public static void main(String [] args) {
		//test1();
		solvePartB();
	}
	
	/**
	 * Best output: 14365052
     * Input: [7, 8, 6, 9, 5]
	 */
	public static void solvePartB() {
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
			int output = amplifier.runWithFeedback(input);
			
			if (output > bestOutput) {
				bestOutput = output;
				bestInput = input;
			}	
		}
		
		System.out.println("Best output: " + bestOutput);
		System.out.println("Input: " + Arrays.toString(bestInput));
	}
	
	public static void test1() {
		int [] program = ProgramIO.loadProgramFromString("3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5");
		Amplifier amplifier = new Amplifier(program);
		int output = amplifier.runWithFeedback(new int [] {9,8,7,6,5});
		System.out.println(output); //139629729
		
		findMax(program);
	}
	
	/**
	 * Iterator that iterates over every possible valid input.  This is a bit of a hack.  We loop
	 * through the numbers 56789 to 98765, convert each to a string and then to an array and then
	 * check if they are valid.  It works though.
	 */
	static class InputIterator {
		
		int inputNumber = 56789;
		
		public int [] next() {
			int [] input = null;
			while (inputNumber <= 98765) {
				input = toInputArray(inputNumber);
				inputNumber++;
				
				if (input != null)
					return input;
			}
			
			return null;
		}
			
		protected int [] toInputArray(int inputNumber) {
			String inputText = String.valueOf(inputNumber);
			if (inputText.length() < 5)
				return null;
			
			int [] input = new int[5];
			for (int i=0; i<5; i++)
				input[i] = Integer.parseInt(inputText.substring(i, i+1));
			
			if (isValidPhaseSetting(input))
				return input;
			else
				return null;
		}
		
		/**
		 * Determine if the given phase setting is valid.  A valid setting uses the numbers 5, 6, 7, 8, 9
		 * each exactly once.
		 */
		public static boolean isValidPhaseSetting(int [] input) {
			if (input.length != 5)
				return false;
			
			for (int value=5; value<=9; value++) {
				boolean hasNumber = false;
				for (int j=0; j<input.length; j++) {
					if (input[j] == value) {
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
	
	
}
