package days.day03;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import day.Day;
import day.Part;
import utils.Pair;
import utils.Parsing;

public class Day3 implements Day {

	@Override
	public void solveA(String input) {
		List<Pair<Integer, Integer>> pairs = parse(input, Pairs.class, Part.A).pairs();
		int sum = 0;

		for (var pair : pairs) {
			sum += pair.fst() * pair.snd();
		}
		
		System.out.println(sum);
	}

	@Override
	public void solveB(String input) {
		List<Pair<Integer, Integer>> pairs = parse(input, Pairs.class, Part.B).pairs();

		int sum = 0;
		for (var pair : pairs) {
			sum += pair.fst() * pair.snd();
		}

		System.out.println(sum);
	}

	@Override
	public Object parseA(String input) {
		String mulPattern = "mul\\((\\d+),(\\d+)\\)";
		Pattern p = Pattern.compile(mulPattern);
		Matcher m = p.matcher(input);
		List<Pair<Integer, Integer>> pairs = new ArrayList<Pair<Integer, Integer>>();

		while (m.find()) {
			int left = Parsing.stoi(m.group(1));
			int right = Parsing.stoi(m.group(2));
			pairs.add(Pair.of(left, right));
		}

		return new Pairs(pairs);
	}

	@Override
	public Object parseB(String input) {
		String mulPattern = "mul\\((?<left>\\d+),(?<right>\\d+)\\)";
		String doPattern = "(?<do>do\\(\\))";
		String dontPattern = "(?<dont>don\\'t\\(\\))";
		
		Pattern p = Pattern.compile(mulPattern + "|" + doPattern + "|" + dontPattern);
		Matcher m = p.matcher(input);
		List<Pair<Integer, Integer>> pairs = new ArrayList<Pair<Integer, Integer>>();

		boolean active = true;
		while (m.find()) {
			if (m.group("do") != null) {
				active = true;
				continue;
			}

			if (m.group("dont") != null) {
				active = false;
				continue;
			}

			int mulLeft = Parsing.stoi(m.group("left"));
			int mulRight = Parsing.stoi(m.group("right"));

			if (active) {
				pairs.add(Pair.of(mulLeft, mulRight));
			}
		}

		return new Pairs(pairs);
	}

	private record Pairs(List<Pair<Integer, Integer>> pairs) {
	};
}
