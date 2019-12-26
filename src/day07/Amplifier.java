package day07;

public class Amplifier {

	int [] program;
	
	public Amplifier(int [] program) {
		this.program = program;
	}
	
	/**
	 * Runs the amplifier with the given "phase setting".  This is an array of 5 values,
	 * each representing an input to one stage of the amplifier.  The output of the
	 * amplifier is returned.
	 */
	public int run(int [] phaseSetting) {
		int input = 0;
		for (int i=0; i<phaseSetting.length; i++) {
			Computer computer = new Computer();
			
			computer.loadProgram(program);
			computer.setInput(new ComputerInput.Array(new int [] {phaseSetting[i], input}));
			
			ComputerOutput.PrintAndSave output = new ComputerOutput.PrintAndSave();
			computer.setOutput(output);
			
			computer.runProgram();
			
			input = output.getFirstValue();//output.getLastValue(); 
		}
		
		return input;
	}
	
	/**
	 * Runs the amplifier "feedback" mode (part B).
	 */
	public int runWithFeedback(int [] phaseSetting) {
		Computer [] computers = new Computer[5];
		ComputerOutput.PrintAndSave [] output = new ComputerOutput.PrintAndSave[5];
		
		//init computers:
		for (int i=0; i<computers.length; i++) {
			computers[i] = new Computer();
			computers[i].loadProgram(this.program);
			
			output[i] = new ComputerOutput.PrintAndSave();
			computers[i].setOutput(output[i]);
		}
		
		//connect to each other (1 through 4 get phaseSetting[i] + previousOutput
		//first computer gets phaseSetting[0] + 0 + previousOutput
		for (int i=1; i<computers.length; i++)
			computers[i].setInput(new ConnectedInput(new int [] {phaseSetting[i]}, output[i-1]));
		computers[0].setInput(new ConnectedInput(new int [] {phaseSetting[0], 0}, output[4]));
		
		int computerIndex = 0;
		while (true) {
			int opcode = computers[computerIndex].runCommand();
			if (opcode == Computer.OUTPUT_VALUE || opcode == Computer.EXIT) {
				
				if (opcode == Computer.EXIT && computerIndex == 4)
					break;
				
				computerIndex++;
				if (computerIndex == 5)
					computerIndex = 0;
			}
			
		}
		
		return output[4].getLastValue();
	}
	
	static class ConnectedInput implements ComputerInput {
		
		int [] fixedInput;
		ComputerOutput.PrintAndSave previousOutput;
		int nextIndex = 0;
		
		public ConnectedInput(int [] fixedInput, ComputerOutput.PrintAndSave previousOutput) {
			this.fixedInput = fixedInput;
			this.previousOutput = previousOutput;
		}
		
		public int getInput() {
			if (nextIndex < fixedInput.length) {
				return fixedInput[nextIndex++];
			}
			else {
				int value = previousOutput.getValue(nextIndex-fixedInput.length);
				nextIndex++;
				return value;
			}
		}
		
	}
	
	
}
