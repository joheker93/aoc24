package utils.graph;

import java.util.Objects;

public class Node<T> {

	private final String _name;
	private final T _value;

	public Node(String name, T value) {
		_name = name;
		_value = value;
	}

	public Node(String name) {
		_name = name;
		_value = null;

	}

	public String get_name() {
		return _name;
	}

	public T get_value() {
		return _value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_name, _value);
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
		Node<?> other = (Node<?>) obj;
		return Objects.equals(_name, other._name) && Objects.equals(_value, other._value);
	}
	
	@Override
	public String toString() {
		return _name;
	}

}
