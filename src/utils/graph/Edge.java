package utils.graph;

import java.util.Objects;

public class Edge<K,V> {
	private final Node<K,V> _source;
	private final Node<K,V> _target;
	private final long _weight;

	public Edge(final Node<K,V> source, final Node<K,V> target, final long weight) {
		_source = source;
		_target = target;
		_weight = weight;
	}

	public Edge(final Node<K,V> source, final Node<K,V> target) {
		_source = source;
		_target = target;
		_weight = 1;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_source, _target);
	}

	public boolean hasSource(final Node<?,?> node) {
		return _source.equals(node);
	}

	public boolean hasTarget(final Node<?,?> node) {
		return _target.equals(node);
	}

	public Node<K,V> getTarget() {
		return _target;
	}

	public Node<K,V> getSource() {
		return _source;
	}

	public long getWeight() {
		return _weight;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Edge<?,?> other = (Edge<?,?>) obj;
		return Objects.equals(_source, other._source) && Objects.equals(_target, other._target);
	}
	
	@Override
	public String toString() {
		return _source + " --> " + _target;
	}

}
