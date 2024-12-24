package days.day14;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import day.Day;
import day.Part;
import utils.Parsing;
import utils.Point;

public class Day14 implements Day {

	private final static int WIDTH = 101;
	private final static int HEIGHT = 103;

	@Override
	public void solveA(String input) {
		List<RobotState> robotStates = parse(input, RobotSystem.class, Part.A).robotStates();
		Map<Quadrant, Integer> quadCounts = new HashMap<>();
		for (int i = 0; i < robotStates.size(); i++) {
			RobotState robot = walk(robotStates.get(i), 100);

			Quadrant quadrant = getQuadrant(robot);
			quadCounts.merge(quadrant, 1, Integer::sum);
			robotStates.set(i, robot);
		}

		int result = 1;
		for (Entry<Quadrant, Integer> val : quadCounts.entrySet()) {
			if (val.getKey() == Quadrant.None) {
				continue;
			}

			result *= val.getValue();
		}
		System.out.println(result);
	}

	private Quadrant getQuadrant(RobotState robot) {
		int halfWidth = WIDTH / 2;
		int halfHeight = HEIGHT / 2;
		if (robot.x() < halfWidth) {
			if (robot.y() < halfHeight) {
				return Quadrant.Q1;
			}

			if (robot.y() > halfHeight) {
				return Quadrant.Q2;
			}
		}

		if (robot.x() > halfWidth) {
			if (robot.y() < halfHeight) {
				return Quadrant.Q3;
			}

			if (robot.y() > halfHeight) {
				return Quadrant.Q4;
			}
		}

		return Quadrant.None;
	}

	private RobotState walk(RobotState robot, int steps) {

		int currentX = robot.x();
		int currentY = robot.y();
		for (int i = 0; i < steps; i++) {
			currentX = Math.floorMod(currentX + robot.dx(), WIDTH);
			currentY = Math.floorMod(currentY + robot.dy(), HEIGHT);
		}

		return new RobotState(robot.id(), currentX, currentY, robot.dx(), robot.dy());
	}

	@Override
	public void solveB(String input) {
		List<RobotState> robotStates = parse(input, RobotSystem.class, Part.A).robotStates();
		int res = 0;
		boolean unique = false;
		int i = 0;
		while (!unique) {
			unique = true;

			Set<Point> duplicate = new HashSet<>();
			for (int j = 0; j < robotStates.size(); j++) {
				RobotState robotState = walk(robotStates.get(j), 1);
				robotStates.set(j, robotState);
				boolean success = duplicate.add(new Point(robotState.x(), robotState.y()));
				if (!success) {
					unique = false;
				}
			}

			if (unique) {
				res = i + 1;
				break;
			}
			i++;
		}

		System.out.println(res);
	}

	@Override
	public Object parseA(String input) {
		Pattern p = Pattern.compile("p\\=(?<x>\\d+),(?<y>\\d+) v\\=(?<dx>\\d+|\\-\\d+),(?<dy>\\d+|\\-\\d+)");
		Matcher m = p.matcher(input);

		List<RobotState> robotStates = new ArrayList<>();
		int id = 0;
		while (m.find()) {
			int x = Parsing.stoi(m.group("x"));
			int y = Parsing.stoi(m.group("y"));
			int dx = Parsing.stoi(m.group("dx"));
			int dy = Parsing.stoi(m.group("dy"));

			robotStates.add(new RobotState(id++, x, y, dx, dy));
		}

		return new RobotSystem(robotStates);

	}

	@Override
	public Object parseB(String input) {
		return parseA(input);
	}

	private record RobotSystem(List<RobotState> robotStates) {
	};

	private record RobotState(int id, int x, int y, int dx, int dy) {
	};

	private enum Quadrant {
		Q1, Q2, Q3, Q4, None
	}
}
