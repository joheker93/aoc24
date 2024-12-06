package utils;

import java.util.Objects;

public class Pair<K, V> {
	private final K _fst;
	private final V _snd;

	private Pair(K fst, V snd) {
		_fst = fst;
		_snd = snd;
	}

	public static <K, V> Pair<K, V> of(K fst, V snd) {
		return new Pair<K, V>(fst, snd);
	}

	public K fst() {
		return _fst;
	}

	public V snd() {
		return _snd;
	}

	public Pair<V, K> flip() {
		return Pair.of(_snd, _fst);
	}

	@Override
	public int hashCode() {
		return _fst.hashCode() * 31 + _snd.hashCode();
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
		Pair<?,?> other = (Pair<?,?>) obj;
		return Objects.equals(_fst, other._fst) && Objects.equals(_snd, other._snd);
	}

	@Override
	public String toString() {
		return "(" + _fst + "," + _snd + ")";
	}
}
