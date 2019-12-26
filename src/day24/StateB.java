package day24;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StateB {

	public static final char BUG = '#';
	public static final char EMPTY = '.';
	
	char [][] state;
	int height;
	int width;
	
	int [][] counts;
	
	StateB inner = null;
	StateB outer = null;
	
	public StateB(int height, int width) {
		this.state = new char[height][width];
		this.height = height;
		this.width = width;
		
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				state[i][j] = EMPTY;
			}
		}
		
		this.counts = new int[5][5];
	}
	
	public StateB getLayer(int layerIndex) {
		if (layerIndex == 0)
			return this;
		
		if (layerIndex > 0) {
			StateB layer = this;
			for (int i=0; i<layerIndex; i++) {
				layer = layer.inner;
			}
			return layer;
		}
		if (layerIndex < 0) {
			StateB layer = this;
			layerIndex = Math.abs(layerIndex);
			for (int i=0; i<layerIndex; i++) {
				layer = layer.outer;
			}
			return layer;
		}
		throw new IllegalStateException();
	}
	public StateB getMostInnerLayer() {
		StateB layer = this;
		while (layer.inner != null)
			layer = layer.inner;
		return layer;
	}
	public StateB getMostOuterLayer() {
		StateB layer = this;
		while (layer.outer != null)
			layer = layer.outer;
		return layer;
	}
	
	public int getMostInnerIndex() {
		int layerIndex = 0;
		StateB layer = this;
		while (layer.inner != null) {
			layerIndex++;
			layer = layer.inner;
		}
		return layerIndex;
	}

	public int getMostOuterIndex() {
		int layerIndex = 0;
		StateB layer = this;
		while (layer.outer != null) {
			layerIndex--;
			layer = layer.outer;
		}
		return layerIndex;
	}
	
	public void expandInnerAndOuter() {
		StateB oldInner = getMostInnerLayer();
		oldInner.inner = new StateB(5, 5);
		oldInner.inner.outer = oldInner;
		
		StateB oldOuter = getMostOuterLayer();
		oldOuter.outer = new StateB(5, 5);
		oldOuter.outer.inner = oldOuter;
	}
	
	public void applyCounts() {
		
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				
				if (i == 2 && j == 2)
					continue;
				
				if (state[i][j] == BUG) {
					if (counts[i][j] != 1)
						state[i][j] = EMPTY;
				} else {
					if (counts[i][j] == 1 || counts[i][j] == 2)
						state[i][j] = BUG;
				}
			}
		}
	}
	
	public void step() {
		expandInnerAndOuter();
		
		StateB layer = getMostOuterLayer();
		while (layer != null) {
			//System.out.println("Applying counts");
			layer.updateCounts();
			layer = layer.inner;
		}
		
		layer = getMostOuterLayer();
		while (layer != null) {
			layer.applyCounts();
			layer = layer.inner;
		}
	}
	
	public int getTopCount() {
		int count = 0;
		for (int j=0; j<5; j++) {
			if (state[0][j] == BUG)
				count++;
		}
		return count;
	}
	public int getBottomCount() {
		int count = 0;
		for (int j=0; j<5; j++) {
			if (state[4][j] == BUG)
				count++;
		}
		return count;
	}
	public int getLeftCount() {
		int count = 0;
		for (int i=0; i<5; i++) {
			if (state[i][0] == BUG)
				count++;
		}
		return count;
	}
	public int getRightCount() {
		int count = 0;
		for (int i=0; i<5; i++) {
			if (state[i][4] == BUG)
				count++;
		}
		return count;
	}
	
	public void updateCounts() {
		
		int innerTopCount    = (inner == null) ? 0 : inner.getTopCount();
		int innerBottomCount = (inner == null) ? 0 : inner.getBottomCount();
		int innerRightCount  = (inner == null) ? 0 : inner.getRightCount();
		int innerLeftCount   = (inner == null) ? 0 : inner.getLeftCount();
		
		//int outerTopCount    = (outer == null) ? 0 : outer.getTopCount();
		//int outerBottomCount = (outer == null) ? 0 : outer.getBottomCount();
		//int outerRightCount  = (outer == null) ? 0 : outer.getRightCount();
		//int outerLeftCount   = (outer == null) ? 0 : outer.getLeftCount();
		
		int outerTopCount    = (outer == null) ? 0 : outer.addBugIfValid(3, 2);
		int outerBottomCount = (outer == null) ? 0 : outer.addBugIfValid(1, 2);
		int outerRightCount  = (outer == null) ? 0 : outer.addBugIfValid(2, 1);
		int outerLeftCount   = (outer == null) ? 0 : outer.addBugIfValid(2, 3);		
		
		//             left              top                       right                 bottom
		counts[0][0] = outerRightCount     + outerBottomCount + addBugIfValid(0, 1) + addBugIfValid(1, 0);
		counts[0][1] = addBugIfValid(0, 0) + outerBottomCount + addBugIfValid(0, 2) + addBugIfValid(1, 1);
		counts[0][2] = addBugIfValid(0, 1) + outerBottomCount + addBugIfValid(0, 3) + addBugIfValid(1, 2);
		counts[0][3] = addBugIfValid(0, 2) + outerBottomCount + addBugIfValid(0, 4) + addBugIfValid(1, 3);
		counts[0][4] = addBugIfValid(0, 3) + outerBottomCount + outerLeftCount      + addBugIfValid(1, 4);
		
		counts[1][0] = outerRightCount     + addBugIfValid(0, 0) + addBugIfValid(1, 1) + addBugIfValid(2, 0);
		counts[1][1] = addBugIfValid(1, 0) + addBugIfValid(0, 1) + addBugIfValid(1, 2) + addBugIfValid(2, 1);
		counts[1][2] = addBugIfValid(1, 1) + addBugIfValid(0, 2) + addBugIfValid(1, 3) + innerTopCount;
		counts[1][3] = addBugIfValid(1, 2) + addBugIfValid(0, 3) + addBugIfValid(1, 4) + addBugIfValid(2, 3);
		counts[1][4] = addBugIfValid(1, 3) + addBugIfValid(0, 4) + outerLeftCount      + addBugIfValid(2, 4);
		
		counts[2][0] = outerRightCount     + addBugIfValid(1, 0) + addBugIfValid(2, 1) + addBugIfValid(3, 0);
		counts[2][1] = addBugIfValid(2, 0) + addBugIfValid(1, 1) + innerLeftCount      + addBugIfValid(3, 1);
		
		counts[2][3] = innerRightCount     + addBugIfValid(1, 3) + addBugIfValid(2, 4) + addBugIfValid(3, 3);
		counts[2][4] = addBugIfValid(2, 3) + addBugIfValid(1, 4) + outerLeftCount      + addBugIfValid(3, 4);
		
		counts[3][0] = outerRightCount     + addBugIfValid(2, 0) + addBugIfValid(3, 1) + addBugIfValid(4, 0);
		counts[3][1] = addBugIfValid(3, 0) + addBugIfValid(2, 1) + addBugIfValid(3, 2) + addBugIfValid(4, 1);
		counts[3][2] = addBugIfValid(3, 1) + innerBottomCount    + addBugIfValid(3, 3) + addBugIfValid(4, 2);
		counts[3][3] = addBugIfValid(3, 2) + addBugIfValid(2, 3) + addBugIfValid(3, 4) + addBugIfValid(4, 3);
		counts[3][4] = addBugIfValid(3, 3) + addBugIfValid(2, 4) + outerLeftCount      + addBugIfValid(4, 4);
		
		counts[4][0] = outerRightCount     + addBugIfValid(3, 0) + addBugIfValid(4, 1) + outerTopCount;
		counts[4][1] = addBugIfValid(4, 0) + addBugIfValid(3, 1) + addBugIfValid(4, 2) + outerTopCount;
		counts[4][2] = addBugIfValid(4, 1) + addBugIfValid(3, 2) + addBugIfValid(4, 3) + outerTopCount;
		counts[4][3] = addBugIfValid(4, 2) + addBugIfValid(3, 3) + addBugIfValid(4, 4) + outerTopCount;
		counts[4][4] = addBugIfValid(4, 3) + addBugIfValid(3, 4) + outerLeftCount      + outerTopCount;
	}
	
	public int addBugIfValid(int i, int j) {
		if (i >= 0 && i < height &&
			j >= 0 && j < width &&
			state[i][j] == BUG) {
				return 1;
		} else {
			return 0;
		}
	}
	
	public void print() {
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++)
				System.out.print(state[i][j]);
			System.out.println();
		}
	}
	public void printCounts() {
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				System.out.print(counts[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	public void printAll() {
		int outerIndex = getMostOuterIndex();
		int innerIndex = getMostInnerIndex();
		for (int layer = outerIndex; layer<= innerIndex; layer++) {
			System.out.println("Layer: " + layer);
			getLayer(layer).print();
			System.out.println();
		}
	}
	
	public void printAllCounts() {
		int outerIndex = getMostOuterIndex();
		int innerIndex = getMostInnerIndex();
		for (int layer = outerIndex; layer<= innerIndex; layer++) {
			System.out.println("Layer: " + layer);
			getLayer(layer).printCounts();
			System.out.println();
		}
	}
	
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				s.append(state[i][j]);
			}
		}
		return s.toString();
	}
	
	public void loadFromFile(File file) throws IOException {
		List<String> lines = new ArrayList<>();
		
		BufferedReader in = new BufferedReader(new FileReader(file));
		try {
			String line;
			while ((line = in.readLine()) != null) {
				if (line.trim().isEmpty())
					continue;
				
				lines.add(line);
			}
		}
		finally {
			in.close();
		}
		
		for (int i=0; i<lines.size(); i++) {
			for (int j=0; j<5; j++)
				this.state[i][j] = lines.get(i).charAt(j);
		}
	}
	
	public int getBiodiversityRating() {
		int count = 0;
		
		int placeValue = 1;
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				if (state[i][j] == BUG)
					count+= placeValue;
				placeValue *= 2;
			}
		}
		
		return count;
	}
	
	public int getBugCount() {
		int count = 0;
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				if (state[i][j] == BUG)
					count++;
			}
		}
		return count;
	}
	
	public int getTotalBugCount() {
		int count = 0;
		
		StateB layer = getMostOuterLayer();
		while (layer != null) {
			count += layer.getBugCount();
			layer = layer.inner;
		}
		
		return count;
	}
	
}
