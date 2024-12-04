package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

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
		List<String> words = Parsing.words(input, splitPattern);
		List<T> objects = new ArrayList<>();

		for (var word : words) {
			objects.add(converter.apply(word.strip()));
		}

		return objects;

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

}
