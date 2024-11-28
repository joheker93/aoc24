package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
		_x += dx;
		_y += dy;
	}

	public List<Point> neighbours() {
		List<Point> neighbours = new ArrayList<>();
		for (int i = _x - 1; i <= _x + 1; i++) {
			for (int j = _y - 1; j <= _y + 1; j++) {
				if (i == _x && j == _y) {
					continue;
				}
				neighbours.add(new Point(i, j));
			}
		}

		return neighbours;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_x, _y);
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
		return _x == other._x && _y == other._y;
	}

	@Override
	public String toString() {
		return "(" + _x + "," + _y + ")";
	}

}
