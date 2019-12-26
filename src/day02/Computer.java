package day02;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Computer {

	int [] memory;
	
	public Computer(int [] memory) {
		this.memory = memory;
	}
	public Computer() {
		this(new int [] {99});
	}
	
	public int get(int offset) {
		return memory[offset];
	}
	public void set(int offset, int value) {
		memory[offset] = value;
	}
	
	public void loadProgramFromString(String text) {
		String [] parts = text.split(",");
		this.memory = new int[parts.length];
		for (int i=0; i<parts.length; i++)
			memory[i] = Integer.parseInt(parts[i]);
	}
	public void loadProgramFromFile(File file) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(file));
		try {
			String line = in.readLine();
			loadProgramFromString(line);
		}
		finally {
			in.close();
		}
	}
	
	public String writeProgramToString() {
		StringBuilder s = new StringBuilder();
		s.append(memory[0]);
		for (int i=1; i<memory.length; i++)
			s.append(",").append(memory[i]);
		return s.toString();
	}
	
	public void runProgram() {
		
		int pos = 0;
		boolean keepRunning = true;
		
		int cmd;
		while(keepRunning) {
			cmd = memory[pos];
			if (cmd == 99) {
				keepRunning = false;
			}
			else if (cmd == 1) {
				int x = memory[memory[pos+1]];
				int y = memory[memory[pos+2]];
				memory[memory[pos+3]] = x + y;
				pos += 4;
			}
			else if (cmd == 2) {
				int x = memory[memory[pos+1]];
				int y = memory[memory[pos+2]];
				memory[memory[pos+3]] = x * y;
				pos += 4;
			}
			else {
				throw new IllegalStateException("Invalid op code: " + cmd);
			}
		}
	}
	
	public static void main(String [] args) {
		Computer computer = new Computer();
		try {
			computer.loadProgramFromFile(new File("files/day2/input.txt"));
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
	
	
}
