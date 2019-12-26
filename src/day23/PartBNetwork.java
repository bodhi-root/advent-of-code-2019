package day23;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import day09.Computer;
import day09.ComputerInput;
import day09.ComputerOutput;

public class PartBNetwork {

	Computer [] computers = new Computer[50];
	CachedInput [] computerInput = new CachedInput[50];
	Nat nat = new Nat();
	
	public PartBNetwork(long [] program) {
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
	
	/**
	 * Returns true if the network is idle.  This means that every computer's last
	 * input was -1 because no message was available.
	 */
	public boolean isIdle() {
		for (int i=0; i<computers.length; i++) {
			if (!computerInput[i].isIdle())
				return false;
		}
		return true;
	}
	
	Set<Long> yValues = new HashSet<>();
	
	public void runNetwork() {
		
		int idlePeriods = 0;
		
		while(true) {
			for (int i=0; i<computers.length; i++) {
				computers[i].runCommand();
			}
			
			if (isIdle()) {
				idlePeriods++;
			} else {
				idlePeriods = 0;
			}
			
			//this works with (idlePeriods == 1), but I wasn't sure of that at first
			if (idlePeriods == 1) {
				if (nat.isValid) { 
					System.out.println("NAT writing (" + nat.x + ", " + nat.y + ")");

					computerInput[0].write(nat.x);
					computerInput[0].write(nat.y);
					//nat.isValid = false;

					if (!yValues.add(nat.y)) {
						System.out.println("Duplicate y value written: " + nat.y);
						System.exit(0);
					}
				}
			}
		}
	}
	
	static class Nat {
		
		long x;
		long y;
		
		boolean isValid = false;
		
		public void write(long x, long y) {
			this.x = x;
			this.y = y;
			this.isValid = true;
		}
		
	}
	
	static class CachedInput implements ComputerInput {
		
		Queue<Long> input = new LinkedList<Long>();
		boolean lastInputEmpty = false;

		public boolean isIdle() {
			return lastInputEmpty && input.isEmpty();
		}
		
		@Override
		public long getInput() {
			if (input.isEmpty()) {
				lastInputEmpty = true;
				return -1;
			}
			else {
				lastInputEmpty = false;
				return input.poll();
			}
		}
		
		public void write(long value) {
			input.add(value);
		}
		
	}
	
	static class NetworkOutput implements ComputerOutput {
		
		PartBNetwork network;
		List<Long> output = new ArrayList<>();
		
		public NetworkOutput(PartBNetwork network) {
			this.network = network;
		}

		@Override
		public void write(long value) {
			output.add(value);
			if (output.size() == 3) {
				long destination = output.get(0);
				
				//write to NAT:
				if (destination == 255) {
					System.out.println("Value written to NAT: (" + output.get(1) + ", " + output.get(2) + ")");
					network.nat.write(output.get(1), output.get(2));
				}
				else {

					if (destination < 0 || destination > network.computers.length)
						throw new IllegalStateException("Invalid destination: " + destination);

					//write to computer:
					int index = (int)destination;
					network.computerInput[index].write(output.get(1));
					network.computerInput[index].write(output.get(2));
				}
				
				output.clear();
			}
		}
		
	}
	
}
