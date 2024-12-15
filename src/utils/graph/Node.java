package utils.graph;

import java.util.Objects;

public class Node<V,I> {

	private final V _value;
	private final I _identifier;

	public Node(V value, I identifier) {
		_value = value;
		_identifier = identifier;
	}

	public Node(V value) {
		_value = value;
		_identifier = null;

	}

	public V getValue() {
		return _value;
	}

	public I getIdentifier() {
		return _identifier;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_value, _identifier);
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
		Node<?,?> other = (Node<?,?>) obj;
		return Objects.equals(_value, other._value) && Objects.equals(_identifier, other._identifier);
	}
	
	@Override
	public String toString() {
		return _value + " @ " + _identifier;
	}

}
