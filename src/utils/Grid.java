package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class Grid<T> {

	List<List<T>> _grid = new ArrayList<>();

	public Grid() {
	}

	public void insertRow(List<T> row) {
		_grid.add(row);
	}

	public List<List<T>> get() {
		return _grid;
	}

	public Stream<List<T>> stream() {
		return _grid.stream();
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
			if (withinGrid(neighbour) && withinDirection(p, neighbour, dir)) {
				neighbours.add(at(neighbour));
			}
		}

		return neighbours;
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

	public <K> Grid<K> traverseAndApply(BiConsumer<Point, T> consumer, Function<T, K> fun) {
		Grid<K> newGrid = new Grid<K>();

		for (int x = 0; x < _grid.size(); x++) {
			List<K> row = new ArrayList<>();
			for (int y = 0; y < _grid.get(x).size(); y++) {
				Point p = new Point(x, y);

				if (consumer != null) {
					consumer.accept(p, at(p));
				}

				if (fun != null) {
					K k = fun.apply(at(p));
					row.add(k);
				}
			}

			newGrid.insertRow(row);
		}

		return newGrid;

	}

	public <K> Grid<K> map(Function<T, K> fun) {
		return traverseAndApply(null,fun);
	}

	private boolean withinDirection(Point p, Point neighbour, Direction dir) {
		if (dir == Direction.NON_DIAGONAL) {
			return p._x == neighbour._x || p._y == neighbour._y;
		}

		return false;
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
