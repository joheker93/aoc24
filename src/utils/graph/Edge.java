package utils.graph;

import java.util.Objects;

public class Edge<T> {
	private final Node<T> _source;
	private final Node<T> _target;
	private final long _weight;

	public Edge(final Node<T> source, final Node<T> target, final long weight) {
		_source = source;
		_target = target;
		_weight = weight;
	}

	public Edge(final Node<T> source, final Node<T> target) {
		_source = source;
		_target = target;
		_weight = 1;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_source, _target);
	}

	public boolean hasSource(final Node<?> node) {
		return _source.equals(node);
	}

	public boolean hasTarget(final Node<?> node) {
		return _target.equals(node);
	}

	public Node<T> getTarget() {
		return _target;
	}

	public Node<T> getSource() {
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
		final Edge<?> other = (Edge<?>) obj;
		return Objects.equals(_source, other._source) && Objects.equals(_target, other._target);
	}

}
