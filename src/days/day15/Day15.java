package days.day15;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import day.Day;
import day.Part;
import utils.Grid;
import utils.Parsing;
import utils.Point;

public class Day15 implements Day {

	@Override
	public void solveA(String input) {
		Warehouse warehouse = parse(input, Warehouse.class, Part.A);

		runAmok(warehouse);

		AtomicLong sum = new AtomicLong();
		warehouse._map.traverse((pos, val) -> {
			if (warehouse.hasBox(pos)) {
				sum.addAndGet(100 * pos.getY() + pos.getX());
			}
		});

		System.out.println(sum);
	}

	@Override
	public void solveB(String input) {

		Warehouse warehouse = parse(input, Warehouse.class, Part.B);

		runAmok(warehouse);

		warehouse.repaintMap();

		AtomicLong sum = new AtomicLong();
		warehouse._map.traverse((pos, val) -> {
			if (val.equals("[")) {
				sum.addAndGet(100 * pos.getY() + pos.getX());
			}
		});

		System.out.println(sum);
	}

	private void runAmok(Warehouse warehouse) {
		Queue<Character> moves = warehouse._moves;
		while (!moves.isEmpty()) {
			if (moves.size() == 1) {
			}
			Point current = warehouse._robotPosition;
			char move = moves.poll();
			if (move == '<') {
				move(Direction.LEFT, current, warehouse);
			} else if (move == '>') {
				move(Direction.RIGHT, current, warehouse);
			} else if (move == 'v') {
				move(Direction.DOWN, current, warehouse);
			} else if (move == '^') {
				move(Direction.UP, current, warehouse);
			}

		}

	}

	private void move(Direction dir, Point robotPos, Warehouse warehouse) {
		if (warehouse.hasBox(nextPoint(dir, robotPos))) {
			boolean moved = warehouse.tryMoveBox(warehouse.getBox(nextPoint(dir, robotPos)), dir, warehouse);
			if (moved) {
				warehouse._robotPosition = nextPoint(dir, robotPos);
			}
			return;
		}

		if (!warehouse.hasWall(nextPoint(dir, robotPos))) {
			warehouse._robotPosition = nextPoint(dir, robotPos);
			return;
		}

	}

	private Point nextPoint(Direction dir, Point current) {
		if (dir == Direction.LEFT) {
			return new Point(current.getX() - 1, current.getY());
		}
		if (dir == Direction.RIGHT) {
			return new Point(current.getX() + 1, current.getY());
		}
		if (dir == Direction.UP) {
			return new Point(current.getX(), current.getY() - 1);
		}
		if (dir == Direction.DOWN) {
			return new Point(current.getX(), current.getY() + 1);
		}

		return null;
	}

	@Override
	public Object parseA(String input) {
		String[] strs = input.split("\n\n");
		String mapInput = strs[0];
		String movesInput = strs[1];

		Grid<String> grid = Parsing.asGrid(mapInput, "", s -> s);
		Queue<Character> moves = new LinkedList<Character>();
		for (char c : movesInput.toCharArray()) {
			moves.offer(c);
		}

		AtomicReference<Point> robot = new AtomicReference<>();
		List<Box> boxes = new ArrayList<>();
		List<Point> walls = new ArrayList<>();
		grid.traverse((pos, val) -> {
			if (val.equals("@")) {
				robot.set(pos);
			}

			if (val.equals("O")) {
				boxes.add(new Box(List.of(pos)));
			}

			if (val.equals("#")) {
				walls.add(pos);
			}
		});

		return new Warehouse(moves, grid, boxes, walls, robot.get());
	}

	@Override
	public Object parseB(String input) {
		Warehouse warehouseA = parse(input, Warehouse.class, Part.A);

		Grid<String> newGrid = reArrangeGrid(warehouseA._map);

		AtomicReference<Point> robot = new AtomicReference<>();
		List<Box> boxes = new ArrayList<>();
		List<Point> walls = new ArrayList<>();
		newGrid.traverse((pos, val) -> {
			if (val.equals("@")) {
				robot.set(pos);
			}

			if (val.equals("[")) {
				boxes.add(new Box(List.of(pos, new Point(pos.getX() + 1, pos.getY()))));
			}

			if (val.equals("#")) {
				walls.add(pos);
			}
		});

		return new Warehouse(warehouseA._moves, newGrid, boxes, walls, robot.get());
	}

	private Grid<String> reArrangeGrid(Grid<String> map) {
		List<List<String>> gridList = new ArrayList<>();
		for (var row : map.get()) {
			List<String> rowList = new ArrayList<>();
			for (var val : row) {
				if (val.equals(".")) {
					rowList.add(".");
					rowList.add(".");
				} else if (val.equals("#")) {
					rowList.add("#");
					rowList.add("#");
				} else if (val.equals("O")) {
					rowList.add("[");
					rowList.add("]");
				} else {
					rowList.add(val);
					rowList.add(".");
				}

			}
			gridList.add(rowList);
		}

		return new Grid<>(gridList);
	}

	private class Warehouse {
		private final Queue<Character> _moves;
		private final Set<Point> _walls;
		private final Grid<String> _map;
		private final Map<Point, Box> _boxMap = new HashMap<>();

		private Point _robotPosition;

		private Warehouse(Queue<Character> moves, Grid<String> map, List<Box> boxes, List<Point> walls,
				Point robotPosition) {
			_moves = moves;
			_robotPosition = robotPosition;
			_walls = new HashSet<>(walls);
			_map = map;

			for (var box : boxes) {
				box._coordinates.forEach(c -> _boxMap.put(c, box));
			}
		}

		private boolean hasWall(Point p) {
			return _walls.contains(p);
		}

		private boolean hasBox(Point p) {
			return _boxMap.containsKey(p);
		}

		private Box getBox(Point p) {
			return _boxMap.get(p);
		}

		private boolean tryMoveBox(Box box, Direction dir, Warehouse warehouse) {
			if (canMoveBox(box, dir, warehouse)) {
				return moveBox(box, dir, warehouse);
			}
			return false;
		}

		private boolean moveBox(Box box, Direction dir, Warehouse warehouse) {
			for (Point p : box._coordinates) {
				Point nextPoint = nextPoint(dir, p);

				if (warehouse.hasWall(nextPoint)) {
					return false;
				}

				if (warehouse.hasBox(nextPoint)) {
					Box nextBox = warehouse.getBox(nextPoint);
					if (nextBox != box) {

						if (!canMoveBox(nextBox, dir, warehouse)) {
							return false;
						}

						if (!moveBox(nextBox, dir, warehouse)) {
							return false;
						}
					}
				}
			}

			warehouse.moveBox(box._coordinates.get(0), dir);
			return true;
		}

		private boolean canMoveBox(Box box, Direction dir, Warehouse warehouse) {
			for (Point p : box._coordinates) {
				Point nextPoint = nextPoint(dir, p);

				if (warehouse.hasWall(nextPoint)) {
					return false;
				}

				if (warehouse.hasBox(nextPoint)) {
					Box nextBox = warehouse.getBox(nextPoint);
					if (nextBox != box) {

						if (!canMoveBox(nextBox, dir, warehouse)) {
							return false;
						}
					}
				}
			}

			return true;
		}

		private void moveBox(Point from, Direction d) {
			List<Box> deleteCandidates = new ArrayList<>();
			List<Box> newBoxes = new ArrayList<>();

			for (var box : _boxMap.values()) {
				if (box.contains(from)) {
					deleteCandidates.add(box);

					List<Point> newCoords = new ArrayList<>();
					for (var coord : box._coordinates) {
						if (d == Direction.UP) {
							newCoords.add(new Point(coord.getX(), coord.getY() - 1));
						}

						if (d == Direction.DOWN) {
							newCoords.add(new Point(coord.getX(), coord.getY() + 1));
						}

						if (d == Direction.LEFT) {
							newCoords.add(new Point(coord.getX() - 1, coord.getY()));
						}

						if (d == Direction.RIGHT) {
							newCoords.add(new Point(coord.getX() + 1, coord.getY()));
						}
					}

					newBoxes.add(new Box(newCoords));

				}
			}

			deleteCandidates.forEach(cand -> {
				cand._coordinates.forEach(_boxMap::remove);

			});
			newBoxes.forEach(newB -> {
				newB._coordinates.forEach(c -> _boxMap.put(c, newB));
			});
		}

		private void repaintMap() {
			_map.traverse((pos, val) -> {
				if (hasBox(pos)) {
					_map.setValue(getBox(pos)._coordinates.get(0), "[");
					_map.setValue(getBox(pos)._coordinates.get(1), "]");
				} else if (!val.equals("#")) {
					_map.setValue(pos, ".");
				}
			});

			_map.setValue(_robotPosition, "@");
		}
	}

	private class Box {
		private final List<Point> _coordinates;

		private Box(List<Point> points) {
			_coordinates = new ArrayList<>(points);
		}

		private boolean contains(Point p) {
			return _coordinates.contains(p);
		}

		@Override
		public String toString() {
			return _coordinates.toString();
		}
	}

	enum Direction {
		LEFT, UP, RIGHT, DOWN
	};

}