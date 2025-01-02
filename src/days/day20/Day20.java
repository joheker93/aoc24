package days.day20;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import day.Day;
import day.Part;
import utils.Grid;
import utils.Pair;
import utils.Parsing;
import utils.Point;
import utils.enums.Direction;

public class Day20 implements Day {

	@Override
	public void solveA(String input) {
		Grid<String> grid = parse(input, Race.class, Part.A).grid();

		Point start = getPoint("S", grid);
		Point end = getPoint("E", grid);

		Map<Point, Long> originalPath = getPath(start, end, grid);

		System.out.println(countCheats(originalPath, grid, 2));
	}
	
	@Override
	public void solveB(String input) {
		Grid<String> grid = parse(input, Race.class, Part.B).grid();

		Point start = getPoint("S", grid);
		Point end = getPoint("E", grid);

		Map<Point, Long> originalPath = getPath(start, end, grid);

		System.out.println(countCheats(originalPath, grid, 20));
	}


	private long countCheats(Map<Point, Long> originalPath, Grid<String> grid, int cheatSize) {
		long cheats = 0;
		for (Point p : originalPath.keySet()) {

			List<Point> validCheats = validCheats(p, cheatSize, grid);
			for (var validCheat : validCheats) {
				long cheatCost = originalPath.get(validCheat);
				long pCost = originalPath.get(p);

				Pair<Point, Point> cheatPair = Pair.of(p, validCheat);

				if (cheatCost > pCost) {
					continue;
				}

				if ((pCost - cheatCost) - distance(cheatPair) < 100) {
					continue;
				}

				cheats++;
			}
		}
		return cheats;
	}

	private long distance(Pair<Point, Point> points) {
		Point p1 = points.fst();
		Point p2 = points.snd();
		return Math.abs(p2.getX() - p1.getX()) + Math.abs(p2.getY() - p1.getY());
	}

	private List<Point> validCheats(Point p, int cheatSize, Grid<String> grid) {
		List<Point> validCheats = new ArrayList<>();

		for (int k = p.getY() - cheatSize; k <= p.getY() + cheatSize; k++) {
			for (int j = p.getX() - cheatSize; j <= p.getX() + cheatSize; j++) {
				var cheatAttempt = new Point(j, k);

				if (!grid.withinGrid(cheatAttempt)) {
					continue;
				}

				if (Math.abs(j - p.getX()) + Math.abs(k - p.getY()) > cheatSize) {
					continue;
				}

				if (cheatAttempt.equals(p)) {
					continue;
				}

				if (grid.at(cheatAttempt).equals("#")) {
					continue;
				}

				validCheats.add(cheatAttempt);
			}
		}

		return validCheats;
	}

	private Map<Point, Long> getPath(Point start, Point end, Grid<String> grid) {
		Set<Point> visited = new HashSet<>();
		Queue<Point> queue = new LinkedList<>();
		queue.add(end);

		long dist = 0;
		Map<Point, Long> path = new HashMap<>();
		while (!queue.isEmpty()) {
			var current = queue.poll();
			if (!visited.add(current)) {
				continue;
			}

			path.put(current, dist++);

			if (current.equals(start)) {
				return path;
			}

			for (var neighbour : grid.neighbourIndexes(current, Direction.NON_DIAGONAL)) {
				if (grid.at(neighbour).equals("#")) {
					continue;
				}

				queue.add(neighbour);
			}
		}

		return path;
	}

	private Point getPoint(String search, Grid<String> grid) {
		AtomicReference<Point> res = new AtomicReference<>();
		grid.traverse((pos, val) -> {
			if (val.equals(search)) {
				res.set(pos);
			}
		});

		return res.get();
	}

	@Override
	public Object parseA(String input) {
		return new Race(Parsing.asGrid(input, "", s -> s));
	}

	@Override
	public Object parseB(String input) {
		return parseA(input);
	}

	private record Race(Grid<String> grid) {
	}

}