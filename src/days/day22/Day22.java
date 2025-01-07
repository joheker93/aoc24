package days.day22;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import day.Day;
import day.Part;
import utils.Parsing;

public class Day22 implements Day {

	private static final int SELLS = 2000;

	@Override
	public void solveA(String input) {
		var starts = parse(input, Secrets.class, Part.A).startingValues();

		long sum = 0;
		for (var start : starts) {
			long current = start;
			for (int i = 0; i < SELLS; i++) {
				current = randomGen(current);
			}
			sum += current;
		}

		System.out.println(sum);

	}

	@Override
	public void solveB(String input) {
		var starts = parse(input, Secrets.class, Part.A).startingValues();

		Map<List<Integer>, Long> yields = new HashMap<>();

		for (var monkee : starts) {
			yields(monkee, yields);

		}

		System.out.println(Collections.max(yields.values()));
	}

	private void yields(long monkeyStart, Map<List<Integer>, Long> yields) {
		long current = monkeyStart;
		int lastValue = (int) (current % 10);

		Set<String> reached = new HashSet<>();
		List<Integer> diffs = new ArrayList<>(Arrays.asList(0, 0, 0, 0));

		for (int i = 0; i < SELLS; i++) {
			current = randomGen(current);
			int newVal = (int) (current % 10);

			diffs.remove(0);
			diffs.add(newVal - lastValue);

			if (i >= 3) {
				List<Integer> key = new ArrayList<>(diffs);

				if (reached.add(key.toString())) {
					yields.merge(key, (long) newVal, Long::sum);
				}
			}

			lastValue = newVal;
		}
	}

	private long randomGen(long secret) {
		var e1 = secret * 64;
		var newSecret = mix(e1, secret);
		newSecret = prune(newSecret);

		var e2 = newSecret / 32;
		newSecret = mix(e2, newSecret);
		newSecret = prune(newSecret);

		var e3 = newSecret * 2048;
		newSecret = mix(e3, newSecret);
		newSecret = prune(newSecret);

		return newSecret;
	}

	private long mix(long value, long secret) {
		return value ^ secret;
	}

	private long prune(long secret) {
		return secret % 16777216;
	}

	@Override
	public Object parseA(String input) {
		List<Long> startingValues = new ArrayList<>();
		for (var line : Parsing.lines(input)) {
			startingValues.add(Parsing.stol(line));
		}
		return new Secrets(startingValues);
	}

	@Override
	public Object parseB(String input) {
		return parseA(input);
	}

	private record Secrets(List<Long> startingValues) {
	};
}
