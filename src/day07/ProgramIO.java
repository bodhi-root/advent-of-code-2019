package day07;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ProgramIO {

	public static int [] loadProgramFromString(String text) {
		String [] parts = text.split(",");
		int [] memory = new int[parts.length];
		for (int i=0; i<parts.length; i++)
			memory[i] = Integer.parseInt(parts[i]);
		
		return memory;
	}
	
	public static int [] loadProgramFromFile(File file) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(file));
		try {
			String line = in.readLine();
			return loadProgramFromString(line);
		}
		finally {
			in.close();
		}
	}
	

	public static String writeProgramToString(int [] memory) {
		StringBuilder s = new StringBuilder();
		s.append(memory[0]);
		for (int i=1; i<memory.length; i++)
			s.append(",").append(memory[i]);
		return s.toString();
	}
	

	
}
