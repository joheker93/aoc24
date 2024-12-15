package days.day13;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import day.Day;
import day.Part;
import utils.Pair;
import utils.Parsing;

public class Day13 implements Day {

	@Override
	public void solveA(String input) {
		List<Equation> equations = parse(input, Equations.class, Part.A).equations();

		long sum = solve(equations);
		System.out.println(sum);
	}

	private long solve(List<Equation> equations) {
		long aCost = 3;
		long bCost = 1;

		long sum = 0;
		for (var equation : equations) {
			Pair<Long, Long> ab = solve(equation);
			if (testSolution(ab.fst(), ab.snd(), equation)) {
				long tokens = ab.fst() * aCost + ab.snd() * bCost;
				sum += tokens;
			}
		}

		return sum;
	}

	@Override
	public void solveB(String input) {
		List<Equation> equations = parse(input, Equations.class, Part.B).equations();

		long megaNumber = 10000000000000L;
		equations = equations.stream()
				.map(e -> new Equation(e.ax(), e.ay(), e.bx(), e.by(), megaNumber + e.xp(), megaNumber + e.yp()))
				.toList();

		long sum = solve(equations);
		System.out.println(sum);

	}

	// <a,b>
	private Pair<Long, Long> solve(Equation eq) {
		long b = (eq.ax() * eq.yp() - eq.xp() * eq.ay()) / (eq.ax() * eq.by() - eq.ay() * eq.bx());
		long a = (eq.xp() - eq.bx() * b) / eq.ax;
		return Pair.of(a, b);
	}

	private boolean testSolution(long a, long b, Equation eq) {
		return eq.ax() * a + eq.bx() * b == eq.xp() && eq.ay() * a + eq.by() * b == eq.yp();
	}

	@Override
	public Object parseA(String input) {
		String groupAStr = "Button A: X\\+(?<ax>\\d+), Y\\+(?<ay>\\d++)";
		String groupBStr = "Button B: X\\+(?<bx>\\d+), Y\\+(?<by>\\d++)";
		String priceStr = "Prize: X\\=(?<xp>\\d+), Y\\=(?<yp>\\d++)";

		Pattern pattern = Pattern.compile(groupAStr + "\r\n" + groupBStr + "\r\n" + priceStr);

		Matcher matcher = pattern.matcher(input);

		List<Equation> equations = new ArrayList<>();
		while (matcher.find()) {
			String ax = matcher.group("ax");
			String ay = matcher.group("ay");

			String bx = matcher.group("bx");
			String by = matcher.group("by");

			String xp = matcher.group("xp");
			String yp = matcher.group("yp");

			equations.add(new Equation(Parsing.stol(ax), Parsing.stol(ay), Parsing.stol(bx), Parsing.stol(by),
					Parsing.stol(xp), Parsing.stol(yp)));

		}

		return new Equations(equations);
	}

	@Override
	public Object parseB(String input) {
		return parseA(input);
	}

	private record Equations(List<Equation> equations) {
	};

	private record Equation(long ax, long ay, long bx, long by, long xp, long yp) {
	};
}
