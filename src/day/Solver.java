package day;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

import days.day1.Day1;

public class Solver {

	public static void main(String[] args) {
		Solver solver = new Solver();

		for (Day day : solver.getDay(1)) {
			String path = "src/main/java/" + day.getClass().getCanonicalName().toLowerCase().replace(".", "/") + ".in";
			try {
				String dayName = day.getClass().getSimpleName();
				String input = Files.readString(new File(path).toPath());

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

				// Execution time and footer
				System.out.println("\n\u001B[32m✔ Solved part A in " + (mid - before) + " ms\u001B[0m");
				System.out.println("\u001B[32m✔ Solved part B in " + (after - mid) + " ms\u001B[0m");
				printFooter();
			} catch (IOException e) {
				System.out.println("\u001B[31m✘ Could not solve " + day.getClass().getSimpleName()
						+ " (Input file not found)\u001B[0m");
			}
		}
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
		return new Day[] { new Day1(), new Day1() };
	}

	private Day[] getDay(int day) {
		return new Day[] { getDays()[day - 1] };
	}
}
