package days.day12;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import day.Day;
import day.Part;
import utils.Grid;
import utils.Pair;
import utils.Parsing;
import utils.Point;
import utils.enums.Direction;

public class Day12 implements Day {

	@Override
	public void solveA(final String input) {
		final Grid<String> garden = parse(input, Garden.class, Part.A).garden();

		Set<Point> alreadyFlooded = new HashSet<>();
		AtomicInteger sum = new AtomicInteger();
		garden.traverse((pos, val) -> {
			if (!alreadyFlooded.add(pos)) {
				return;
			}

			HashSet<Point> region = new HashSet<>();
			Pair<Integer, Integer> p = floodFill(garden, pos, region);

			for (var elem : region) {
				alreadyFlooded.add(elem);
			}

			sum.addAndGet(p.fst() * p.snd());
		});

		System.out.println(sum);

	}

	@Override
	public void solveB(final String input) {
		final Grid<String> garden = parse(input, Garden.class, Part.A).garden();

		Set<Point> alreadyFlooded = new HashSet<>();
		AtomicInteger sum = new AtomicInteger();
		garden.traverse((pos, val) -> {
			if (!alreadyFlooded.add(pos)) {
				return;
			}

			Set<Point> region = new HashSet<>();
			Pair<Integer, Integer> res = floodFillCornersNew(garden, pos, region);

			for (var elem : region) {
				alreadyFlooded.add(elem);
			}
			sum.addAndGet(res.fst() * res.snd());

		});

		System.out.println(sum);
	}

	// perimiter, area
	private Pair<Integer, Integer> floodFill(Grid<String> garden, Point current, Set<Point> flooded) {
		flooded.add(current);

		int edges = 0;
		int area = 1;

		for (Point neighbour : current.neighbours(Direction.NON_DIAGONAL)) {
			if (!garden.withinGrid(neighbour)) {
				edges++;
			} else if (!garden.at(neighbour).equals(garden.at(current))) {
				edges++;
			} else if (!flooded.contains(neighbour)) {
				Pair<Integer, Integer> p = floodFill(garden, neighbour, flooded);
				edges += p.fst();
				area += p.snd();
			}
		}
		return Pair.of(edges, area);
	}

	// area, corners
	private Pair<Integer, Integer> floodFillCornersNew(Grid<String> garden, Point current, Set<Point> flooded) {

		flooded.add(current);

		int corners = 0;
		int visited = 1;

		Point NE = new Point(current.getX() + 1, current.getY() - 1);
		Point SE = new Point(current.getX() + 1, current.getY() + 1);
		Point SW = new Point(current.getX() - 1, current.getY() + 1);
		Point NW = new Point(current.getX() - 1, current.getY() - 1);
		Point N = new Point(current.getX(), current.getY() - 1);
		Point E = new Point(current.getX() + 1, current.getY());
		Point W = new Point(current.getX() - 1, current.getY());
		Point S = new Point(current.getX(), current.getY() + 1);

		if (isCorner(garden, current, NE, N, E)) {
			corners++;
		}
		if (isCorner(garden, current, NW, N, W)) {
			corners++;
		}
		if (isCorner(garden, current, SW, S, W)) {
			corners++;
		}
		if (isCorner(garden, current, SE, S, E)) {
			corners++;
		}

		for (var neighbour : garden.neighbourIndexes(current, Direction.NON_DIAGONAL)) {
			if (!flooded.contains(neighbour) && garden.at(neighbour).equals(garden.at(current))) {
				Pair<Integer, Integer> res = floodFillCornersNew(garden, neighbour, flooded);
				corners += res.snd();
				visited += res.fst();

			}
		}

		return Pair.of(visited, corners);
	}

	private boolean isCorner(Grid<String> garden, Point source, Point diag, Point left, Point right) {

		// just close your eyes and have faith
		if (!garden.withinGrid(diag) || !garden.at(diag).equals(garden.at(source))) {

			if (!garden.withinGrid(right) && !garden.withinGrid(left)) {
				return true;
			}

			if (garden.withinGrid(right) && garden.withinGrid(left) && !garden.at(right).equals(garden.at(source))
					&& !garden.at(left).equals(garden.at(source))) {
				return true;
			}

			if (garden.withinGrid(right) && garden.withinGrid(left) && garden.at(right).equals(garden.at(left))
					&& garden.at(right).equals(garden.at(source))) {
				return true;
			}

		}

		if (garden.withinGrid(diag) && garden.withinGrid(right) && garden.withinGrid(left)) {
			boolean crossCorner = garden.at(diag).equals(garden.at(source))
					&& !garden.at(left).equals(garden.at(source)) && !garden.at(right).equals(garden.at(source));
			if (crossCorner) {
				return true;
			}
		}

		if (!garden.withinGrid(diag)) {
			boolean rightDiffs = garden.withinGrid(right) && !garden.at(right).equals(garden.at(source));
			boolean leftDiffs = garden.withinGrid(left) && !garden.at(left).equals(garden.at(source));

			if (rightDiffs || leftDiffs) {
				return true;
			}
		}
		return false;

	}

	@Override
	public Object parseA(final String input) {
		return new Garden(Parsing.asGrid(input, "", s -> s));
	}

	@Override
	public Object parseB(final String input) {
		return parseA(input);
	}

	private record Garden(Grid<String> garden) {
	};

}
