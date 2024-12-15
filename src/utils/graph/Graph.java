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
import java.util.function.Function;

import utils.Grid;
import utils.Point;
import utils.enums.Direction;

public class Graph<K, V> {
	List<Node<K, V>> _nodes;
	List<Edge<K, V>> _edges;
	Map<Node<K, V>, List<Node<K, V>>> _nodeAdjecencies;
	Map<Node<K, V>, List<Edge<K, V>>> _nodeEdges;

	public Graph(List<Node<K, V>> nodes, List<Edge<K, V>> edges) {
		_nodes = nodes;
		_edges = edges;

		_nodeAdjecencies = new HashMap<Node<K, V>, List<Node<K, V>>>();
		_nodeEdges = new HashMap<Node<K, V>, List<Edge<K, V>>>();
		// Initialize adjacency maps
		for (Node<K, V> node : nodes) {
			_nodeAdjecencies.put(node, new ArrayList<>());
			_nodeEdges.put(node, new ArrayList<>());
		}

		for (Edge<K, V> edge : edges) {
			Node<K, V> source = edge.getSource();
			Node<K, V> target = edge.getTarget();

			_nodeAdjecencies.get(source).add(target);
			_nodeEdges.get(source).add(edge);
		}
	}

	public Graph() {
		_nodes = new ArrayList<Node<K, V>>();
		_edges = new ArrayList<Edge<K, V>>();
	}

	void addNode(K name) {
		_nodes.add(new Node<K, V>(name, null));
	}

	public void addEdge(Node<K, V> source, Node<K, V> target, long weight) {
		if (!hasNode(source)) {
			System.out.println("Missing source node " + source);
			return;
		}

		if (!hasNode(target)) {
			System.out.println("Missing target node " + target);
			return;
		}

		_edges.add(new Edge<K, V>(source, target, weight));
	}

	public void addEdge(Node<K, V> source, Node<K, V> target) {
		addEdge(source, target, 1);
	}

	public boolean hasEdge(Edge<K, V> e) {
		return _edges.contains(e);
	}

	public boolean hasEdge(Node<K, V> source, Node<K, V> target) {
		return _edges.contains(new Edge<K, V>(source, target, 0));
	}

	public boolean hasNode(Node<K, V> node) {
		return _nodes.contains(node);
	}

	public boolean hasNode(K name, V value) {
		return _nodes.contains(new Node<K, V>(name, value));
	}

	public List<Edge<K, V>> getEdgesFrom(Node<K, V> node) {
		return _nodeEdges.get(node);
	}

	public List<Edge<K, V>> getEdgesTo(Node<K, V> node) {
		List<Edge<K, V>> edges = new ArrayList<>();
		for (Edge<K, V> edge : _edges) {
			if (edge.hasTarget(node)) {
				edges.add(edge);
			}
		}

		return edges;
	}

	public List<Node<K, V>> adjecencies(Node<K, V> node) {
		return _nodeAdjecencies.get(node);
	}

	public void dfs(Node<K, V> start, BiFunction<Node<K, V>, Node<K, V>, Boolean> visitor) {

		Set<Node<K, V>> visited = new HashSet<>();
		Stack<Node<K, V>> stack = new Stack<>();

		stack.push(start);
		while (!stack.isEmpty()) {
			Node<K, V> current = stack.pop();

			if (visited.contains(current)) {
				continue;
			}

			visited.add(current);

			List<Node<K, V>> neighbours = adjecencies(current);
			for (Node<K, V> neighbour : neighbours) {
				if (!visitor.apply(current, neighbour)) {
					continue;
				}

				if (!visited.contains(neighbour)) {
					stack.push(neighbour);
				}
			}
		}
	}

	public void bfs(Node<K, V> start, BiFunction<Node<K, V>, Node<K, V>, Boolean> visitor) {
		Set<Node<K, V>> visited = new HashSet<>();
		Queue<Node<K, V>> queue = new LinkedList<>();
		queue.add(start);

		while (!queue.isEmpty()) {
			Node<K, V> current = queue.poll();
			if (visited.contains(current)) {
				continue;
			}
			visited.add(current);

			List<Node<K, V>> adjecencies = adjecencies(current);
			for (Node<K, V> neighbor : adjecencies) {
				if (!visitor.apply(current, neighbor)) {
					continue;
				}
				if (!visited.contains(neighbor)) {
					queue.add(neighbor);
				}
			}
		}
	}

	public Map<Node<K, V>, Long> shortestPath(Node<K, V> start) {
		if (start == null) {
			start = _nodes.get(0);
		}

		Map<Node<K, V>, Long> distances = new HashMap<>();
		PriorityQueue<NodeDistance> pq = new PriorityQueue<>(Comparator.comparingLong(nd -> nd.distance));

		for (Node<K, V> node : _nodes) {
			distances.put(node, Long.MAX_VALUE);
		}

		distances.put(start, 0L);
		pq.add(new NodeDistance(start, 0L));

		while (!pq.isEmpty()) {
			NodeDistance current = pq.poll();
			Node<K, V> currentNode = current.node;

			if (distances.get(currentNode) < current.distance) {
				continue;
			}

			long currentDistance = distances.get(currentNode);

			for (Edge<K, V> edge : _nodeEdges.getOrDefault(currentNode, new ArrayList<>())) {
				Node<K, V> neighbor = edge.getTarget();
				long newDistance = currentDistance + edge.getWeight();

				if (newDistance < distances.get(neighbor)) {
					distances.put(neighbor, newDistance);
					pq.add(new NodeDistance(neighbor, newDistance));
				}
			}
		}

		return distances;
	}

	public static <K, V> Graph<K, Point> fromGrid(Grid<V> g, Direction dir, Function<V, K> converter) {
		List<Edge<K, Point>> _edges = new ArrayList<>();
		List<Node<K, Point>> _nodes = new ArrayList<>();

		g.traverse((point, gridElement) -> {
			Node<K, Point> source = new Node<>(converter.apply(gridElement), point);
			_nodes.add(source);

			for (var neighbourPoint : g.neighbourIndexes(point, dir)) {
				Node<K, Point> target = new Node<>(converter.apply(g.at(neighbourPoint)), neighbourPoint);
				Edge<K, Point> edge = new Edge<>(source, target);
				_nodes.add(target);
				_edges.add(edge);
			}
		});

//		System.out.println(System.currentTimeMillis() - now);
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
		Node<K, V> node;
		long distance;

		NodeDistance(Node<K, V> node, long distance) {
			this.node = node;
			this.distance = distance;
		}
	}
}
