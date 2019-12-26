package day09;

import java.util.ArrayList;
import java.util.List;

public interface ComputerOutput {

	void write(long value);
	
	public static class PrintAndSave implements ComputerOutput {
		
		List<Long> values = new ArrayList<>();
		boolean print;
		
		public PrintAndSave(boolean print) {
			this.print = print;
		}
		public PrintAndSave() {
			this(true);
		}
		
		public void write(long value) {
			if (print)
				System.out.println("OUTPUT: " + value);
			values.add(new Long(value));
		}
		
		public List<Long> getValues() {
			return values;
		}
		public long getLastValue() {
			if (values.isEmpty())
				throw new IllegalStateException("No output to retrieve");
			
			return values.get(values.size() - 1).longValue();
		}
		public long getFirstValue() {
			if (values.isEmpty())
				throw new IllegalStateException("No output to retrieve");
			
			return values.get(0).longValue();
		}
		public long getValue(int index) {
			if (values.size() <= index)
				throw new IllegalStateException("No output to retrieve at index: " + index);
			
			return values.get(index).longValue();
		}
	}
	
}
