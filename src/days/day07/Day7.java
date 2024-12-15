package days.day07;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import day.Day;
import day.Part;
import utils.Parsing;
import utils.Sequence;

public class Day7 implements Day {

	@Override
	public void solveA(final String input) {
		final Map<Long, Deque<Long>> equations = parse(input, Equations.class, Part.A).equations();
		final boolean concat = false;
		final long result = solve(equations, concat);

		System.out.println(result);
	}

	@Override
	public void solveB(final String input) {
		final Map<Long, Deque<Long>> equations = parse(input, Equations.class, Part.A).equations();
		final boolean concat = true;
		final long result = solve(equations, concat);

		System.out.println(result);
	}

	private long solve(final Map<Long, Deque<Long>> equations, final boolean concat) {
		long sum = 0;
		for (final var entry : equations.entrySet()) {
			final List<Long> longs = new ArrayList<>(entry.getValue());

			final boolean valid = solver(entry.getKey(), longs, concat);
			if (valid) {
				sum += entry.getKey();
			}
		}

		return sum;
	}

	public boolean solver(final long res, final List<Long> values, final boolean concat) {
		if (values.size() == 1) {
			return values.get(0) == res;
		}

		final List<Long> sumList = new ArrayList<>(values);
		final Long lastAdd = sumList.remove(sumList.size() - 1);
		final boolean addRes = solver(res - lastAdd, sumList, concat);

		final List<Long> prodList = new ArrayList<>(values);
		final long lastMul = prodList.remove(prodList.size() - 1);

		boolean mulRes = false;
		if (res % lastMul == 0) {
			mulRes = solver(res / lastMul, prodList, concat);
		}

		boolean concatRes = false;
		if (concat) {
			final List<Long> concatList = new ArrayList<>(values);
			final long last = concatList.remove(concatList.size() - 1);
			final long magLat = base(last) * 10;

			concatRes = res % magLat == last && solver(res / magLat, concatList, concat);
		}

		return addRes || mulRes || concatRes;
	}

	public long base(long a) {
		if (a == 0) {
			return 1;
		}

		long magnitude = 1;
		while (a >= 10) {
			a /= 10;
			magnitude *= 10;
		}
		return magnitude;
	}

	@Override
	public Object parseA(final String input) {
		final Map<Long, Deque<Long>> equations = new HashMap<>();
		for (final var line : Parsing.lines(input)) {
			final List<String> eqs = Parsing.split(line, ":");
			equations.put(Parsing.stol(eqs.get(0)),
					new ArrayDeque<>(Sequence.sequence(eqs.get(1).strip(), " ", Parsing::stol)));
		}

		return new Equations(equations);
	}

	@Override
	public Object parseB(final String input) {
		return parseA(input);
	}

	private record Equations(Map<Long, Deque<Long>> equations) {
	};

}
