package days.day6;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import day.Day;
import day.Part;
import utils.Grid;
import utils.Pair;
import utils.Parsing;
import utils.Point;

public class Day6 implements Day {

	@Override
	public void solveA(final String input) {
		final Grid<String> grid = parse(input, ParsedGrid.class, Part.A).grid();
		final Set<Point> visited = traverse(grid, getStart(grid));

		System.out.println(visited.size());
	}

	@Override
	public void solveB(final String input) {

		final Grid<String> grid = parse(input, ParsedGrid.class, Part.B).grid();

		final Point start = getStart(grid);
		final AtomicInteger count = new AtomicInteger();

		final Set<Point> visited = traverse(grid, start);
		grid.traverse((pos, val) -> {

			if (!visited.contains(pos) || val.equals("^")) {
				return;
			}

			if (hasCycle(grid, start, pos)) {
				count.incrementAndGet();
			}

		});

		System.out.println(count.get());
	}

	private boolean hasCycle(final Grid<String> grid, final Point start, final Point blocker) {
		final Set<Pair<Point, Point>> history = new HashSet<>();
		Point current = start;
		Point direction = new Point(0, -1);

		while (true) {
			final Pair<Point, Point> transission = Pair.of(current, direction);

			if (!history.add(transission)) {
				return true;
			}

			final Point step = current.move(direction);

			if (!grid.withinGrid(step)) {
				return false;
			}

			final String value = grid.at(step);

			if (value.equals("#") || step.equals(blocker)) {
				direction = direction.rotateRight();
			} else {
				current = step;
			}
		}
	}

	private Set<Point> traverse(final Grid<String> grid, final Point start) {
		final Set<Point> visited = new HashSet<>();
		Point current = start;
		Point direction = new Point(0, -1);

		while (true) {
			visited.add(current);

			final Point step = current.move(direction);
			if (!grid.withinGrid(step)) {
				break;
			}

			direction = grid.at(step).equals("#") ? direction.rotateRight() : direction;
			current = grid.at(step).equals("#") ? current : step;
		}

		return visited;
	}

	private Point getStart(final Grid<String> grid) {
		final Point p = new Point(0, 0);
		grid.traverse((pos, val) -> {
			if (val.equals("^")) {
				p.set(pos);
			}
		});

		return p;
	}

	@Override
	public Object parseA(final String input) {
		return new ParsedGrid(Parsing.asGrid(input, "", s -> s));
	}

	@Override
	public Object parseB(final String input) {
		return parseA(input);
	}

	private record ParsedGrid(Grid<String> grid) {
	};
}
