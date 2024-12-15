package days.day11;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import day.Day;
import day.Part;
import utils.Pair;
import utils.Parsing;

public class Day11 implements Day {

	@Override
	public void solveA(String input) {
		List<Long> sequence = parse(input, Sequence.class, Part.A).sequence();
		Map<Pair<Long, Long>, Long> memory = new HashMap<>();

		long val = transform(sequence, 25, memory);
		System.out.println(val);
	}

	@Override
	public void solveB(String input) {
		List<Long> sequence = parse(input, Sequence.class, Part.B).sequence();
		Map<Pair<Long, Long>, Long> memory = new HashMap<>();

		long val = transform(sequence, 75, memory);
		System.out.println(val);

	}

	private long transform(List<Long> stones, int rounds, Map<Pair<Long, Long>, Long> memory) {
		long sum = 0;
		for (var stone : stones) {
			sum += transform(stone, rounds, memory);
		}
		return sum;
	}

	private long transform(long stone, long rounds, Map<Pair<Long, Long>, Long> memory) {

		Long count = memory.get(Pair.of(stone, rounds));
		if (count != null) {
			return count;
		}

		if (rounds == 0) {
			return 1;
		}

		if (stone == 0) {
			long res = transform(1, rounds - 1, memory);
			memory.put(Pair.of(stone, rounds), res);
		} else if (String.valueOf(stone).length() % 2 == 0) {
			String stoneStr = String.valueOf(stone);
			int half = stoneStr.length() / 2;
			long r1 = transform(newStone(stoneStr, 0, half), rounds - 1, memory);
			long r2 = transform(newStone(stoneStr, half, stoneStr.length()), rounds - 1, memory);
			memory.put(Pair.of(stone, rounds), r1 + r2);

		} else {
			long res = transform(stone * 2024, rounds - 1, memory);
			memory.put(Pair.of(stone, rounds), res);
		}

		return memory.get(Pair.of(stone, rounds));
	}

	private long newStone(String stone, int from, int to) {
		return Parsing.stol(stone.subSequence(from, to).toString());
	}

	@Override
	public Object parseA(String input) {
		return new Sequence(utils.Sequence.sequence(input, "\\s+", Parsing::stol));
	}

	@Override
	public Object parseB(String input) {
		return parseA(input);
	}

	private record Sequence(List<Long> sequence) {
	};
}
