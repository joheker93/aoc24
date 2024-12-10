package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class Sequence<T> {

	public static List<Integer> intSequence(String input) {
		return sequence(input, Integer::parseInt);
	}

	public static List<Double> doubleSequence(String input) {
		return sequence(input, Double::parseDouble);
	}

	public static <T extends Enum<T>> List<T> enumSequence(String input, Class<T> enumType) {
		return sequence(input, s -> {
			return Parsing.asEnum(enumType, s);
		});
	}

	public static <T> List<T> sequence(String input, Function<String, T> converter) {
		return sequence(input, ",", converter);
	}

	public static <T> List<T> sequence(String input, String splitPattern, Function<String, T> converter) {
		List<String> parts = Parsing.split(input, splitPattern);
		List<T> objects = new ArrayList<>();

		for (var word : parts) {
			objects.add(converter.apply(word.strip()));
		}

		return objects;

	}

	public static <T> List<T> dropWhile(Predicate<T> p, List<T> elems) {
		return elems.stream().dropWhile(p).collect(Collectors.toList());
	}

	public static <T> List<T> takeWhile(Predicate<T> p, List<T> elems) {
		return elems.stream().takeWhile(p).collect(Collectors.toList());
	}

	@SafeVarargs
	public static <T> List<T> concat(List<? extends T>... lists) {
		List<T> result = new ArrayList<>();
		for (var l : lists) {
			result.addAll(l);
		}
		return result;
	}

	List<T> getList(List<T> t) {
		return t;
	}

	public static long sum(List<Long> values) {
		long sum = 0;
		for (var val : values) {
			sum += val;
		}

		return sum;
	}

}
