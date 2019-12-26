package day09;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ProgramIO {

	public static long [] loadProgramFromString(String text) {
		String [] parts = text.split(",");
		long [] memory = new long[parts.length];
		for (int i=0; i<parts.length; i++)
			memory[i] = Long.parseLong(parts[i]);
		
		return memory;
	}
	
	public static long [] loadProgramFromFile(File file) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(file));
		try {
			String line = in.readLine();
			return loadProgramFromString(line);
		}
		finally {
			in.close();
		}
	}
	

	public static String writeProgramToString(long [] memory) {
		StringBuilder s = new StringBuilder();
		s.append(memory[0]);
		for (int i=1; i<memory.length; i++)
			s.append(",").append(memory[i]);
		return s.toString();
	}
	
}
