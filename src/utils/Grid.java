package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * A grid is an object mapping from an (x,y) coordinate to instanced T object.
 * 
 * @param <T>
 */
public class Grid<T> {

	List<List<T>> _grid = new ArrayList<>();

	public Grid() {
	}

	public void insertRow(List<T> row) {
		_grid.add(row);
	}

	public T at(int x, int y) {
		return at(new Point(x, y));
	}

	public T at(Point p) {
		return _grid.get(p._x).get(p._y);
	}

	public List<T> neighbours(Point p, Direction dir) {
		List<T> neighbours = new ArrayList<>();

		for (Point neighbour : p.neighbours()) {
			if (withinGrid(neighbour) && withinDirection(p, neighbour,dir)) {
				neighbours.add(at(neighbour));
			}
		}

		return neighbours;
	}

	private boolean withinDirection(Point p, Point neighbour, Direction dir) {
		if(dir == Direction.NON_DIAGONAL) {
			return p._x == neighbour._x|| p._y == neighbour._y;
		}
		
		return false;
	}

	public List<T> neighbours(Point p) {
		List<T> neighbours = new ArrayList<>();

		for (Point neighbour : p.neighbours()) {
			if (withinGrid(neighbour)) {
				neighbours.add(at(neighbour));
			}
		}

		return neighbours;
	}

	public void traverse(BiConsumer<Point, T> consumer) {
		for (int x = 0; x < _grid.size(); x++) {
			for (int y = 0; y < _grid.get(x).size(); y++) {
				Point p = new Point(x, y);
				consumer.accept(p, at(p));
			}
		}
	}

	private boolean withinGrid(Point p) {
		if (p._x < 0 || p._x >= _grid.get(0).size()) {
			return false;
		}

		if (p._y < 0 || p._y >= _grid.size()) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (var row : _grid) {
			sb.append(row + "\n");
		}

		return sb.toString();
	}

	public enum Direction {
		DIAGONAL, NON_DIAGONAL;
	}
}
