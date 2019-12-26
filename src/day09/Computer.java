package day09;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Computer {

	long [] memory;
	int pos = 0;
	int relativeBase = 0;
	
	ComputerInput input = ComputerInput.ZEROES;
	ComputerOutput output = new ComputerOutput.PrintAndSave();
	
	int lastOpCode = 0;
	boolean verbose = false;
	
	public Computer(int memorySize) {
		this.memory = new long[memorySize];
	}
	public Computer() {
		this(1024 * 1024);
	}
	
	public void setInput(ComputerInput input) {
		this.input = input;
	}
	
	public void setOutput(ComputerOutput output) {
		this.output = output;
	}
	public ComputerOutput getOutput() {
		return output;
	}
	
	public long get(int offset) {
		return memory[offset];
	}
	public void set(int offset, long value) {
		memory[offset] = value;
	}
	
	public static final int POSITION_MODE = 0;
	public static final int IMMEDIATE_MODE = 1;
	public static final int RELATIVE_MODE = 2;
	
	public void setWithMode(int offset, long value, int mode) {
		if (mode == POSITION_MODE)
			memory[offset] = value;
		else if (mode == IMMEDIATE_MODE)
			throw new IllegalArgumentException("Immediate mode cannot be used for setting values");
		else if (mode == RELATIVE_MODE)
			memory[(int)(offset + relativeBase)] = value;
		else
			throw new IllegalArgumentException("Unknown parameter mode: " + mode);
	}
	
	/**
	 * Returns the value of the given parameter.  parameterMode
	 * determines whether the parameter should be treated as 
	 * a memory position or an actual value.
	 */
	public long toParameterValue(long value, int parameterMode) {
				
		if (parameterMode == POSITION_MODE)
			return memory[(int)value];
		else if (parameterMode == IMMEDIATE_MODE)
			return value;
		else if (parameterMode == RELATIVE_MODE)
			return memory[(int)(value + relativeBase)];
		else
			throw new IllegalArgumentException("Unknown parameter mode: " + parameterMode);
	}
	
	public void loadProgram(long [] memory) {
		Arrays.fill(this.memory, 0);
		System.arraycopy(memory, 0, this.memory, 0, memory.length);
	}
	public void loadProgramFromString(String text) {
		loadProgram(ProgramIO.loadProgramFromString(text));
	}
	public void loadProgramFromFile(File file) throws IOException {
		loadProgram(ProgramIO.loadProgramFromFile(file));
	}
	
	public String writeProgramToString() {
		return ProgramIO.writeProgramToString(this.memory);
	}
	
	public void runProgram() {
		
		this.pos = 0;
		
		while (true) {
			this.lastOpCode = runCommand();
			if (this.lastOpCode == EXIT)
				return;
		}
		
	}
	
	/**
	 * Runs the next command.  The opcode that was run is returned.
	 */
	public int runCommand() {
		long cmd = memory[pos];
		if (verbose)
			System.out.println("CMD: " + cmd);
		
		if (cmd == EXIT)
			return EXIT;
		
		//opcode is the last 2 digits
		int opcode = (int)(cmd % 100);

		int pmode1 = (int)((cmd / 100) % 10);
		int pmode2 = (int)((cmd / 1000) % 10);
		int pmode3 = (int)((cmd / 10000) % 10);

		//System.out.println("CMD: " + cmd + " " + Arrays.toString(new int [] {pmode1, pmode2, pmode3}));

		runCommand(opcode, new int [] {pmode1, pmode2, pmode3});
		return opcode;
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
	
	public static final int ADJUST_RELATIVE_BASE = 9;
	
	protected void runCommand(int opcode, int [] pmode) {
		if (opcode == ADD) {
			long p1 = get(pos+1);
			long p2 = get(pos+2);
			long p3 = get(pos+3);
			//System.out.println(p1 + "," + p2 + "," + p3);
			setWithMode((int)p3, 
					    toParameterValue(p1, pmode[0]) + toParameterValue(p2, pmode[1]),
					    pmode[2]);
			pos += 4;
		}
		else if (opcode == MULTIPLY) {
			long p1 = get(pos+1);
			long p2 = get(pos+2);
			long p3 = get(pos+3);
			setWithMode((int)p3, 
					     toParameterValue(p1, pmode[0]) * toParameterValue(p2, pmode[1]),
					     pmode[2]);
			pos += 4;
		}
		else if (opcode == INPUT_VALUE) {
			long p1 = get(pos+1);
			long value = input.getInput();
			setWithMode((int)p1, 
					     value,
					     pmode[0]);
			pos += 2;
		}
		else if (opcode == OUTPUT_VALUE) {
			long p1 = get(pos+1);
			output.write(toParameterValue(p1, pmode[0]));
			pos += 2;
		}
		else if (opcode == JUMP_IF_TRUE) {
			long p1 = get(pos+1);
			long p2 = get(pos+2);
			if (toParameterValue(p1, pmode[0]) != 0) {
				this.pos = (int)toParameterValue(p2, pmode[1]);
			} else {
				this.pos += 3;
			}
		}
		else if (opcode == JUMP_IF_FALSE) {
			long p1 = get(pos+1);
			long p2 = get(pos+2);
			if (toParameterValue(p1, pmode[0]) == 0) {
				this.pos = (int)toParameterValue(p2, pmode[1]);
			} else {
				this.pos += 3;
			}
		}
		else if (opcode == TEST_LESS_THAN) {
			long p1 = get(pos+1);
			long p2 = get(pos+2);
			long p3 = get(pos+3);
			long value = (toParameterValue(p1, pmode[0]) < toParameterValue(p2, pmode[1])) ? 1 : 0;
			setWithMode((int)p3, 
					    value,
					    pmode[2]);
			pos += 4;
		}
		else if (opcode == TEST_EQUALS) {
			long p1 = get(pos+1);
			long p2 = get(pos+2);
			long p3 = get(pos+3);
			long value = (toParameterValue(p1, pmode[0]) == toParameterValue(p2, pmode[1])) ? 1 : 0;
			setWithMode((int)p3, 
					     value,
					     pmode[2]);
			pos += 4;
		}
		else if (opcode == ADJUST_RELATIVE_BASE) {
			long p1 = get(pos+1);
			this.relativeBase += toParameterValue(p1, pmode[0]);
			pos += 2;
		}
		else {
			throw new IllegalStateException("Invalid op code: " + opcode);
		}
	}
	
}
