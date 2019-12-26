package common;

/**
 * Location in a 2-dimensional array.  This specifies a row and column.
 */
public class Location {

	public final int row;
	public final int col;
	
	public Location(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	public boolean equals(Object o) {
		Location that = (Location)o;
		return (this.row == that.row && this.col == that.col);
	}
	public int hashCode() {
		return row ^ 7 + col;
	}
	
	public String toString() {
		return "Location(" + row + "," + col + ")";
	}
	
}
