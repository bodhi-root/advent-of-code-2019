package day06;

import java.io.BufferedReader;
import java.io.FileReader;

public class Main {

	public static void main(String [] args) {
		try {
			PlanetInfo info = new PlanetInfo();

			BufferedReader in = new BufferedReader(new FileReader("files/day06/input.txt"));
			try {
				String line;
				while ((line = in.readLine()) != null) {
					//System.out.println(line);
					String [] parts = line.split("\\)");
					Planet planet1 = info.getPlanet(parts[0], true);
					Planet planet2 = info.getPlanet(parts[1], true);
					planet2.orbitsAround = planet1;
				}
			}
			finally {
				in.close();
			}
			
			System.out.println("Total Orbits: " + info.getTotalOrbitCount());
			System.out.println("Jumps: " + (info.getDistanceBetween("YOU", "SAN") - 2));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
