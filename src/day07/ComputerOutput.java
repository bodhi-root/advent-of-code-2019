package day07;

import java.util.ArrayList;
import java.util.List;

public interface ComputerOutput {

	void write(int value);
	
	public static class PrintAndSave implements ComputerOutput {
		
		List<Integer> values = new ArrayList<>();
		
		public void write(int value) {
			System.out.println("OUTPUT: " + value);
			values.add(new Integer(value));
		}
		
		public List<Integer> getValues() {
			return values;
		}
		public int getLastValue() {
			if (values.isEmpty())
				throw new IllegalStateException("No output to retrieve");
			
			return values.get(values.size() - 1).intValue();
		}
		public int getFirstValue() {
			if (values.isEmpty())
				throw new IllegalStateException("No output to retrieve");
			
			return values.get(0).intValue();
		}
		public int getValue(int index) {
			if (values.size() <= index)
				throw new IllegalStateException("No output to retrieve at index: " + index);
			
			return values.get(index).intValue();
		}
	}
	
}
