package day05;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Computer {

	int [] memory;
	int pos = 0;
	
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
	
	public static final int POSITION_MODE = 0;
	public static final int IMMEDIATE_MODE = 1;
	
	/**
	 * Returns the value of the given parameter.  parameterMode
	 * determines whether the parameter should be treated as 
	 * a memory position or an actual value.
	 */
	public int toParameterValue(int value, int parameterMode) {
				
		if (parameterMode == POSITION_MODE)
			return memory[value];
		else if (parameterMode == IMMEDIATE_MODE)
			return value;
		else
			throw new IllegalArgumentException("Unknown parameter mode: " + parameterMode);
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
		
		this.pos = 0;
		boolean keepRunning = true;
		
		int cmd;
		while(keepRunning) {
			cmd = memory[pos];
			System.out.println("CMD: " + cmd);
			if (cmd == EXIT) {
				keepRunning = false;
			}
			else {
				//opcode is the last 2 digits
				int opcode = cmd % 100;
				
				int pmode1 = (cmd / 100) % 10;
				int pmode2 = (cmd / 1000) % 10;
				int pmode3 = (cmd / 10000) % 10;
				
				//System.out.println("CMD: " + cmd + " " + Arrays.toString(new int [] {pmode1, pmode2, pmode3}));
				
				runCommand(opcode, new int [] {pmode1, pmode2, pmode3});
			}
			
		}
	}
	
	public static final int EXIT = 99;
	
	public static final int ADD = 1;
	public static final int MULTIPLY = 2;
	
	public static final int INPUT_VALUE = 3;
	public static final int OUTPUT_VALUE = 4;
	
	public static final int JUMP_IF_TRUE = 5;
	public static final int JUMP_IF_FALSE = 6;
	public static final int TEST_LESS_THAN = 7;
	public static final int TEST_EQUALS = 8;
	
	protected void runCommand(int opcode, int [] pmode) {
		if (opcode == ADD) {
			int p1 = get(pos+1);
			int p2 = get(pos+2);
			int p3 = get(pos+3);
			//System.out.println(p1 + "," + p2 + "," + p3);
			set(p3, toParameterValue(p1, pmode[0]) +
					toParameterValue(p2, pmode[1]));
			pos += 4;
		}
		else if (opcode == MULTIPLY) {
			int p1 = get(pos+1);
			int p2 = get(pos+2);
			int p3 = get(pos+3);
			set(p3, toParameterValue(p1, pmode[0]) *
					toParameterValue(p2, pmode[1]));
			pos += 4;
		}
		else if (opcode == INPUT_VALUE) {
			int p1 = get(pos+1);
			int value = inputValue();
			set(p1, value);
			pos += 2;
		}
		else if (opcode == OUTPUT_VALUE) {
			int p1 = get(pos+1);
			outputValue(toParameterValue(p1, pmode[0]));
			pos += 2;
		}
		else if (opcode == JUMP_IF_TRUE) {
			int p1 = get(pos+1);
			int p2 = get(pos+2);
			if (toParameterValue(p1, pmode[0]) != 0) {
				this.pos = toParameterValue(p2, pmode[1]);
			} else {
				this.pos += 3;
			}
		}
		else if (opcode == JUMP_IF_FALSE) {
			int p1 = get(pos+1);
			int p2 = get(pos+2);
			if (toParameterValue(p1, pmode[0]) == 0) {
				this.pos = toParameterValue(p2, pmode[1]);
			} else {
				this.pos += 3;
			}
		}
		else if (opcode == TEST_LESS_THAN) {
			int p1 = get(pos+1);
			int p2 = get(pos+2);
			int p3 = get(pos+3);
			int value = (toParameterValue(p1, pmode[0]) < toParameterValue(p2, pmode[1])) ? 1 : 0;
			set(p3, value);
			pos += 4;
		}
		else if (opcode == TEST_EQUALS) {
			int p1 = get(pos+1);
			int p2 = get(pos+2);
			int p3 = get(pos+3);
			int value = (toParameterValue(p1, pmode[0]) == toParameterValue(p2, pmode[1])) ? 1 : 0;
			set(p3, value);
			pos += 4;
		}
		else {
			throw new IllegalStateException("Invalid op code: " + opcode);
		}
	}
	
	protected void outputValue(int value) {
		System.out.println("OUTPUT: " + value);
	}
	
	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	
	protected int inputValue() {
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
