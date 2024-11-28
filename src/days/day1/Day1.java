package days.day1;

import day.Day;
import day.Part;
import utils.Grid;
import utils.Grid.Direction;
import utils.Parsing;
import utils.graph.Graph;
import utils.graph.Node;

public class Day1 implements Day {

	@Override
	public void solveA(String input) {

		Grid<String> grid = parse(input, ParsedGrid.class, Part.A).grid;

		Graph<String> g = Graph.fromGrid(grid, Direction.NON_DIAGONAL);

		g.dfs(new Node<>("23"), (c, n) -> {
			System.out.println("Going from " + c + " to " + n);
			if (n.equals(new Node("108466"))) {
				return false;
			}
			return true;
		});
		System.out.println(g.shortestPath(null).get(new Node<>("40")));
//		System.out.println("Grid generated " + g);
//		for (var entry : g.shortestPath(null).entrySet()) {
////			System.out.println("To " + entry.getKey() + " = " + entry.getValue());
//		}
	}

	@Override
	public void solveB(String input) {
		System.out.println(parse(input, String.class, Part.B));

	}

	@Override
	public Object parseA(String input) {
		ParsedGrid parseObject = new ParsedGrid();

		parseObject.grid = Parsing.asGrid(input, s -> s);
		return parseObject;
	}

	@Override
	public Object parseB(String input) {
		return "Hello";
	}
}
