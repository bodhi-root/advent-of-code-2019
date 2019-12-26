package day09;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public interface ComputerInput {

	long getInput();
	
	/**
	 * Input object that will prompt the user for input
	 */
	public static class UserInput implements ComputerInput {
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		public long getInput() {
			try {
				System.out.print("Input Number: ");
				String line = in.readLine();
				return Long.parseLong(line);
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
		
		long [] input;
		int nextIndex = 0;
		boolean loop;
		
		public Array(long [] input, boolean loop) {
			this.input = input;
			this.loop = loop;
		}
		public Array(long [] input) {
			this(input, false);
		}
		
		public long getInput() {
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
		public long getInput() {
			return 0;
		}
	};
	
}
