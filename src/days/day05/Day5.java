package days.day05;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import day.Day;
import day.Part;
import utils.Parsing;
import utils.Sequence;

public class Day5 implements Day {

	private final List<List<Integer>> _invalids = new ArrayList<List<Integer>>();

	@Override
	public void solveA(final String input) {
		final SafetyManual safetyManual = parse(input, SafetyManual.class, Part.A);

		final Map<Integer, List<Integer>> rules = safetyManual.rules();
		final List<List<Integer>> sections = safetyManual.sections();

		int count = 0;
		for (final var section : sections) {
			if (isValid(section, rules)) {
				count += section.get(section.size() / 2);
			} else {
				_invalids.add(section);
			}
		}

		System.out.println(count);

	}

	@Override
	public void solveB(final String input) {
		final SafetyManual safetyManual = parse(input, SafetyManual.class, Part.B);
		final Map<Integer, List<Integer>> rules = safetyManual.rules();

		int count = 0;
		for (final var section : _invalids) {
			final List<Integer> newSection = applyRules(section, rules);
			count += newSection.get(newSection.size() / 2);
		}

		System.out.println(count);

	}

	private List<Integer> applyRules(final List<Integer> section, final Map<Integer, List<Integer>> rules) {
		final List<Integer> result = new ArrayList<>();

		result.add(section.get(0));

		// try inserting by index until valid by rules
		for (final int val : section) {
			for (int i = 0; i < section.size(); i++) {
				if (i > result.size()) {
					break;
				}

				final ArrayList<Integer> test = new ArrayList<>(result);
				test.add(i, val);

				if (isValid(test, rules) && !result.contains(val)) {
					result.add(i, val);
				}
			}
		}

		return result;
	}

	// section is valid if for every rule that exists in the sequence
	// the index of the first element in the rules is lower than the index of the
	// other
	private boolean isValid(final List<Integer> section, final Map<Integer, List<Integer>> rules) {
		final Map<Integer, Integer> indexes = new HashMap<>();

		for (int i = 0; i < section.size(); i++) {
			indexes.put(section.get(i), i);
		}

		for (final var rule : rules.entrySet()) {
			final int first = rule.getKey();
			final List<Integer> thens = rule.getValue();

			for (final var then : thens) {
				if (!(indexes.containsKey(first) && indexes.containsKey(then))) {
					continue;
				}

				if (indexes.get(first) > indexes.get(then)) {
					return false;
				}

			}
		}

		return true;
	}

	@Override
	public Object parseA(final String input) {
		final Pattern p = Pattern.compile("(?<left>\\d+)\\|(?<right>\\d+)");
		final Matcher m = p.matcher(input);

		final Map<Integer, List<Integer>> rules = new HashMap<>();

		while (m.find()) {
			final String left = m.group("left");
			final String right = m.group("right");

			if (left != null && right != null) {
				rules.merge(Parsing.stoi(left), new ArrayList<Integer>(List.of(Parsing.stoi(right))),
						(oldVal, newVal) -> {
							final var list = new ArrayList<>(oldVal);
							list.addAll(newVal);
							return list;
						});
			}

		}

		final List<String> sequences = Sequence.dropWhile(l -> !l.isEmpty(), Parsing.lines(input));
		sequences.remove(0);

		final List<List<Integer>> sections = new ArrayList<>();
		for (final var line : sequences) {
			sections.add(Sequence.intSequence(line));
		}

		return new SafetyManual(rules, sections);
	}

	@Override
	public Object parseB(final String input) {
		return parseA(input);
	}

	private record SafetyManual(Map<Integer, List<Integer>> rules, List<List<Integer>> sections) {
	};
}
