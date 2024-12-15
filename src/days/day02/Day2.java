package days.day02;

import java.util.ArrayList;
import java.util.List;

import day.Day;
import day.Part;
import utils.Grid;
import utils.Parsing;

public class Day2 implements Day {

	@Override
	public void solveA(String input) {
		List<List<Integer>> reports = parse(input, Reports.class, Part.A).reports();

		int safeReports = 0;
		for (var report : reports) {
			if (isSafe(report)) {
				safeReports++;
			}
		}

		System.out.println(safeReports);

	}

	@Override
	public void solveB(String input) {
		List<List<Integer>> reports = parse(input, Reports.class, Part.B).reports();

		int safeReports = 0;
		for (var report : reports) {
			if (isSafeWithDampener(report)) {
				safeReports++;
			}
		}

		System.out.println(safeReports);
	}

	private boolean isSafe(List<Integer> report) {
		int startValue = report.get(0);
		boolean increasing = report.get(1) > startValue;

		for (int i = 1; i < report.size(); i++) {
			int left = report.get(i - 1);
			int right = report.get(i);

			if (increasing && !strictDifference(left, right, 1, 3)) {
				return false;
			}

			if (!increasing && !strictDifference(right, left, 1, 3)) {
				return false;
			}

		}

		return true;
	}

	// just remove one index at a time and try each sublist
	private boolean isSafeWithDampener(List<Integer> report) {

		if (isSafe(report)) {
			return true;
		}

		for (int i = 0; i < report.size(); i++) {
			List<Integer> subList = new ArrayList<>(report);

			subList.remove(i);
			
			if (isSafe(subList)) {
				return true;
			}
		}

		return false;
	}

	private boolean strictDifference(int a, int b, int minDiff, int maxDiff) {
		int diff = b - a;
		return diff >= minDiff && diff <= maxDiff;
	}

	@Override
	public Object parseA(String input) {
		Grid<Integer> grid = Parsing.asGrid(input, Parsing::stoi);

		return new Reports(grid.get());
	}

	@Override
	public Object parseB(String input) {
		return parseA(input);
	}

	private record Reports(List<List<Integer>> reports) {
	};

}
