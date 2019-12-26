package day19;

import java.io.File;
import java.io.IOException;

import common.Location;
import day09.Computer;
import day09.ComputerInput;
import day09.ComputerOutput;
import day09.ProgramIO;

public class Drone {
	
	public static final int PULL = 1;
	public static final int NO_PULL = 0;
	public static final int UNKNOWN = -1;
	
	long [] program;

	public Drone() {
		
		try {
			program = ProgramIO.loadProgramFromFile(new File("files/day19/input.txt"));
		}
		catch(IOException e) {
			throw new RuntimeException(e);
		}
		
	}
		
	public int test(Location location) {
		ComputerInput.Array input = new ComputerInput.Array(new long [] {location.col, location.row});
		ComputerOutput.PrintAndSave output = new ComputerOutput.PrintAndSave(false);
		
		Computer computer = new Computer();
		computer.loadProgram(this.program);
		computer.setInput(input);
		computer.setOutput(output);
		computer.runProgram();
		
		return (int)output.getLastValue(); 
	}
	
}
