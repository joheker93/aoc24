package days.day10;

import java.util.HashSet;
import java.util.Set;

import day.Day;
import day.Part;
import utils.Grid;
import utils.Parsing;
import utils.Point;
import utils.enums.Direction;
import utils.graph.Graph;
import utils.graph.Node;

public class Day10 implements Day {

	@Override
	public void solveA(String input) {
		Grid<Integer> gridMap = parse(input, HikingMap.class, Part.A).map();
		Graph<Integer, Point> graph = Graph.fromGrid(gridMap, Direction.NON_DIAGONAL, i -> i);
		Set<Node<Integer, Point>> starts = new HashSet<>();
		Set<Node<Integer, Point>> goals = new HashSet<>();

		gridMap.traverse((pos, val) -> {
			if (val.equals(0)) {
				starts.add(new Node<Integer, Point>(val, pos));
			}

			if (val.equals(9)) {
				goals.add(new Node<Integer, Point>(val, pos));
			}
		});

		int sum = 0;
		for (var start : starts) {
			for (var goal : goals) {
				int paths = paths(graph, start, goal, new HashSet<>());
				if (paths > 0) {
					sum++;
				}
			}
		}

		System.out.println(sum);
	}

	@Override
	public void solveB(String input) {
		Grid<Integer> gridMap = parse(input, HikingMap.class, Part.B).map();
		Graph<Integer, Point> graph = Graph.fromGrid(gridMap, Direction.NON_DIAGONAL, i -> i);
		Set<Node<Integer, Point>> starts = new HashSet<>();
		Set<Node<Integer, Point>> goals = new HashSet<>();

		gridMap.traverse((pos, val) -> {
			if (val.equals(0)) {
				starts.add(new Node<Integer, Point>(val, pos));
			}

			if (val.equals(9)) {
				goals.add(new Node<Integer, Point>(val, pos));
			}
		});

		int sum = 0;
		for (var start : starts) {
			for (var goal : goals) {
				sum += paths(graph, start, goal, new HashSet<>());
			}
		}

		System.out.println(sum);
	}

	private int paths(Graph<Integer, Point> g, Node<Integer, Point> current, Node<Integer, Point> goal,
			HashSet<Node<Integer, Point>> visited) {

		if (current.equals(goal)) {
			return 1;
		}

		visited.add(current);
		int count = 0;

		for (var neighbour : g.adjecencies(current)) {
			if (!visited.contains(neighbour) && neighbour.getValue().equals(current.getValue() + 1)) {
				count += paths(g, neighbour, goal, visited);
			}
		}

		visited.remove(current);
		return count;
	}

	@Override
	public Object parseA(String input) {
		return new HikingMap(Parsing.asGrid(input, "", s -> {
			return s.equals(".") ? Integer.MAX_VALUE : Parsing.stoi(s);
		}));
	}

	@Override
	public Object parseB(String input) {
		return parseA(input);
	}

	private record HikingMap(Grid<Integer> map) {
	};
}
