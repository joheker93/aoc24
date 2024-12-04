package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import utils.enums.Direction;

public class Point {

	int _x;
	int _y;

	public Point(int x, int y) {
		_x = x;
		_y = y;
	}

	public void set(int x, int y) {
		_x = x;
		_y = y;
	}

	public void move(int dx, int dy) {
		_x = getX() + dx;
		_y = getY() + dy;
	}

	public List<Point> neighbours(Direction dir) {
		List<Point> neighbours = new ArrayList<>();
		for (int i = getX() - 1; i <= getX() + 1; i++) {
			for (int j = getY() - 1; j <= getY() + 1; j++) {
				if (i == getX() && j == getY()) {
					continue;
				}

				Point p = new Point(i, j);
				if (withinDirection(p, dir)) {
					neighbours.add(p);
				}
			}
		}
		return neighbours;
	}

	private boolean withinDirection(Point p, Direction dir) {
		boolean nonDiagonal = getX() == p.getX() || getY() == p.getY();
		boolean diagonal = Math.abs(getX() - p.getX()) == Math.abs(getY() - p.getY());

		if (dir == Direction.NON_DIAGONAL) {
			return nonDiagonal;
		}

		if (dir == Direction.DIAGONAL) {
			return diagonal;
		}

		if (dir == Direction.ANY) {
			return diagonal || nonDiagonal;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(getX(), getY());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Point other = (Point) obj;
		return getX() == other.getX() && getY() == other.getY();
	}

	@Override
	public String toString() {
		return "(" + getX() + "," + getY() + ")";
	}

	public int getX() {
		return _x;
	}

	public int getY() {
		return _y;
	}
}
