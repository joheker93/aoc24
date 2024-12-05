package days.day4;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import day.Day;
import day.Part;
import utils.Grid;
import utils.Parsing;
import utils.Point;
import utils.Sequence;
import utils.enums.Direction;

public class Day4 implements Day {

	@Override
	public void solveA(final String input) {
		final Grid<String> grid = parse(input, ParseGrid.class, Part.A).grid();

		final List<List<String>> rows = grid.rows();
		final List<List<String>> cols = grid.columns();
		final List<List<String>> diags = grid.diagonals();

		int count = 0;
		for (final var line : Sequence.concat(rows, cols, diags)) {
			if (line.size() < 4) {
				continue;
			}

			String str = String.join("", line);
			final Pattern xmas = Pattern.compile("XMAS");
			Matcher matcher = xmas.matcher(str);
			while (matcher.find()) {
				count++;
			}

			str = String.join("", line);
			final Pattern samx = Pattern.compile("SAMX");
			matcher = samx.matcher(str);
			while (matcher.find()) {
				count++;
			}

		}
		System.out.println(count);
	}

	@Override
	public void solveB(final String input) {
		final Grid<String> grid = parse(input, ParseGrid.class, Part.B).grid();

		final AtomicInteger count = new AtomicInteger();
		grid.traverse((p, s) -> {
			if (s.equals("A")) {
				final List<Point> neighbours = grid.neighbourIndexes(p, Direction.DIAGONAL);

				final Point NW = new Point(p.getX() - 1, p.getY() - 1);
				final Point NE = new Point(p.getX() + 1, p.getY() - 1);
				final Point SE = new Point(p.getX() + 1, p.getY() + 1);
				final Point SW = new Point(p.getX() - 1, p.getY() + 1);

				if (neighbours.contains(NW) && neighbours.contains(NE) && neighbours.contains(SE)
						&& neighbours.contains(SW)) {
					final String r1 = grid.at(NW) + s + grid.at(SE); // \-diagonal
					final String r2 = grid.at(NE) + s + grid.at(SW); // /-diagonal

					final boolean b = r1.equals("MAS") || r1.equals("SAM");
					final boolean b2 = r2.equals("MAS") || r2.equals("SAM");
					if (b && b2) {
						count.incrementAndGet();
					}

				}
			}
		});

		System.out.println(count.get());
	}

	@Override
	public Object parseA(final String input) {

		return new ParseGrid(Parsing.asGrid(input, "", s -> s));
	}

	@Override
	public Object parseB(final String input) {
		return parseA(input);
	}

	private record ParseGrid(Grid<String> grid) {
	};
}
