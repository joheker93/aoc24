package utils.graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.function.BiFunction;

import utils.Grid;
import utils.enums.Direction;

public class Graph<T> {
	List<Node<T>> _nodes;
	List<Edge<T>> _edges;
	Map<Node<T>, List<Node<T>>> _nodeAdjecencies;
	Map<Node<T>, List<Edge<T>>> _nodeEdges;

	public Graph(List<Node<T>> nodes, List<Edge<T>> edges) {
		_nodes = nodes;
		_edges = edges;

		_nodeAdjecencies = new HashMap<Node<T>, List<Node<T>>>();
		_nodeEdges = new HashMap<Node<T>, List<Edge<T>>>();

		// Initialize adjacency maps
		for (Node<T> node : nodes) {
			_nodeAdjecencies.put(node, new ArrayList<>());
			_nodeEdges.put(node, new ArrayList<>());
		}

		for (Edge<T> edge : edges) {
			Node<T> source = edge.getSource();
			Node<T> target = edge.getTarget();

			_nodeAdjecencies.get(source).add(target);
			_nodeEdges.get(source).add(edge);
		}

	}

	public Graph() {
		_nodes = new ArrayList<Node<T>>();
		_edges = new ArrayList<Edge<T>>();
	}

	void addNode(String name) {
		_nodes.add(new Node<T>(name, null));
	}

	public void addEdge(Node<T> source, Node<T> target, long weight) {
		if (!hasNode(source)) {
			System.out.println("Missing source node " + source);
			return;
		}

		if (!hasNode(target)) {
			System.out.println("Missing target node " + target);
			return;
		}

		_edges.add(new Edge<T>(source, target, weight));
	}

	public void addEdge(Node<T> source, Node<T> target) {
		addEdge(source, target, 1);
	}

	public boolean hasEdge(Edge<T> e) {
		return _edges.contains(e);
	}

	public boolean hasEdge(Node<T> source, Node<T> target) {
		return _edges.contains(new Edge<T>(source, target, 0));
	}

	public boolean hasNode(Node<T> node) {
		return _nodes.contains(node);
	}

	public boolean hasNode(String name, T value) {
		return _nodes.contains(new Node<T>(name, value));
	}

	public List<Edge<T>> getEdgesFrom(Node<T> node) {
		return _nodeEdges.get(node);
	}

	public List<Edge<T>> getEdgesTo(Node<T> node) {
		List<Edge<T>> edges = new ArrayList<>();
		for (Edge<T> edge : _edges) {
			if (edge.hasTarget(node)) {
				edges.add(edge);
			}
		}

		return edges;
	}

	public List<Node<T>> adjecencies(Node<T> node) {
		return _nodeAdjecencies.get(node);
	}

	public void dfs(Node<T> start, BiFunction<Node<T>, Node<T>, Boolean> visitor) {

		Set<Node<T>> visited = new HashSet<>();
		Stack<Node<T>> stack = new Stack<>();

		stack.push(start);
		while (!stack.isEmpty()) {
			Node<T> current = stack.pop();

			if (visited.contains(current)) {
				continue;
			}

			visited.add(current);

			List<Node<T>> neighbours = adjecencies(current);
			for (Node<T> neighbour : neighbours) {

				if (!visitor.apply(current, neighbour)) {
					return;
				}

				if (!visited.contains(neighbour)) {
					stack.push(neighbour);
				}
			}
		}
	}

	public void bfs(Node<T> start, BiFunction<Node<T>, Node<T>, Boolean> visitor) {
		Set<Node<T>> visited = new HashSet<>();
		Queue<Node<T>> queue = new LinkedList<>();
		queue.add(start);

		while (!queue.isEmpty()) {
			Node<T> current = queue.poll();
			if (visited.contains(current)) {
				continue;
			}
			visited.add(current);

			for (Node<T> neighbor : adjecencies(current)) {
				if (!visitor.apply(current, neighbor)) {
					return;
				}
				if (!visited.contains(neighbor)) {
					queue.add(neighbor);
				}
			}
		}
	}

	public Map<Node<T>, Long> shortestPath(Node<T> start) {
		if (start == null) {
			start = _nodes.get(0);
		}

		Map<Node<T>, Long> distances = new HashMap<>();
		PriorityQueue<NodeDistance> pq = new PriorityQueue<>(Comparator.comparingLong(nd -> nd.distance));

		for (Node<T> node : _nodes) {
			distances.put(node, Long.MAX_VALUE);
		}

		distances.put(start, 0L);
		pq.add(new NodeDistance(start, 0L));

		while (!pq.isEmpty()) {
			NodeDistance current = pq.poll();
			Node<T> currentNode = current.node;

			if (distances.get(currentNode) < current.distance) {
				continue;
			}

			long currentDistance = distances.get(currentNode);

			for (Edge<T> edge : _nodeEdges.getOrDefault(currentNode, new ArrayList<>())) {
				Node<T> neighbor = edge.getTarget();
				long newDistance = currentDistance + edge.getWeight();

				if (newDistance < distances.get(neighbor)) {
					distances.put(neighbor, newDistance);
					pq.add(new NodeDistance(neighbor, newDistance));
				}
			}
		}

		return distances;
	}

	public static <T> Graph<T> fromGrid(Grid<T> g, Direction dir) {
		var now = System.currentTimeMillis();
		List<Edge<T>> _edges = new ArrayList<Edge<T>>();
		List<Node<T>> _nodes = new ArrayList<Node<T>>();

		g.traverse((point, gridElement) -> {
			Node<T> source = new Node<>(gridElement.toString());
			_nodes.add(source);

			for (var neighbour : g.neighbours(point, dir)) {
				Node<T> target = new Node<>(neighbour.toString());
				Edge<T> edge = new Edge<>(source, target);
				_nodes.add(target);
				_edges.add(edge);
			}
		});

		System.out.println(System.currentTimeMillis() - now);
		return new Graph<>(_nodes, _edges);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		_edges.forEach(e -> {
			sb.append(e + "\n");
		});
		return sb.toString();
	}

	private class NodeDistance {
		Node<T> node;
		long distance;

		NodeDistance(Node<T> node, long distance) {
			this.node = node;
			this.distance = distance;
		}
	}
}
