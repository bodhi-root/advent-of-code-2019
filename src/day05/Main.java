package day05;

import java.io.File;

public class Main {

	public static void main(String [] args) {
		//runTests();
		solve();
	}
	
	public static void solve() {
		Computer computer = new Computer();
		try {
			//program 1: output 0 if the input is 0, otherwise output 1
			//computer.loadProgramFromString("3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9");
			
			//program 2: same thing
			//computer.loadProgramFromString("3,3,1105,-1,9,1101,0,0,12,4,12,99,1");
			
			/*
			//program 3: print 999 if input less than 8, 1000 if equal to 8, and 10001 if greater than 8
			computer.loadProgramFromString(
					"3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31," +
                    "1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104," + 
                    "999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99");
                    */
			computer.loadProgramFromFile(new File("files/day05/input.txt"));
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		computer.runProgram();
	}

	
	public static void runTests() {
		//old tests
		runAndTest("1,0,0,0,99", "2,0,0,0,99");
		runAndTest("2,3,0,3,99", "2,3,0,6,99");
		runAndTest("2,4,4,5,99,0", "2,4,4,5,99,9801");
		runAndTest("1,1,1,4,99,5,6,0,99", "30,1,1,4,2,5,6,0,99");
		
		//part 1 tests (new):
		runAndTest("1002,4,3,4,33", "1002,4,3,4,99");
		
		//part 2 tests (new):
		//runAndTest("3,9,8,9,10,9,4,9,99,-1,8", "");
		
	}
	
	protected static void runAndTest(String input, String output) {
		Computer computer = new Computer();
		computer.loadProgramFromString(input);
		computer.runProgram();
		assertEquals(computer.writeProgramToString(), output);
	}
	
	protected static void assertEquals(String txt1, String txt2) {
		if (!txt1.equals(txt2))
			throw new IllegalStateException("Assertion failed.  '" + txt1 + "' != '" + txt2 + "'");
	}
	
	
}
