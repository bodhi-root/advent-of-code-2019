package day06;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanetInfo {

	Map<String, Planet> planets = new HashMap<>();
	
	public void add(Planet planet) {
		planets.put(planet.id, planet);
	}
	public Planet getPlanet(String id, boolean create) {
		Planet planet = planets.get(id);
		if (planet == null && create) {
			planet = new Planet(id);
			add(planet);
		}
		
		return planet;
	}
	
	public int getTotalOrbitCount() {
		int count = 0;
		
		for (Planet planet : planets.values())
			count += planet.getTotalOrbitCount();
		
		return count;
	}
	
	/**
	 * Returns the distance between the two planets.  This is the
	 * number of lines connecting them if you diagram them as done
	 * on day 6.
	 */
	public int getDistanceBetween(String planet1, String planet2) {
		List<String> ancestors1 = getPlanet(planet1, false).getAncestorIds();
		List<String> ancestors2 = getPlanet(planet2, false).getAncestorIds();
		
		int bestDistance = Integer.MAX_VALUE;
		for (int i=0; i<ancestors1.size(); i++) {
			String ancestorId1 = ancestors1.get(i);
			int j = ancestors2.indexOf(ancestorId1);
			
			if (j >= 0) {
				int distance = i + j + 2;
				if (distance < bestDistance)
					bestDistance = distance;
			}
		}
		
		return bestDistance;
	}
	
}
