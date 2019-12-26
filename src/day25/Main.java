package day25;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import day09.Computer;
import day09.ComputerInput;
import day09.ComputerOutput;

/**
 * Part A solution (see attached map), plus these items at end:
 *  - festive hat
 *  - semiconductor
 *  - space heater
 *  - hypercube
 */
public class Main {

	public static void main(String [] args) {
		Computer computer = new Computer();
		try {
			computer.loadProgramFromFile(new File("files/day25/input.txt"));
		}
		catch(IOException e) {
			e.printStackTrace();
			return;
		}
		
		computer.setInput(new UserInput());
		computer.setOutput(new PrintOutput());
		
		computer.runProgram();
	}
	
	static class PrintOutput implements ComputerOutput {

		public void write(long value) {
			System.out.print((char)value);
		}
		
	}
	
	static class UserInput implements ComputerInput {

		BufferedReader in;
		
		String line = null;
		int nextIndex = 0;
		
		public UserInput() {
			this.in = new BufferedReader(new InputStreamReader(System.in));
		}
		
		public long getInput() {
			if (line == null || nextIndex >= line.length()) {
				try {
					line = in.readLine() + "\n";
				}
				catch(IOException e) {
					throw new RuntimeException(e);
				}
				this.nextIndex = 0;
			}
			return line.charAt(this.nextIndex++);
		}		
		
	}
	
}
