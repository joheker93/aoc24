package days.day09;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import day.Day;
import day.Part;
import utils.Pair;
import utils.Parsing;
import utils.Sequence;

public class Day9 implements Day {

	@Override
	public void solveA(String input) {
		List<Integer> diskMap = parse(input, Disk.class, Part.A).diskMap();

		List<Integer> expandedDisk = expandDisk(diskMap);
		List<Integer> compactDisk = compactDisk(expandedDisk);
		long checkSum = checkSum(compactDisk);
		System.out.println(checkSum);
	}

	@Override
	public void solveB(String input) {
		List<Integer> diskMap = parse(input, Disk.class, Part.A).diskMap();
		List<Integer> expandedDisk = expandDisk(diskMap);
		AtomicInteger i = new AtomicInteger(0);
		List<Integer> chunkedDisk = chunkDisk(expandedDisk,
				diskMap.stream().filter(x -> i.getAndIncrement() % 2 == 0).toList());

		long checksum = checkSum(chunkedDisk);
		System.out.println(checksum);
	}

	private List<Integer> chunkDisk(List<Integer> expandedDisk, List<Integer> usedDisk) {
		List<Integer> chunkedDisk = new ArrayList<>(expandedDisk);
		List<Pair<Integer, Integer>> freeIntervals = getFreeIntervals(chunkedDisk);

		Map<Integer, Integer> idStartIndices = new HashMap<>();
		for (int i = 0; i < chunkedDisk.size(); i++) {
			int value = chunkedDisk.get(i);

			if (value >= 0 && !idStartIndices.containsKey(value)) {
				idStartIndices.put(value, i);
			}
		}

		for (int id = usedDisk.size() - 1; id >= 0; id--) {
			int chunkSize = usedDisk.get(id);
			int startIndexOfId = idStartIndices.getOrDefault(id, -1);

			if (startIndexOfId == -1) {
				continue;
			}

			Iterator<Pair<Integer, Integer>> it = freeIntervals.iterator();
			while (it.hasNext()) {
				Pair<Integer, Integer> interval = it.next();
				int intervalSize = interval.snd() - interval.fst();

				if (intervalSize >= chunkSize && startIndexOfId > interval.fst()) {
					for (int i = 0; i < chunkSize; i++) {
						chunkedDisk.set(interval.fst() + i, id);
						chunkedDisk.set(startIndexOfId++, -1);
					}

					if (intervalSize == chunkSize) {
						it.remove();
					} else {
						freeIntervals.set(freeIntervals.indexOf(interval),
								Pair.of(interval.fst() + chunkSize, interval.snd()));
					}
					break;
				}
			}
		}

		return chunkedDisk;
	}

	private List<Pair<Integer, Integer>> getFreeIntervals(List<Integer> disk) {
		List<Pair<Integer, Integer>> intervals = new ArrayList<>();
		int start = -1;

		for (int i = 0; i < disk.size(); i++) {
			if (disk.get(i) == -1) {
				if (start == -1) {
					start = i;
				}
			} else {
				if (start != -1) {
					intervals.add(Pair.of(start, i));
					start = -1;
				}
			}
		}

		if (start != -1) {
			intervals.add(Pair.of(start, disk.size()));
		}

		return intervals;
	}

	private long checkSum(List<Integer> compactDisk) {
		long sum = 0;
		for (int i = 0; i < compactDisk.size(); i++) {
			int segment = compactDisk.get(i);
			if (segment == -1) {
				continue;
			}
			sum += segment * i;
		}
		return sum;
	}

	private List<Integer> compactDisk(List<Integer> expandedDisk) {
		Deque<Integer> segments = new ArrayDeque<>(expandedDisk);
		List<Integer> compactDisk = new ArrayList<>();

		while (segments.contains(-1)) {
			Integer segment = segments.pop();
			if (segment == -1) {
				int val = segments.pollLast();

				if (val == -1) {
					segments.addFirst(segment);
					continue;
				}

				compactDisk.add(val);
			} else {
				compactDisk.add(segment);
			}
		}

		compactDisk.addAll(segments);

		return compactDisk;
	}

	private List<Integer> expandDisk(List<Integer> cd) {
		Deque<Integer> segments = new ArrayDeque<>(cd);

		int id = 0;
		List<Integer> newDisk = new ArrayList<>();

		boolean isFile = true;
		int index = 0;
		while (!segments.isEmpty()) {
			if (isFile) {
				int file = segments.pop();

				for (int i = 0; i < file; i++) {
					newDisk.add(index++, id);
				}

				id++;
				isFile = false;

			} else {
				int space = segments.pop();
				for (int i = 0; i < space; i++) {
					newDisk.add(index++, -1);
				}

				isFile = true;
			}
		}

		return newDisk;
	}

	@Override
	public Object parseA(String input) {
		return new Disk(Sequence.sequence(input, "", Parsing::stoi));
	}

	@Override
	public Object parseB(String input) {
		return parseA(input);
	}

	private record Disk(List<Integer> diskMap) {
	};
}
