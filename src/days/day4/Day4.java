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
	public void solveA(String input) {
		Grid<String> grid = parse(input, ParseGrid.class, Part.A).grid();

		List<List<String>> rows = grid.rows();
		List<List<String>> cols = grid.columns();
		List<List<String>> diags = grid.diagonals();

		int count = 0;
		for (var line : Sequence.concat(rows, cols, diags)) {
			if (line.size() < 4) {
				continue;
			}

			String str = String.join("", line);
			Pattern xmas = Pattern.compile("XMAS");
			Matcher matcher = xmas.matcher(str);
			while (matcher.find()) {
				count++;
			}

			str = String.join("", line);
			Pattern samx = Pattern.compile("SAMX");
			matcher = samx.matcher(str);
			while (matcher.find()) {
				count++;
			}

		}
		System.out.println(count);
	}

	@Override
	public void solveB(String input) {
		Grid<String> grid = parse(input, ParseGrid.class, Part.B).grid();

		AtomicInteger count = new AtomicInteger();
		grid.traverse((p, s) -> {
			if (s.equals("A")) {
				List<Point> neighbours = grid.neighbourIndexes(p, Direction.DIAGONAL);

				Point NW = new Point(p.getX() - 1, p.getY() - 1);
				Point NE = new Point(p.getX() + 1, p.getY() - 1);
				Point SE = new Point(p.getX() + 1, p.getY() + 1);
				Point SW = new Point(p.getX() - 1, p.getY() + 1);

				if (neighbours.contains(NW) && neighbours.contains(NE) && neighbours.contains(SE)
						&& neighbours.contains(SW)) {
					String r1 = grid.at(NW) + s + grid.at(SE); // \-diagonal
					String r2 = grid.at(NE) + s + grid.at(SW); // /-diagonal

					boolean b = r1.equals("MAS") || r1.equals("SAM");
					boolean b2 = r2.equals("MAS") || r2.equals("SAM");
					if (b && b2) {
						count.incrementAndGet();
					}

				}
			}
		});
		System.out.println(count.get());
	}

	@Override
	public Object parseA(String input) {

		return new ParseGrid(Parsing.asGrid(input, "", s -> s));
	}

	@Override
	public Object parseB(String input) {
		return parseA(input);
	}

	private record ParseGrid(Grid<String> grid) {
	};
}
