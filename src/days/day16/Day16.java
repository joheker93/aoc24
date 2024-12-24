package days.day16;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;

import day.Day;
import day.Part;
import utils.Grid;
import utils.Pair;
import utils.Parsing;
import utils.Point;
import utils.enums.Direction;

public class Day16 implements Day {

	@Override
	public void solveA(String input) {
		Grid<String> maze = parse(input, Maze.class, Part.A).maze();
		solve(maze);

	}

	private void solve(Grid<String> maze) {
		Point start = findTarget(maze, "S");
		Point end = findTarget(maze, "E");

		List<Node> bestPaths = findTopPaths(start, end, maze);
		Set<Point> seats = new HashSet<>();
		
		bestPaths.forEach(node -> {
			List<Point> points = backTrack(node);
			points.forEach(seats::add);

		});

		System.out.println(seats.size());
	}

	private List<Point> backTrack(Node node) {
		List<Point> l = new ArrayList<>();
		if (node._parent == null) {
			l.add(node._point);
			return l;
		}

		l.add(node._point);
		l.addAll(backTrack(node._parent));
		return l;
	}

	private List<Node> findTopPaths(Point start, Point target, Grid<String> maze) {
		PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(node -> node._cost));
		queue.add(new Node(start, 0, Dir.R, null));

		List<Node> successfulPaths = new ArrayList<>();
		Set<Pair<Dir, Point>> visited = new HashSet<>();

		int leastCost = Integer.MAX_VALUE;
		while (!queue.isEmpty()) {
			Node current = queue.poll();

			if (current._point.equals(target)) {

				if (current._cost <= leastCost) {
					leastCost = current._cost;
					successfulPaths.add(current);
				} else {
					break;
				}

				continue;
			}

			visited.add(Pair.of(current._dir, current._point));

			for (var neighbour : maze.neighbourIndexes(current._point, Direction.NON_DIAGONAL)) {
				if (maze.at(neighbour).equals("#")) {
					continue;
				}

				Dir direction = getDirection(current._point, neighbour);
				int newCost = direction != current._dir ? current._cost + 1001 : current._cost + 1;

				if (!visited.contains(Pair.of(direction, neighbour))) {
					queue.add(new Node(neighbour, newCost, direction, current));
				}
			}
		}

		return successfulPaths;
	}

	private Dir getDirection(Point from, Point to) {
		if (from.getX() == to.getX()) {
			return (to.getY() > from.getY()) ? Dir.D : Dir.U;
		} else {
			return (to.getX() > from.getX()) ? Dir.R : Dir.L;
		}
	}

	private enum Dir {
		U, L, D, R
	};

	private Point findTarget(Grid<String> maze, String target) {
		Point p = new Point(-1, -1);
		maze.traverse((pos, val) -> {
			if (val.equals(target)) {
				p.set(pos);
			}
		});

		return p;
	}

	@Override
	public void solveB(String input) {

	}

	@Override
	public Object parseA(String input) {
		return new Maze(Parsing.asGrid(input, "", s -> s));
	}

	@Override
	public Object parseB(String input) {
		return null;
	}

	private record Maze(Grid<String> maze) {
	};

	private class Node {
		Point _point;
		int _cost;
		Dir _dir;
		Node _parent;

		public Node(Point point, int cost, Dir dir, Node parent) {
			_point = point;
			_cost = cost;
			_dir = dir;
			_parent = parent;
		}

		@Override
		public String toString() {
			return "Node [_point=" + _point + ", _cost=" + _cost + ", _dir=" + _dir + ", _parent=" + _parent + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + Objects.hash(_cost, _dir, _parent, _point);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			Node other = (Node) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance())) {
				return false;
			}
			return _cost == other._cost && _dir == other._dir && Objects.equals(_parent, other._parent)
					&& Objects.equals(_point, other._point);
		}

		private Day16 getEnclosingInstance() {
			return Day16.this;
		}

	}
}