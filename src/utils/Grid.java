package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import utils.enums.Direction;

public class Grid<T> {

	List<List<T>> _grid = new ArrayList<>();

	public Grid() {
	}

	public Grid(final Grid<T> grid) {
		_grid = grid.get();
	}

	public void insertRow(final List<T> row) {
		_grid.add(new ArrayList<>(row));
	}

	public List<List<T>> get() {
		return _grid;
	}

	public Stream<List<T>> stream() {
		return _grid.stream();
	}

	public T at(final int x, final int y) {
		return at(new Point(x, y));
	}

	public T at(final Point p) {
		return _grid.get(p.getY()).get(p.getX());
	}

	public int width() {
		return _grid.get(0).size();
	}

	public int height() {
		return _grid.size();
	}

	public List<List<T>> rows() {
		return _grid;
	}

	public List<List<T>> columns() {
		return transpose().get();
	}

	public List<List<T>> diagonals() {
		List<List<T>> diags = new ArrayList<List<T>>();

		diags.addAll(diagonals(false));
		diags.addAll(diagonals(true));

		return diags;
	}

	private List<List<T>> diagonals(final boolean offDiagonals) {

		final List<List<T>> diagonals = new ArrayList<>();

		Grid<T> grid = offDiagonals ? flipHorizontally() : this;
		final BiFunction<Point, Integer, Point> diagXY = (p, i) -> new Point(p.getX() + i, p.getY() - i);

		int outerBound = Math.max(grid.width(), grid.height());
		for (int i = 0; i < outerBound; i++) {
			final Point startP = new Point(0, i);
			final List<T> diag = new ArrayList<>();

			for (int j = 0; j < outerBound; j++) {
				final Point next = diagXY.apply(startP, j);
				if (withinGrid(next)) {
					diag.add(grid.at(next));
				} else {
					break;
				}
			}

			if (!diag.isEmpty()) {
				diagonals.add(diag);
			}
		}

		for (int i = 1; i < outerBound; i++) {
			final Point startP = new Point(i, grid.height() - 1);
			List<T> diag = new ArrayList<>();

			for (int j = 0; j <= outerBound; j++) {
				Point next = diagXY.apply(startP, j);
				if (withinGrid(next)) {
					diag.add(grid.at(next));
				} else {
					break;
				}
			}

			if (!diag.isEmpty()) {
				diagonals.add(diag);
			}
		}

		return diagonals;
	}

	public Grid<T> rotateRight() {
		Grid<T> g = new Grid<>();

		List<List<T>> rows = transpose().rows();

		for (var row : rows) {
			ArrayList<T> r = new ArrayList<>(row);
			Collections.reverse(r);
			g.insertRow(r);
		}

		return g;
	}

	public Grid<T> transpose() {
		final Grid<T> g = new Grid<>();

		for (int c = 0; c < width(); c++) {
			final List<T> row = new ArrayList<>();
			for (int r = 0; r < height(); r++) {
				row.add(_grid.get(r).get(c));
			}
			g.insertRow(row);
		}

		return g;
	}

	public Grid<T> flipVertically() {
		Grid<T> g = new Grid<>();

		List<List<T>> grid = new ArrayList<>(_grid);
		Collections.reverse(grid);
		for (var row : grid) {
			g.insertRow(row);
		}

		return g;
	}

	public Grid<T> flipHorizontally() {
		return rotateRight().rotateRight().flipVertically();
	}

	public List<Point> neighbourIndexes(final Point p, final Direction dir) {
		final List<Point> neighbours = new ArrayList<>();

		for (final Point neighbour : p.neighbours(dir)) {
			if (withinGrid(neighbour)) {
				neighbours.add(neighbour);
			}
		}

		return neighbours;
	}

	public List<T> neighbours(final Point p, final Direction dir) {
		final List<T> neighbours = new ArrayList<>();

		for (final Point neighbour : p.neighbours(dir)) {
			if (withinGrid(neighbour)) {
				neighbours.add(at(neighbour));
			}
		}

		return neighbours;
	}

	public List<T> neighbours(final Point p) {
		return neighbours(p, Direction.ANY);
	}

	public void traverse(final BiConsumer<Point, T> consumer) {
		for (int x = 0; x < _grid.size(); x++) {
			for (int y = 0; y < _grid.get(x).size(); y++) {
				final Point p = new Point(x, y);
				consumer.accept(p, at(p));
			}
		}
	}

	public <K> Grid<K> traverseAndApply(final BiConsumer<Point, T> consumer, final Function<T, K> fun) {
		final Grid<K> newGrid = new Grid<K>();

		for (int x = 0; x < _grid.size(); x++) {
			final List<K> row = new ArrayList<>();
			for (int y = 0; y < _grid.get(x).size(); y++) {
				final Point p = new Point(x, y);

				if (consumer != null) {
					consumer.accept(p, at(p));
				}

				if (fun != null) {
					final K k = fun.apply(at(p));
					row.add(k);
				}
			}

			newGrid.insertRow(row);
		}

		return newGrid;

	}

	public <K> Grid<K> map(final Function<T, K> fun) {
		return traverseAndApply(null, fun);
	}

	private boolean withinGrid(final Point p) {
		if (p.getX() < 0 || p.getX() >= _grid.get(0).size()) {
			return false;
		}

		if (p.getY() < 0 || p.getY() >= _grid.size()) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		for (final var row : _grid) {
			sb.append(row + "\n");
		}

		return sb.toString();
	}

}
