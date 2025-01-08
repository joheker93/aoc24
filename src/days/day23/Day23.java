package days.day23;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import day.Day;
import day.Part;
import utils.Parsing;
import utils.Sequence;

public class Day23 implements Day {

	@Override
	public void solveA(String input) {
		var connections = parse(input, Connections.class, Part.A).connections();

		List<List<String>> cliques = new ArrayList<>();
		findCliques(connections, 3, 0, new ArrayList<>(connections.keySet()), new ArrayList<>(), cliques);

		for (var clique : cliques) {
			System.out.println(clique);
		}

		int sum = 0;
		for (var clique : cliques) {
			for (var elem : clique) {
				if (elem.charAt(0) == 't') {
					sum++;
					System.out.println(clique);
					break;
				}
			}
		}

		System.out.println(sum);
	}

	@Override
	public void solveB(String input) {
		var connections = parse(input, Connections.class, Part.B).connections();

		List<String> clique = null;
		for (int i = 0; i < connections.keySet().size(); i++) {
			List<List<String>> cliques = new ArrayList<>();
			findCliques(connections, i, 0, new ArrayList<>(connections.keySet()), new ArrayList<>(), cliques);

			if (cliques.size() == 0) {
				break;
			}

			clique = cliques.get(0);
		}

		System.out.println(clique);
	}

	private void findCliques(Map<String, List<String>> graph, int size, int from, List<String> nodes,
			List<String> currentClique, List<List<String>> cliques) {

		if (currentClique.size() == size) {
			cliques.add(new ArrayList<>(currentClique));
			return;
		}

		for (int i = from; i < nodes.size(); i++) {
			var node = nodes.get(i);
			if (isClique(graph, currentClique, node)) {
				currentClique.add(node);
				findCliques(graph, size, i + 1, nodes, currentClique, cliques);
				currentClique.remove(currentClique.size() - 1);
			}
		}
	}

	private boolean isClique(Map<String, List<String>> graph, List<String> clique, String node) {
		for (var elem : clique) {
			if (!graph.get(elem).contains(node)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public Object parseA(String input) {
		Map<String, List<String>> connections = new HashMap<>();

		for (var line : Parsing.lines(input)) {
			String[] split = line.split("-");
			var left = split[0];
			var right = split[1];
			connections.merge(left, new ArrayList<>(List.of(right)), Sequence::merge);
			connections.merge(right, new ArrayList<>(List.of(left)), Sequence::merge);
		}

		return new Connections(connections);
	}

	@Override
	public Object parseB(String input) {
		return parseA(input);
	}

	private record Connections(Map<String, List<String>> connections) {
	};
}
