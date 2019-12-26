package day06;

import java.util.ArrayList;
import java.util.List;

public class Planet {

	String id;
	Planet orbitsAround = null;

	public Planet(String id) {
		this.id = id;
	}
	
	public int getDirectOrbitCount() {
		return orbitsAround == null ? 0 : 1;
	}
	public int getIndirectOrbitCount() {
		return orbitsAround == null ? 0 : orbitsAround.getTotalOrbitCount();
	}
	public int getTotalOrbitCount() {
		if (orbitsAround == null)
			return 0;
		else
			return 1 + orbitsAround.getTotalOrbitCount();
	}
	
	/**
	 * Returns a list of planet IDs for all of the planets this one
	 * orbits either directly or indirectly.
	 */
	public List<String> getAncestorIds() {
		List<String> ids = new ArrayList<>();
		Planet planet = this;
		while (planet.orbitsAround != null) {
			ids.add(planet.orbitsAround.id);
			planet = planet.orbitsAround;
		}
		return ids;
	}
	
}
