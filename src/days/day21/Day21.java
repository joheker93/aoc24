package days.day21;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import day.Day;
import day.Part;
import utils.Grid;
import utils.Pair;
import utils.Parsing;
import utils.Point;
import utils.Sequence;
import utils.enums.Direction;

public class Day21 implements Day {

	private record State(String from, String to, int depth) {
	};

	private static final Map<State, Long> MEMO = new HashMap<>();
	private int DEPTH = -1;

	@Override
	public void solveA(final String input) {
		final var passwords = parse(input, Passwords.class, Part.A).passwords();
		MEMO.clear();
		solveForDepth(2, passwords);
	}

	@Override
	public void solveB(final String input) {
		final var passwords = parse(input, Passwords.class, Part.B).passwords();
		MEMO.clear();
		solveForDepth(25, passwords);
	}

	private void solveForDepth(final int depth, final List<String> passwords) {
		long finalResult = 0;

		DEPTH = depth;

		for (final var password : passwords) {

			final List<List<List<String>>> dirs = new ArrayList<>();
			for (final var pair : pairs(Sequence.sequence(password, "", s -> s))) {
				final var bfs = bfs(pair.fst(), pair.snd(), numPad());
				final var dir = dirs(bfs, numPad());
				dirs.add(dir);
			}

			final List<String> combinations = new ArrayList<>();

			generateCombinations(dirs, 0, new StringBuilder(), combinations);

			long res = Long.MAX_VALUE;
			for (final var combination : combinations) {
				long sum = 0;
				sum += cost("A", String.valueOf(combination.charAt(0)), 1, dirPad());
				for (final var pair : pairs(Sequence.sequence(combination, "", s -> s))) {
					sum += cost(pair.fst(), pair.snd(), 1, dirPad());
				}

				if (sum < res) {
					res = sum;
				}
			}

			finalResult += res * Parsing.stol(password.substring(1, password.length() - 1));
		}

		System.out.println(finalResult);
	}

	private long cost(final String from, final String to, final int depth, final Grid<String> pad) {

		final var val = MEMO.get(new State(from, to, depth));
		if (val != null) {
			return val;
		}

		final var paths = bfs(from, to, pad);
		if (depth == DEPTH) {
			return (long) paths.get(0).size();
		}

		long sum = Long.MAX_VALUE;
		for (final var dirPath : dirs(paths, dirPad())) {
			long subCost = 0;
			subCost += cost("A", dirPath.get(0), depth + 1, pad);

			for (final var pair : pairs(dirPath)) {
				subCost += cost(pair.fst(), pair.snd(), depth + 1, pad);
			}

			if (subCost < sum) {
				sum = subCost;
				MEMO.put(new State(from, to, depth), sum);
			}
		}

		return sum;
	}

	private List<List<String>> dirs(final List<List<String>> paths, final Grid<String> pad) {
		final List<List<String>> dirs = new ArrayList<>();

		for (final var path : paths) {
			final List<String> dirPath = new ArrayList<>();
			for (final Pair<String, String> val : pairs(path)) {
				if (pad.find(val.fst()).getX() < pad.find(val.snd()).getX()) {
					dirPath.add(">");
				}
				if (pad.find(val.fst()).getX() > pad.find(val.snd()).getX()) {
					dirPath.add("<");
				}
				if (pad.find(val.fst()).getY() > pad.find(val.snd()).getY()) {
					dirPath.add("^");
				}
				if (pad.find(val.fst()).getY() < pad.find(val.snd()).getY()) {
					dirPath.add("v");
				}
			}
			dirPath.add("A");
			dirs.add(dirPath);
		}
		return dirs;
	}

	private static void generateCombinations(final List<List<List<String>>> input, final int index,
			final StringBuilder current, final List<String> combinations) {

		if (index == input.size()) {
			combinations.add(current.toString());
			return;
		}

		for (final List<String> subList : input.get(index)) {
			final int lengthBefore = current.length();

			for (final String element : subList) {
				current.append(element);
			}

			generateCombinations(input, index + 1, current, combinations);

			current.setLength(lengthBefore);
		}
	}

	private List<Pair<String, String>> pairs(final List<String> path) {
		final List<Pair<String, String>> pairs = new ArrayList<>();
		for (int i = 1; i < path.size(); i++) {
			pairs.add(Pair.of(path.get(i - 1), path.get(i)));
		}

		return pairs;
	}

	private List<List<String>> bfs(final String from, final String to, final Grid<String> dirPad) {

		final List<List<String>> paths = new ArrayList<>();
		final Queue<List<String>> queue = new LinkedList<>();

		queue.add(new ArrayList<>(List.of(from)));

		long minSize = Long.MAX_VALUE;

		while (!queue.isEmpty()) {
			final List<String> currentPath = queue.poll();
			final String currentNode = currentPath.get(currentPath.size() - 1);

			if (currentNode.equals(to) && currentPath.size() <= minSize) {
				paths.add(new ArrayList<>(currentPath));
				minSize = currentPath.size();
				continue;
			}

			for (final var neighbour : dirPad.neighbourIndexes(dirPad.find(currentNode), Direction.NON_DIAGONAL)) {
				final String neighborValue = dirPad.at(neighbour);

				if (neighborValue.equals("#")) {
					continue;
				}

				if (currentPath.contains(neighborValue)) {
					continue;
				}

				final List<String> newPath = new ArrayList<>(currentPath);
				newPath.add(neighborValue);
				queue.add(newPath);
			}
		}

		return paths;
	}

	@Override
	public Object parseA(final String input) {
		final List<String> sequences = new ArrayList<>();
		for (final var line : Parsing.lines(input)) {
			sequences.add(new StringBuilder("A").append(line).toString());
		}

		return new Passwords(sequences);
	}

	@Override
	public Object parseB(final String input) {
		return parseA(input);
	}

	private record Passwords(List<String> passwords) {
	};

	private Grid<String> dirPad() {
		final Grid<String> dirPad = Grid.createGrid(2, 1, ".");

		dirPad.setValue(new Point(0, 0), "#");
		dirPad.setValue(new Point(1, 0), "^");
		dirPad.setValue(new Point(2, 0), "A");
		dirPad.setValue(new Point(0, 1), "<");
		dirPad.setValue(new Point(1, 1), "v");
		dirPad.setValue(new Point(2, 1), ">");

		return dirPad;
	}

	private Grid<String> numPad() {
		final Grid<String> numPad = Grid.createGrid(2, 3, ".");

		numPad.setValue(new Point(0, 0), "7");
		numPad.setValue(new Point(1, 0), "8");
		numPad.setValue(new Point(2, 0), "9");
		numPad.setValue(new Point(0, 1), "4");
		numPad.setValue(new Point(1, 1), "5");
		numPad.setValue(new Point(2, 1), "6");
		numPad.setValue(new Point(0, 2), "1");
		numPad.setValue(new Point(1, 2), "2");
		numPad.setValue(new Point(2, 2), "3");
		numPad.setValue(new Point(1, 3), "0");
		numPad.setValue(new Point(2, 3), "A");
		numPad.setValue(new Point(0, 3), "#");

		return numPad;
	}

}
