package days.day8;

import java.util.HashSet;
import java.util.Set;

import day.Day;
import day.Part;
import utils.Grid;
import utils.Parsing;
import utils.Point;

public class Day8 implements Day {

	@Override
	public void solveA(String input) {
		Grid<String> grid = parse(input, ParsedGrid.class, Part.A).grid();
		Set<Point> antiNodes = solveFor(grid, Part.A);

		System.out.println(antiNodes.size());
	}

	@Override
	public void solveB(String input) {
		Grid<String> grid = parse(input, ParsedGrid.class, Part.A).grid();
		Set<Point> antiNodes = solveFor(grid, Part.B);

		System.out.println(antiNodes.size());
	}

	private Set<Point> solveFor(Grid<String> grid, Part part) {
		final Set<Point> antiNodes = new HashSet<>();

		grid.traverse((pos, val) -> {
			if (!val.equals(".")) {
				if (part == Part.A) {
					solveA(pos, val, grid, antiNodes);
				} else {
					solveB(pos, val, grid, antiNodes);
				}
			}
		});

		return antiNodes;
	}

	private void solveA(Point orig, String origVal, Grid<String> grid, Set<Point> antiNodes) {
		grid.traverse((pos, val) -> {
			if (pos.equals(orig)) {
				return;
			}

			if (!val.equals(origVal)) {
				return;
			}
			int xSlope = pos.getX() - orig.getX();
			int ySlope = pos.getY() - orig.getY();

			Point p = traverseLine(pos, xSlope, ySlope);

			if (grid.withinGrid(p)) {
				antiNodes.add(p);
			}

		});
	}

	private void solveB(Point orig, String origVal, Grid<String> grid, Set<Point> antiNodes) {
		antiNodes.add(orig);

		grid.traverse((pos, val) -> {
			if (pos.equals(orig)) {
				return;
			}

			if (!val.equals(origVal)) {
				return;
			}

			int xSlope = pos.getX() - orig.getX();
			int ySlope = pos.getY() - orig.getY();

			Point current = pos;

			while (grid.withinGrid(current)) {
				current = traverseLine(current, xSlope, ySlope);

				if (grid.withinGrid(current)) {
					antiNodes.add(current);
				}

			}
		});
	}

	private Point traverseLine(Point orig, int xSlope, int ySlope) {
		return new Point(orig.getX() + xSlope, orig.getY() + ySlope);
	}

	@Override
	public Object parseA(String input) {
		return new ParsedGrid(Parsing.asGrid(input, "", s -> s));
	}

	@Override
	public Object parseB(String input) {
		return parseA(input);
	}

	private record ParsedGrid(Grid<String> grid) {
	};

}
