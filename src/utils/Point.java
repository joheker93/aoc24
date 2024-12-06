package utils;

import java.util.ArrayList;
import java.util.List;

import utils.enums.Direction;

public class Point {

	private int _x;
	private int _y;

	public Point(int x, int y) {
		_x = x;
		_y = y;
	}

	public Point(Point p) {
		_x = p.getX();
		_y = p.getY();
	}

	public void set(int x, int y) {
		_x = x;
		_y = y;
	}

	public void set(Point p) {
		_x = p.getX();
		_y = p.getY();
	}

	public Point move(int dx, int dy) {
		return new Point(_x + dx, _y + dy);
	}

	public Point move(Point dp) {
		return new Point(_x + dp.getX(), _y + dp.getY());
	}

	public Point rotate(Point rotationVector) {
		return new Point(rotationVector.getX(), rotationVector.getY());
	}

	public Point rotateRight() {
		return rotate(new Point(-_y, _x));
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
		return _x * 31 + _y;
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
