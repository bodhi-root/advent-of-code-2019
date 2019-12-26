package day21;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import day09.Computer;
import day09.ComputerInput;
import day09.ComputerOutput;
import day09.ProgramIO;

public class Main {

	public static void main(String [] args) {
		//test();
		testB();
	}
	
	/**
	 * I tried to enumerate all possible programs... This didn't work.  It just told me
	 * that there were no 4-line solutions.  Although there is a 5-line solution
	 * (or so I learned later).
	 * 
	 * 36 unique 1-line programs
Trying all 1 line combinations...
There are 36 permutations
Trying all 2 line combinations...
There are 1296 permutations
Trying all 3 line combinations...
There are 46656 permutations
Trying all 4 line combinations...
There are 1679616 permutations
	 */
	public static void solvePartAByTestingAllPrograms() {
		
		//load intcode program:
		long [] intcode;
		try {
			intcode = ProgramIO.loadProgramFromFile(new File("files/day21/input.txt"));
		}
		catch(IOException e) {
			e.printStackTrace();
			return;
		}
		
		//get all 1-line commands:
		List<String> allCommands = listAllCommands();
		System.out.println(allCommands.size() + " unique 1-line programs");
		
		List<String> programs = new ArrayList<>();
		List<String> oldPrograms = new ArrayList<>();
		
		for (int i=0; i<15; i++) {
			System.out.println("Trying all " + (i+1) + " line combinations...");
		
			//every possible combination:
			if (i == 0) {
				for (String command : allCommands)
					programs.add(command);
			} else {
				oldPrograms.addAll(programs);
				programs.clear();
				
				for (String command : allCommands) {
					for (String oldProgram : oldPrograms) {
						programs.add(oldProgram + "\n" + command);
					}
				}
				
				oldPrograms.clear();
			}
			System.out.println("There are " + programs.size() + " permutations");

			for (String program : programs) {

				Computer computer = new Computer();
				computer.loadProgram(intcode);

				computer.setInput(new AsciiInput(program + "\nWALK\n"));

				SaveOutput output = new SaveOutput();
				computer.setOutput(output);

				computer.runProgram();
				if (output.isSolution()) {
					System.out.println("SOLUTION FOUND!");
					System.out.println("Damage = " + output.getSolution());
				}
			}
		}
	}
	
	public static void test() {
		Computer computer = new Computer();
		try {
			computer.loadProgramFromFile(new File("files/day21/input.txt"));
		}
		catch(Exception e) {
			e.printStackTrace();
			return;
		}
		
		//jump if hole in front of us:
		//String SAMPLE_1 = "NOT A J";
		
		/*
		String SAMPLE_2 = String.join("\n",
				"NOT A J",
				"NOT B T",
				"AND T J",
				"NOT C T",
				"AND T J",
				"AND D J"
				);
				*/
		
		//this should work...
		String MINE_1 = String.join("\n",
				//set J to True if A B C or is a hole:
				"NOT A J",
				"NOT B T",
				"OR T J",	
				"NOT C T",
				"OR T J",
				//and jump if we can land on D
				"AND D J"
				);
		System.out.println(MINE_1);
		
		//1 instruction shorter
		String MINE_2 = String.join("\n",
				//set T to True if A B C are all solid
				"OR A T",
				"AND B T",
				"AND C T ",	
				//negate T.  Now it is True if A B or C are holes:
				"NOT T J",
				//and jump if we can land on D
				"AND D J"
				);
		
		String program = MINE_2 + "\nWALK\n";
		
		computer.setInput(new AsciiInput(program));
		SaveOutput output = new SaveOutput();
		computer.setOutput(output);
		
		computer.runProgram();
		
		if (output.isSolution()) {
			System.out.println("SUCCESS!");
			System.out.println("Damage = " + output.getSolution());
		} else {
			output.printResult();
		}
	}


	public static void testB() {
		Computer computer = new Computer();
		try {
			computer.loadProgramFromFile(new File("files/day21/input.txt"));
		}
		catch(Exception e) {
			e.printStackTrace();
			return;
		}
		
		//1 instruction shorter
		String MINE_1 = String.join("\n",
				//set T to True if A B C are all solid
				"OR A T",
				"AND B T",
				"AND C T ",	
				//negate T.  Now it is True if A B or C are holes:
				"NOT T J",
				//and jump if we can land on D
				"AND D J",	//note: If we are jumping then J=True and T=False
				
				//see if we are safe after jumping
				"OR E T",	//if ground after D
				"OR H T",   //or landing spot if we jump
				"AND T J"
				);
		
		//!(A && B && C) && D
		
		String program = MINE_1 + "\nRUN\n";
		
		computer.setInput(new AsciiInput(program));
		SaveOutput output = new SaveOutput();
		computer.setOutput(output);
		
		computer.runProgram();
		output.printResult();
		
		if (output.isSolution()) {
			System.out.println("SUCCESS!");
			System.out.println("Damage = " + output.getSolution());
		} 
	}

	
	static class AsciiInput implements ComputerInput {

		String text;
		int nextIndex;
		
		public AsciiInput(String text) {
			this.text = text;
			this.nextIndex = 0;
		}
		
		@Override
		public long getInput() {
			if (nextIndex >= text.length())
				throw new IllegalStateException("No more input available!");
			else
				return text.charAt(nextIndex++);
		}
		
	}
	
	static class SaveOutput implements ComputerOutput {

		List<Long> list = new ArrayList<>();
		
		@Override
		public void write(long value) {
			list.add(value);
		}
		
		public boolean isSolution() {
			for (Long value : list) {
				if (value.longValue() > 256)
					return true;
			}
			return false;
		}
		public long getSolution() {
			for (Long value : list) {
				if (value.longValue() > 256)
					return value;
			}
			return -1;
		}
		
		public void printResult() {
			if (list.size() == 1)
				System.out.println("SOLUTION: " + list.get(0));
			else {
				for (int i=0; i<list.size(); i++) {
					long value = list.get(i);
					if (value > 256)
						System.out.println(value);
					else
						System.out.print((char)value);
				}
			}
		}
		
	}
	
	static List<String> listAllCommands() {
		List<String> commands = new ArrayList<>();
		
		String [] opArray = new String [] {"AND", "OR", "NOT"};
		String [] allRegisterArray = new String [] {"A", "B", "C", "D", "T", "J"};
		String [] writeableRegisterArray = new String [] {"T", "J"};
		
		for (int iOp=0; iOp<opArray.length; iOp++) {
			for (int iParam1=0; iParam1<allRegisterArray.length; iParam1++) {
				for (int iParam2=0; iParam2<writeableRegisterArray.length; iParam2++) {
					commands.add(opArray[iOp] + " " + allRegisterArray[iParam1] + " " + writeableRegisterArray[iParam2]);
				}
			}
		}
		
		return commands;
	}
	
}
