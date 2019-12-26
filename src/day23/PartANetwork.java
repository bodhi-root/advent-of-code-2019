package day23;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import day09.Computer;
import day09.ComputerInput;
import day09.ComputerOutput;

public class PartANetwork {

	Computer [] computers = new Computer[50];
	CachedInput [] computerInput = new CachedInput[50];
	
	public PartANetwork(long [] program) {
		for (int i=0; i<50; i++) {
			computers[i] = new Computer();
			computers[i].loadProgram(program);
			
			CachedInput input = new CachedInput();
			input.write(i);
			computerInput[i] = input;
			computers[i].setInput(input);
			
			computers[i].setOutput(new NetworkOutput(this));
		}
	}
	
	public void runNetwork() {
		while(true) {
			for (int i=0; i<computers.length; i++) {
				computers[i].runCommand();
			}
		}
	}
	
	static class CachedInput implements ComputerInput {
		
		Queue<Long> input = new LinkedList<Long>();

		@Override
		public long getInput() {
			if (input.isEmpty())
				return -1;
			else
				return input.poll();
		}
		
		public void write(long value) {
			input.add(value);
		}
		
	}
	
	static class NetworkOutput implements ComputerOutput {
		
		PartANetwork network;
		List<Long> output = new ArrayList<>();
		
		public NetworkOutput(PartANetwork network) {
			this.network = network;
		}

		@Override
		public void write(long value) {
			output.add(value);
			if (output.size() == 3) {
				long destination = output.get(0);
				if (destination == 255) {
					System.out.println("Solution found: " + output.get(2));
					for (Long x : output)
						System.out.println(x);
					System.exit(0);
				}
				
				if (destination < 0 || destination > network.computers.length)
					throw new IllegalStateException("Invalid destination: " + destination);
				
				int index = (int)destination;
				network.computerInput[index].write(output.get(1));
				network.computerInput[index].write(output.get(2));
				
				output.clear();
			}
		}
		
	}
	
}
