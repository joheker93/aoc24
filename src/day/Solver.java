package day;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import days.day01.Day1;
import days.day02.Day2;
import days.day03.Day3;
import days.day04.Day4;
import days.day05.Day5;
import days.day06.Day6;
import days.day07.Day7;
import days.day08.Day8;
import days.day09.Day9;
import days.day10.Day10;
import days.day11.Day11;
import days.day12.Day12;
import days.day13.Day13;
import days.day14.Day14;
import days.day15.Day15;
import days.day16.Day16;
import days.day17.Day17;
import days.day18.Day18;
import days.day19.Day19;
import days.day20.Day20;

public class Solver {

	public static void main(String[] args) {
		Solver solver = new Solver();

		Mode mode = Mode.LIVE;

		Map<String, Long> runningTimes = new HashMap<>();
		String fileEnding = mode == Mode.TEST ? ".test" : ".in";
		long startTime = System.currentTimeMillis();
		for (Day day : solver.getDays()) {
			String path = "src/" + day.getClass().getCanonicalName().toLowerCase().replace(".", "/") + fileEnding;

			try {
				String dayName = day.getClass().getSimpleName();
				String input = Files.readString(new File(path).toPath());
				if(input.isEmpty()){
					continue;
				}

				// Pretty header
				printHeader(dayName);

				long before = System.currentTimeMillis();
				System.out.println("\u001B[36m>> PART A\u001B[0m");
				System.out.println("--------------------");
				day.solveA(input);
				long mid = System.currentTimeMillis();

				System.out.println("\n\u001B[36m>> PART B\u001B[0m");
				System.out.println("--------------------");
				day.solveB(input);
				long after = System.currentTimeMillis();

				runningTimes.put(day.getClass().getSimpleName(), after - before);
				// Execution time and footer
				System.out.println("\n\u001B[32m✔ Solved part A in " + (mid - before) + " ms\u001B[0m");
				System.out.println("\u001B[32m✔ Solved part B in " + (after - mid) + " ms\u001B[0m");
				printFooter();
			} catch (IOException e) {
				System.out.println("\u001B[31m✘ Could not solve " + day.getClass().getSimpleName()
						+ " (Input file not found)\u001B[0m");
			}
		}

		long endTime = System.currentTimeMillis();
		System.out.println("\n\u001B[32m✔ Solved all problems in " + (endTime - startTime) + " ms\u001B[0m");

		String slowestDay = Collections.max(runningTimes.entrySet(), Map.Entry.comparingByValue()).getKey();
		System.out.println("\u001B[31m✘ Slowest solution ( " + slowestDay + " ) ran for " + runningTimes.get(slowestDay)
				+ " ms\u001B[0m");
	}

	private static void printHeader(String dayName) {
		String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		System.out.println("\n\u001B[35m==============================\u001B[0m");
		System.out.println("\u001B[35m Solving Day: " + dayName + " \u001B[0m");
		System.out.println("\u001B[35m Time: " + timestamp + " \u001B[0m");
		System.out.println("\u001B[35m==============================\u001B[0m");
	}

	private static void printFooter() {
		System.out.println("\n\u001B[34m==============================\u001B[0m");
		System.out.println("\u001B[34m End of Day Solution \u001B[0m");
		System.out.println("\u001B[34m==============================\u001B[0m");
	}

	private Day[] getDays() {
		return new Day[] { new Day1(), new Day2(), new Day3(), new Day4(), new Day5(), new Day6(), new Day7(),
				new Day8(), new Day9(), new Day10(), new Day11(), new Day12(), new Day13(), new Day14(), new Day15(),
				new Day16(), new Day17(), new Day18(), new Day19(), new Day20() };
	}

	private Day[] getDay(int day) {
		return new Day[] { getDays()[day - 1] };
	}

	private enum Mode {
		TEST, LIVE;
	}
}
