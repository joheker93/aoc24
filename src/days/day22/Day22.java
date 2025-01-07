package days.day22;

import java.util.ArrayList;
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

		Map<Long, Long> yields = new HashMap<>();

		for (var monkee : starts) {
			yields(monkee, yields);

		}

		System.out.println(Collections.max(yields.values()));
	}

	public void yields(long monkeyStart, Map<Long, Long> yields) {
		long current = monkeyStart;
		int lastValue = (int) (current % 10);

		Set<Long> reached = new HashSet<>();
		int[] diffs = new int[4];

		for (int i = 0; i < SELLS; i++) {
			current = randomGen(current);
			int newVal = (int) (current % 10);

			System.arraycopy(diffs, 1, diffs, 0, 3);
			diffs[3] = newVal - lastValue;

			if (i >= 3) {
				long key = uniqueKey(diffs);

				if (reached.add(key)) {
					yields.merge(key, (long) newVal, Long::sum);
				}
			}

			lastValue = newVal;
		}
	}

	private long uniqueKey(int[] diffs) {
		long key = 0;
		long prime = 31;
		for (int i = 0; i < diffs.length; i++) {
			key = key * prime + diffs[i];
		}
		return key;
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
