package days.day1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import day.Day;
import day.Part;
import utils.Parsing;

public class Day1 implements Day {

	@Override
	public void solveA(String input) {

		Lists lists = parse(input, Lists.class, Part.A);
		List<Integer> left = lists.left();
		List<Integer> right = lists.right();

		Collections.sort(left);
		Collections.sort(right);

		int sum = 0;
		for (int i = 0; i < left.size(); i++) {
			sum += diff(left.get(i), right.get(i));
		}

		System.out.println(sum);
	}

	@Override
	public void solveB(String input) {
		Lists lists = parse(input, Lists.class, Part.B);
		List<Integer> left = lists.left();
		List<Integer> right = lists.right();

		int sum = 0;
		for (int leftVal : left) {
			int rightOccurences = Collections.frequency(right, leftVal);
			sum += leftVal * rightOccurences;
		}

		System.out.println(sum);
	}

	private int diff(int a, int b) {
		return Math.abs(a - b);
	}

	@Override
	public Object parseA(String input) {
		List<Integer> left = new ArrayList<>();
		List<Integer> right = new ArrayList<>();

		for (var line : Parsing.lines(input)) {
			var words = Parsing.words(line);

			left.add(Parsing.stoi(words.get(0)));
			right.add(Parsing.stoi(words.get(1)));
		}

		return new Lists(left, right);
	}

	@Override
	public Object parseB(String input) {
		return parseA(input);
	}
}
