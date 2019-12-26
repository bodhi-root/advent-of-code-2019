package day11;


public class Robot {
	
	int row;
	int col;
	Direction direction;
	
	public Robot(int row, int col) {
		this.row = row;
		this.col = col;
		this.direction = Direction.UP;
	}
	
	public void turnRight() {
		Direction next = null;
		switch(direction) {
		case UP:    next = Direction.RIGHT; break;
		case RIGHT: next = Direction.DOWN;  break;
		case DOWN:  next = Direction.LEFT;  break;
		case LEFT:  next = Direction.UP;    break;
		}
		this.direction = next;
	}
	public void turnLeft() {
		Direction next = null;
		switch(direction) {
		case UP:    next = Direction.LEFT;  break;
		case LEFT:  next = Direction.DOWN;  break;
		case DOWN:  next = Direction.RIGHT; break;
		case RIGHT: next = Direction.UP;    break;
		}
		this.direction = next;
	}
	public void advance() {
		int di = 0, dj = 0;
		switch(direction) {
		case UP:    di = -1; break;
		case DOWN:  di = +1; break;
		case LEFT:  dj = -1; break;
		case RIGHT: dj = +1; break;
		}
		this.row += di;
		this.col += dj;
	}
	
}

