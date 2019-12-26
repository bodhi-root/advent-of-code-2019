package day07;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public interface ComputerInput {

	int getInput();
	
	/**
	 * Input object that will prompt the user for input
	 */
	public static class UserInput implements ComputerInput {
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		public int getInput() {
			try {
				System.out.print("Input Number: ");
				String line = in.readLine();
				return Integer.parseInt(line);
			}
			catch(IOException e) {
				throw new IllegalStateException(e);
			}
		}
		
	}
	
	/**
	 * Input object that uses a pre-defined array of numbers.
	 */
	public static class Array implements ComputerInput {
		
		int [] input;
		int nextIndex = 0;
		boolean loop;
		
		public Array(int [] input, boolean loop) {
			this.input = input;
			this.loop = loop;
		}
		public Array(int [] input) {
			this(input, false);
		}
		
		public int getInput() {
			if (this.nextIndex >= input.length) {
				if (loop)
					nextIndex = 0;
				else
					throw new IllegalStateException("No more input in array");
			}
			
			return input[nextIndex++];
		}
		
	}
	
	/**
	 * Input that just provides zeroes
	 */
	public static ComputerInput ZEROES = new ComputerInput() {
		public int getInput() {
			return 0;
		}
	};
	
}
